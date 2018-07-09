package com.hankkin.reading.base

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.hankkin.library.utils.StatusBarUtil
import com.hankkin.reading.utils.ThemeHelper

/**
 * Created by huanghaijie on 2018/5/15.
 */
abstract class BaseActivity: AppCompatActivity() {

    protected var activity: Activity? = null

    protected abstract fun getLayoutId(): Int

    protected abstract fun initData()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this
        if (getLayoutId() != 0) setContentView(getLayoutId())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.attributes.flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        }

        StatusBarUtil.MIUISetStatusBarLightMode(this.getWindow(), true)
        StatusBarUtil.FlymeSetStatusBarLightMode(this.getWindow(), true)

        StatusBarUtil.setColor(this, resources.getColor(ThemeHelper.getCurrentColor(this)), 0)

        initViews(savedInstanceState)
        initData()
    }


    protected abstract fun initViews(savedInstanceState: Bundle?)
}