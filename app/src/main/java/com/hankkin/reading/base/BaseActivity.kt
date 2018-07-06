package com.hankkin.reading.base

import android.app.Activity
import android.app.ActivityManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.WindowManager
import com.bilibili.magicasakura.utils.ThemeUtils
import com.hankkin.reading.R

/**
 * Created by huanghaijie on 2018/5/15.
 */
abstract class BaseActivity: FragmentActivity() {

    protected var activity: Activity? = null

    protected abstract fun getLayoutId(): Int

    protected abstract fun initData()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this
        if (getLayoutId() != 0) setContentView(getLayoutId())
        initViews(savedInstanceState)
        initData()
    }


    protected abstract fun initViews(savedInstanceState: Bundle?)
}