package com.hankkin.library.http

import okhttp3.CookieJar
import okhttp3.Interceptor

/**
 * Created by huanghaijie on 2018/8/23.
 */
object HttpConfig {

    //header参数
    private var mHeaderMap: Map<String, Any>? = null
    //公共参数
    private var mParamsMap: Map<String, Any>? = null
    //OKhttp拦截器
    private var mInterceptor: Interceptor? = null
    //Cookie
    private var mCookie: CookieJar? = null

    fun setHeaders(map: Map<String, Any>?): HttpConfig {
        map?.apply { mHeaderMap = map }
        return this
    }

    fun setParams(map: Map<String, Any>?): HttpConfig {
        map?.apply { mParamsMap = map }
        return this
    }

    fun setInterceptor(interceptor: Interceptor?): HttpConfig {
        interceptor?.apply { mInterceptor = interceptor }
        return this
    }

    fun setCookie(cookieJar: CookieJar?): HttpConfig {
        cookieJar?.apply { mCookie = cookieJar }
        return this
    }

    fun getHeaders(): Map<String, Any>? {
        return mHeaderMap
    }

    fun getParams(): Map<String, Any>? {
        return mParamsMap
    }

    fun getInterceptor(): Interceptor? {
        return mInterceptor
    }

    fun getCookie(): CookieJar? {
        return mCookie
    }

}