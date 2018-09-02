package com.hankkin.library.http

import okhttp3.CookieJar
import okhttp3.Interceptor
import java.lang.StringBuilder

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

    fun encodeCookie(cookies: List<String>): String {
        val sb = StringBuilder()
        val set = HashSet<String>()
        cookies
                .map { cookie ->
                    cookie.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                }
                .forEach {
                    it.filterNot { set.contains(it) }.forEach { set.add(it) }
                }
        val ite = set.iterator()
        while (ite.hasNext()) {
            val cookie = ite.next()
            sb.append(cookie).append(";")
        }
        val last = sb.lastIndexOf(";")
        if (sb.length - 1 == last) {
            sb.deleteCharAt(last)
        }
        return sb.toString()
    }


}