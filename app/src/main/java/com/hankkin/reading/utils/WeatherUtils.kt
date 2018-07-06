package com.hankkin.reading.utils

import android.content.Context

/**
 * Created by huanghaijie on 2018/5/17.
 */
object WeatherUtils{

    public fun getWeatherImg(code: String?,context: Context?): Int{
        val rescoure = context!!.resources
        return rescoure.getIdentifier("w"+code,"mipmap", context!!.packageName)
    }

}