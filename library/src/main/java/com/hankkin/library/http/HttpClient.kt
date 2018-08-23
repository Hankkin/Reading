package com.hankkin.library.http

import com.hankkin.library.http.interceptor.NetLogInterceptor
import com.hankkin.library.utils.LogUtils
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

/**
 * Created by huanghaijie on 2018/5/16.
 */
object HttpClient {

    private const val DEFAULT_TIME_OUT = 15000L


    fun getBuilder(apiUrl: String): Retrofit.Builder {
        val builder = Retrofit.Builder()
        builder.client(mHttpClient)
        builder.baseUrl(apiUrl)//设置远程地址
        builder.addConverterFactory(GsonConverterFactory.create())
        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        return builder
    }

    private val mHttpClient by lazy {
        OkHttpClient.Builder()
                .sslSocketFactory(createSSLSocketFactory())
                .hostnameVerifier { _, _ -> true }
                .addInterceptor({ chain -> addHeader(chain) })
                .addInterceptor(NetLogInterceptor(NetLogInterceptor.Level.BODY) { LogUtils.d(it) })
                .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.MILLISECONDS)
                .cookieJar(HttpConfig.getCookie())
                .build()
    }


    private fun addHeader(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val build = request.newBuilder()
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .addHeader("Platform", "Android")
                .addHeader("Version", "1.0.0")
                .addHeader("User-Agent", "(Windows NT 10.0; Win64; x64; rv:60.0) Gecko/20100101 Firefox/60.0")
                .build()
        return chain.proceed(build)
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


}