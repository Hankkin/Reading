package com.hankkin.reading.http

import com.hankkin.reading.common.Constant
import com.hankkin.reading.http.api.GankApi
import com.hankkin.reading.http.api.OsChinaApi
import com.hankkin.reading.http.api.ToolsApi
import com.hankkin.reading.http.api.WanAndroidApi

interface HttpClientUtils {

    object Builder {
        fun getCommonHttp(): WanAndroidApi {
            return ApiFactory.instance.create(WanAndroidApi::class.java, Constant.WanAndroidUrl.BASE_URL)
        }

        fun getOsChinaHttp(): OsChinaApi {
            return ApiFactory.instance.create(OsChinaApi::class.java, Constant.OSChinaUrl.BASE_URL)
        }

        fun getToolsHttp(): ToolsApi {
            return ApiFactory.instance.create(ToolsApi::class.java, Constant.ToolsUrl.WEATHER_URL)
        }

        fun getGankHttp(): GankApi{
            return ApiFactory.instance.create(GankApi::class.java,Constant.GankUrl.BASE_URL)
        }
    }
}