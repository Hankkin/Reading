package com.hankkin.reading.http.protocol

/**
 * Created by huanghaijie on 2018/5/16.
 */
class HttpFactoryGroup {

    private val map = HashMap<Class<out HttpInterface>, HttpInterface>()

    fun register(key: Class<out HttpInterface>, value: HttpInterface) {
        if (key.isAssignableFrom(value::class.java)) map[key] = value
        else throw IllegalStateException("Http interface register error ： value implements key ?")
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : HttpInterface> getProtocol(key: Class<T>): T {
        val protocol = map[key] ?: throw IllegalStateException("Http interface unregistered")
        if (key.isAssignableFrom(protocol::class.java)) return protocol as T
        else throw IllegalStateException("Http interface register error ： value implements key ?")
    }
}