package com.hankkin.reading.utils

import android.annotation.SuppressLint
import android.content.Context

@SuppressLint("StaticFieldLeak")
object Utils{
     lateinit var context: Context
     lateinit var spUtils: SPUtils

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    fun init(context: Context){
        Utils.context = context.applicationContext
        spUtils = SPUtils("utilcode")
    }

    fun getmContext(): Context{
        if (context != null) return  context
        throw NullPointerException("u should init first")
    }

    fun getSputils(): SPUtils{
        return spUtils
    }
}