package com.hankkin.easyword.http.protocol

import com.hankkin.easyword.http.impl.TranslateHttpProtocol
import com.hankkin.easyword.http.inter.ISearchWordHttp
import com.hankkin.easyword.http.inter.IWeatherHttp

/**
 * Created by huanghaijie on 2018/5/16.
 */
object HttpFactory {

    private val mHttpGroup = HttpFactoryGroup()

    init {
        // 在此处注册Http接口
        mHttpGroup.register(IWeatherHttp::class.java, TranslateHttpProtocol)
        mHttpGroup.register(ISearchWordHttp::class.java, TranslateHttpProtocol)
    }

    /**
     * 调用该方法获取Protocol前，需先在init中注册接口
     * */
    fun <T : HttpInterface> getProtocol(clazz: Class<T>): T {
        return mHttpGroup.getProtocol(clazz)
    }

}