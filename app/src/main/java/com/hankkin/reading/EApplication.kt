package com.hankkin.reading

import android.app.Application
import android.content.Context
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper
import com.bilibili.magicasakura.utils.ThemeUtils
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.hankkin.library.http.HttpConfig
import com.hankkin.library.utils.AppUtils
import com.hankkin.library.utils.FileUtils
import com.hankkin.library.utils.SPUtils
import com.hankkin.library.widget.toasty.Toasty
import com.hankkin.reading.common.Constant
import com.hankkin.reading.greendao.DaoMaster
import com.hankkin.reading.greendao.DaoSession
import com.hankkin.reading.greendao.GreenOpenHelper
import com.hankkin.reading.utils.ThemeHelper
import com.hankkin.reading.utils.ThemeHelper.*
import com.squareup.leakcanary.LeakCanary
import com.tencent.bugly.Bugly
import com.youdao.sdk.app.YouDaoApplication

/**
 * Created by huanghaijie on 2018/5/18.
 */
class EApplication : Application(), ThemeUtils.switchColor {


    private lateinit var daoSession: DaoSession

    companion object {
        private var instance: EApplication? = null

        fun instance() = instance!!
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        FileUtils.initSd(AppUtils.getAppName(this)!!)
        SPUtils.init(this, Constant.COMMON.SP_NAME)
        initCommon()
        initHttp()
        initLeakCanary()
        initDao()
        YouDaoApplication.init(this, "46dbe20b62a7eae3")
        Bugly.init(this, "61fd6ca178", false)
    }

    private fun initHttp() {
        HttpConfig.setCookie(PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(this)))
    }

    private fun initCommon() {
        ThemeUtils.setSwitchColor(this)
        BGASwipeBackHelper.init(this, null)
        Toasty.Config.getInstance()
                .setInfoColor(resources.getColor(ThemeHelper.getCurrentColor(this)))
                .apply(this)
    }

    private fun initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        LeakCanary.install(this)
    }

    private fun initDao() {
        val devOpenHelper = GreenOpenHelper(this, Constant.COMMON.DB_NAME, null)
        val daoMaster = DaoMaster(devOpenHelper.writableDb)
        daoSession = daoMaster.newSession()
    }

    override fun replaceColorById(context: Context, @ColorRes colorId: Int): Int {
        if (ThemeHelper.isDefaultTheme(context)) {
            return context.resources.getColor(colorId)
        }
        val theme = getTheme(context)
        var colorIdTemp = colorId
        if (theme != null) {
            colorIdTemp = getThemeColorId(context, colorId, theme)
        }
        return context.resources.getColor(colorIdTemp)
    }

    override fun replaceColor(context: Context, @ColorInt originColor: Int): Int {
        if (ThemeHelper.isDefaultTheme(context)) {
            return originColor
        }
        val theme = getTheme(context)
        var colorId = -1

        if (theme != null) {
            colorId = getThemeColor(context, originColor.toLong(), theme)
        }
        return if (colorId != -1) resources.getColor(colorId) else originColor
    }

    private fun getTheme(context: Context): String? {
        return when (ThemeHelper.getTheme(context)) {
            COLOR_YIMA -> "yima"
            COLOR_KUAN -> "kuan"
            COLOR_BILI -> "bili"
            COLOR_YIDI -> "yidi"
            COLOR_SHUIYA -> "shuiya"
            COLOR_YITENG -> "yiteng"
            COLOR_JILAO -> "jilao"
            COLOR_ZHIHU -> "zhihu"
            COLOR_GUTONG -> "gutong"
            COLOR_DIDIAO -> "didiao"
            COLOR_GAODUAN -> "gaoduan"
            COLOR_APING -> "aping"
            COLOR_LIANGBAI -> "liangbai"
            COLOR_ANLUOLAN -> "anluolan"
            COLOR_XINGHONG -> "xinghong"
            else -> {
                "yima"
            }
        }
    }

    @ColorRes
    private fun getThemeColorId(context: Context, colorId: Int, theme: String): Int {
        when (colorId) {
            R.color.theme_color_primary -> return context.resources.getIdentifier(theme, "color", packageName)
            R.color.theme_color_primary_dark -> return context.resources.getIdentifier(theme + "_dark", "color", packageName)
            R.color.colorAccent -> return context.resources.getIdentifier(theme + "_accent", "color", packageName)
        }
        return colorId
    }

    @ColorRes
    private fun getThemeColor(context: Context, color: Long, theme: String): Int {
        when (color) {
            0xfff44336 -> return context.resources.getIdentifier(theme, "color", packageName)
            0xfff44336 -> return context.resources.getIdentifier(theme + "_dark", "color", packageName)
            0xfff44336 -> return context.resources.getIdentifier(theme + "_accent", "color", packageName)
        }
        return -1
    }

    fun getDaoSession(): DaoSession {
        return daoSession
    }
}