package com.hankkin.library.http.interceptor

import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.internal.http.HttpHeaders
import okio.Buffer
import java.io.IOException
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

/**
 * Created by huanghaijie on 2018/5/16.
 */
class NetLogInterceptor @JvmOverloads constructor(lv: Level = Level.BODY, private val logger: (String) -> Unit = ::println) : Interceptor {

    @Volatile
    private var level = Level.NONE

    enum class Level {
        /**
         * No logs.
         */
        NONE,
        /**
         * Logs request and response lines.
         *
         *
         * Example:
         * <pre>`--> POST /greeting HTTP/1.1 (3-byte body)
         * <p/>
         * <-- HTTP/1.1 200 OK (22ms, 6-byte body)
        `</pre> *
         */
        BASIC,
        /**
         * Logs request and response lines and their respective headers.
         *
         *
         * Example:
         * <pre>`--> POST /greeting HTTP/1.1
         * Host: example.com
         * Content-Type: plain/text
         * Content-Length: 3
         * --> END POST
         * <p/>
         * <-- HTTP/1.1 200 OK (22ms)
         * Content-Type: plain/text
         * Content-Length: 6
         * <-- END HTTP
        `</pre> *
         */
        HEADERS,
        /**
         * Logs request and response lines and their respective headers and bodies (if present).
         *
         *
         * Example:
         * <pre>`--> POST /greeting HTTP/1.1
         * Host: example.com
         * Content-Type: plain/text
         * Content-Length: 3
         * <p/>
         * Hi?
         * --> END GET
         * <p/>
         * <-- HTTP/1.1 200 OK (22ms)
         * Content-Type: plain/text
         * Content-Length: 6
         * <p/>
         * Hello!
         * <-- END HTTP
        `</pre> *
         */
        BODY
    }

    init {
        this.level = lv
    }

    /**
     * Change the level at which this interceptor logs.
     */
    fun setLevel(level: Level?): NetLogInterceptor {
        if (level == null) throw NullPointerException("level == null. Use Level.NONE instead.")
        this.level = level
        return this
    }

    fun getLevel(): Level {
        return level
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val level = this.level

        val request = chain.request()
        if (level == Level.NONE) {
            return chain.proceed(request)
        }

        val logBody = level == Level.BODY
        val logHeaders = logBody || level == Level.HEADERS

        val requestBody = request.body()
        val hasRequestBody = requestBody != null

        val connection = chain.connection()

        val protocol = if (connection != null) connection.protocol() else Protocol.HTTP_1_1
        val requestStartMessage = StringBuilder()
        requestStartMessage.append("--> " + request.method() + ' ' + request.url() + ' ' + protocol(protocol))
        if (!logHeaders && hasRequestBody) {
            requestStartMessage.append(" (" + requestBody!!.contentLength() + BYTE_BODY)
        }
        logger.invoke(requestStartMessage.toString())

        if (logHeaders) {
            if (hasRequestBody) {
                // Request body headers are only present when installed as a network interceptor. Force
                // them to be included (when available) so there values are known.
                if (requestBody!!.contentType() != null) {
                    logger.invoke("Content-Type: " + requestBody.contentType()!!)
                }
                if (requestBody.contentLength() != -1L) {
                    logger.invoke("Content-Length: " + requestBody.contentLength())
                }
            }

            val headers = request.headers()
            var i = 0
            val count = headers.size()
            while (i < count) {
                val name = headers.name(i)
                // Skip headers from the request body as they are explicitly logged above.
                if (!"Content-Type".equals(name, ignoreCase = true) && !"Content-Length".equals(name, ignoreCase = true)) {
                    logger.invoke(name + ": " + headers.value(i))
                }
                i++
            }

            if (!logBody || !hasRequestBody) {
                logger.invoke(END + request.method())
            } else if (bodyEncoded(request.headers())) {
                logger.invoke(END + request.method() + " (encoded body omitted)")
            } else {
                val buffer = Buffer()
                requestBody!!.writeTo(buffer)

                val charset = UTF8
                val contentType = requestBody.contentType()
                contentType?.charset(UTF8)

                logger.invoke("")
                logger.invoke(buffer.readString(charset))

                logger.invoke(END + request.method()
                        + " (" + requestBody.contentLength() + BYTE_BODY)
            }
        }

        val startNs = System.nanoTime()
        val response = chain.proceed(request)
        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)

        val responseBody = response.body()
        logger.invoke("<-- " + response.code() + ' ' + response.message() + ' '
                + response.request().url() + " (" + tookMs + "ms" + (if (!logHeaders)
            ", "
                    + responseBody!!.contentLength() + "-byte body"
        else
            "") + ')')

        if (logHeaders) {
            val headers = response.headers()
            var i = 0
            val count = headers.size()
            while (i < count) {
                logger.invoke(headers.name(i) + ": " + headers.value(i))
                i++
            }

            if (!logBody || !HttpHeaders.hasBody(response)) {
                logger.invoke("<-- END HTTP")
            } else if (bodyEncoded(response.headers())) {
                logger.invoke("<-- END HTTP (encoded body omitted)")
            } else {
                val source = responseBody!!.source()
                source.request(java.lang.Long.MAX_VALUE) // Buffer the entire body.
                val buffer = source.buffer()

                var charset = UTF8
                val contentType = responseBody.contentType()
                if (contentType != null) {
                    charset = contentType.charset(UTF8)
                }

                if (responseBody.contentLength() != 0L) {
                    logger.invoke("")
                    logger.invoke(buffer.clone().readString(charset))
                }

                logger.invoke("<-- END HTTP (" + buffer.size() + BYTE_BODY)
            }
        }

        return response
    }

    private fun bodyEncoded(headers: Headers): Boolean {
        val contentEncoding = headers.get("Content-Encoding")
        return contentEncoding != null && !contentEncoding.equals("identity", ignoreCase = true)
    }

    companion object {

        private val BYTE_BODY = "-byte body)"

        private val END = "--> END "

        private val UTF8 = Charset.forName("UTF-8")

        private fun protocol(protocol: Protocol): String {
            return if (protocol == Protocol.HTTP_1_0) "HTTP/1.0" else "HTTP/1.1"
        }
    }

}