package com.hankkin.reading.utils

import com.google.gson.Gson

/**
 * Created by huanghaijie on 2018/8/10.
 */
object JsonUtils{

    fun jsonToObject(json: String,clazz: Class<*>): Any{
        val gson = Gson()
        return gson.fromJson(json,clazz)
    }

    fun objToJson(obj: Any): String{
        val gson = Gson()
        return gson.toJson(obj)
    }

}