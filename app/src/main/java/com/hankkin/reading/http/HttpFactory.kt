package com.hankkin.reading.http

import java.lang.ref.WeakReference

/**
 * Created by huanghaijie on 2018/5/23.
 */
class HttpFactory {

    val map = HashMap<Class<*>, WeakReference<Any>>()

//    fun <T> getService(clazz: Class<T>): WeakReference<Any>? {
//        if (!map.containsKey(clazz)) {
//            val value = HttpClient.getnorRetrofit().create(clazz)
//            val realValue = WeakReference(value)
//            map.put(clazz, realValue)
//        }
//        return map[clazz]
//    }
}