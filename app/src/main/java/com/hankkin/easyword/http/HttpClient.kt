package com.hankkin.easyword.http

import com.hankkin.easyword.http.interceptor.NetLogInterceptor
import com.hankkin.easyword.utils.FileUtils
import com.hankkin.easyword.utils.LogUtils
import io.reactivex.Observable
import okhttp3.*
import java.io.IOException
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager
import java.io.File
import java.io.FileOutputStream


/**
 * Created by huanghaijie on 2018/5/16.
 */
object HttpClient {

    private const val DEFAULT_TIME_OUT = 5000L

    const val METHOD_GET: String = "GET"
    const val METHOD_POST: String = "POST"
    const val METHOD_PUT: String = "PUT"
    const val METHOD_DELETE: String = "DELETE"

    private val mHttpClient by lazy {
        OkHttpClient.Builder()
                .sslSocketFactory(createSSLSocketFactory())
                .hostnameVerifier { _, _ -> true }
//                .addInterceptor(AddCookiesInterceptor())
                .addInterceptor(NetLogInterceptor(NetLogInterceptor.Level.BODY) { LogUtils.d(it) })
                .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.MILLISECONDS)
                .build()
    }

    private fun createSSLSocketFactory(): SSLSocketFactory {
        val sc = SSLContext.getInstance("SSL")
        sc.init(null, arrayOf(TrustAllCerts()), java.security.SecureRandom())
        return sc.socketFactory
    }

    private class TrustAllCerts : X509TrustManager {

        @Throws(CertificateException::class)
        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
        }

        @Throws(CertificateException::class)
        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
        }

        override fun getAcceptedIssuers(): Array<X509Certificate?> {
            return arrayOfNulls(0)
        }
    }


    /**
     * 同步
     * */
    fun execute(request: Request): Response = mHttpClient.newCall(request).execute()

    /**
     * 异步
     * */
    fun enqueue(request: Request, responseCallback: Callback) = mHttpClient.newCall(request).enqueue(responseCallback)

    /**
     * 下载文件
     */
    fun downFile(filePath: String, url: String): Observable<String> {
        return Observable.create<String> {
            try {
                val request = Request.Builder().url(url).build()
                val response = HttpClient.execute(request)
                if (response.code() in 200..300) {
                    val savePath = FileUtils.isExistDir(filePath)
                    val file = File(savePath,FileUtils.getNameFromUrl(url))
                    it.onNext(file.absolutePath)
                }
                else{
                    it.onError(Throwable("http fail"))
                }


            } catch (e: Exception) {
                it.onError(e)
            }
        }

    }



    /**
     * 创建Request
     * */
    fun getRequest(url: String, method: String, params: Map<String, String>?): Request {
        val builder = Request.Builder()
        if (METHOD_GET.equals(method, true)) {
            builder.url(initGetRequest(url, params)).get()
        } else if (METHOD_POST.equals(method, true)) {
            builder.url(url).post(initRequestBody(params))
        } else if (METHOD_PUT.equals(method, true)) {
            builder.url(url).put(initRequestBody(params))
        } else if (METHOD_DELETE.equals(method, true)) {
            if (params == null || params.isEmpty()) {
                builder.url(url).delete()
            } else {
                builder.url(url).delete(initRequestBody(params))
            }
        }
        return builder.build()
    }


    /**
     * 创建Body请求体
     * */
    private fun initRequestBody(params: Map<String, String>?): RequestBody {
        val bodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
        val newParams = addCommonParams(params)
        newParams.map {
            bodyBuilder.addFormDataPart(it.key, it.value)
        }
        return bodyBuilder.build()
    }


    /**
     * 创建Get链接
     * */
    private fun initGetRequest(url: String, params: Map<String, String>?): String {
        return StringBuilder(url).apply {
            val newParams = addCommonParams(params)
            if (newParams.isNotEmpty()) {
                append("?")
                newParams.map {
                    append(it.key).append("=").append(it.value).append("&")
                }
                delete(length - "&".length, length)
            }
        }.toString()
    }

    private fun addCommonParams(params: Map<String, String>?): Map<String, String> {
        val newParams = HashMap<String, String>()
//        newParams["platform"] = "APP_ANDROID"
//        newParams["version"] = BuildConfig.VERSION_NAME
//        newParams["uid"] = DeviceInfoUtils.getImei()
        params?.let { newParams.putAll(it) }
        return newParams
    }

    interface OnDownloadFileListener {
        fun downloadSuccess(path: String)
        fun downloading(progress: Int)
        fun downloadFail()
    }

}