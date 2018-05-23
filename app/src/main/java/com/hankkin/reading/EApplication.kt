package com.hankkin.reading

import android.app.Application
import com.hankkin.reading.utils.FileUtils
import com.hankkin.reading.utils.SPUtils
import com.tbruyelle.rxpermissions2.RxPermissions

/**
 * Created by huanghaijie on 2018/5/18.
 */
class EApplication : Application() {

    companion object {
        private var instance: EApplication? = null

        fun instance() = instance!!
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        FileUtils.initSd()
        SPUtils.init(this)
    }

}