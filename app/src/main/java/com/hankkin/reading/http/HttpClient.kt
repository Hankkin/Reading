package com.hankkin.reading.http

import com.hankkin.reading.EApplication
import com.hankkin.reading.common.Constant
import com.hankkin.reading.http.cookie.AddCookiesInterceptor
import com.hankkin.reading.http.cookie.ReceivedCookiesInterceptor
import com.hankkin.reading.http.interceptor.NetLogInterceptor
import com.hankkin.reading.utils.LogUtils
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


    private val mHttpClient by lazy {
        OkHttpClient.Builder()
                .sslSocketFactory(createSSLSocketFactory())
                .hostnameVerifier { _, _ -> true }
                .addInterceptor(ReceivedCookiesInterceptor(EApplication.instance()))
                .addInterceptor(AddCookiesInterceptor(EApplication.instance()))
                .addInterceptor({ chain -> addHeader(chain) })
                .addInterceptor(NetLogInterceptor(NetLogInterceptor.Level.BODY) { LogUtils.d(it) })
                .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.MILLISECONDS)
                .build()
    }

    private val jsonRetrofit by lazy {
        Retrofit.Builder()
                .client(mHttpClient)
                .baseUrl(Constant.ConfigUrl.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    private val weatherRetrofit by lazy {
        Retrofit.Builder()
                .client(mHttpClient)
                .baseUrl(Constant.ConfigUrl.WEATHER_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }



    private  fun addHeader(chain: Interceptor.Chain) : Response{
        val request = chain.request()
        val build = request.newBuilder()
                .addHeader("X-Requested-With","XMLHttpRequest")
                .addHeader("Platform","Android")
                .addHeader("Version","1.0.0")
                .addHeader("User-Agent","(Windows NT 10.0; Win64; x64; rv:60.0) Gecko/20100101 Firefox/60.0")
                .build()
        return chain.proceed(build)
    }


    fun getnorRetrofit() = jsonRetrofit
    fun getWeaRetrofit() = weatherRetrofit

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