package com.hankkin.reading.dao

import java.util.*
/**
 * @author Hankkin
 * @date 2018/08/10
 */
class DaoFactoryHolder {

    private val map = WeakHashMap<Class<out BaseDaoContract>, BaseDaoContract>()

    fun add(key: Class<out BaseDaoContract>, value: BaseDaoContract) {
        if (key.isAssignableFrom(value::class.java)) map[key] = value
        else throw IllegalAccessException("Please let your Dao implements DaoContract")
    }

    fun <T : BaseDaoContract> getProtocol(key: Class<T>): T? {
        val protocol = map[key]
        return if (protocol == null) null
        else protocol as T
    }
}