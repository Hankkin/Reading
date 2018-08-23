package com.hankkin.library.http.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class AddCookiesInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        //TODO  add Http Cookies
        return chain.proceed(builder.build())
    }
}