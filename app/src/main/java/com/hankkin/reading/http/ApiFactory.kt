package com.hankkin.reading.http

import com.hankkin.reading.common.Constant


class ApiFactory {

    companion object {
        var instance: ApiFactory

        init {
            instance = ApiFactory()
        }
    }
    private var commonApi: Any? = null
    private var toolsApi: Any? = null
    private var osChinaApi: Any? = null

    fun <T> create(clazz: Class<T>, type: String): T {
        when (type) {
            Constant.WanAndroidUrl.BASE_URL -> {
                if (commonApi == null) {
                    synchronized(ApiFactory::class.java) {
                        commonApi = HttpClientUtils.getBuilder(type).build().create(clazz)
                    }
                }
                return commonApi as T
            }
            Constant.ToolsUrl.WEATHER_URL -> {
                if (toolsApi == null) {
                    synchronized(ApiFactory::class.java) {
                        toolsApi = HttpClientUtils.getBuilder(type).build().create(clazz)
                    }
                }
                return toolsApi as T
            }
            Constant.OSChinaUrl.BASE_URL -> {
                if (osChinaApi == null) {
                    synchronized(ApiFactory::class.java) {
                        osChinaApi = HttpClientUtils.getBuilder(type).build().create(clazz)
                    }
                }
                return osChinaApi as T
            }
            else -> {
                return commonApi as T
            }
        }
    }
}