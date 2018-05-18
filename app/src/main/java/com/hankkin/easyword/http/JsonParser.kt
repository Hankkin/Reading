package com.hankkin.easyword.http

import com.google.gson.Gson

/**
 * Created by huanghaijie on 2018/5/16.
 */
object JsonParser {

    private val mGson by lazy { Gson() }
    private val googleJsonParser by lazy { com.google.gson.JsonParser() }


    fun <T> fromJsonObj(json: String, clazz: Class<T>): T = mGson.fromJson(json, clazz)

    fun <T> fromJsonArr(json: String, clazz: Class<T>): MutableList<T> {
        val result = ArrayList<T>()
        val jsonArray = googleJsonParser.parse(json).asJsonArray
        jsonArray.mapTo(result) { mGson.fromJson(it, clazz) }
        return result
    }

}