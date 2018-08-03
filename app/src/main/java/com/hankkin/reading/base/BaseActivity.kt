package com.hankkin.reading.base

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.hankkin.library.utils.StatusBarUtil
import com.hankkin.reading.event.EventMap
import com.hankkin.reading.utils.LoadingUtils
import com.hankkin.reading.utils.RxBusTools
import com.hankkin.reading.utils.ThemeHelper
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer

/**
 * Created by huanghaijie on 2018/5/15.
 */
abstract class BaseActivity : AppCompatActivity() {

    var disposables = CompositeDisposable()

    protected var activity: Activity? = null

    protected abstract fun getLayoutId(): Int

    protected abstract fun initData()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this
        if (getLayoutId() != 0) setContentView(getLayoutId())
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        StatusBarUtil.MIUISetStatusBarLightMode(this.getWindow(), true)
        StatusBarUtil.FlymeSetStatusBarLightMode(this.getWindow(), true)

        StatusBarUtil.setColor(this, resources.getColor(ThemeHelper.getCurrentColor(this)), 0)

        initViews(savedInstanceState)
        registerEvent()
        initData()
    }

    open fun isHasBus(): Boolean {
        return false
    }

    protected fun registerEvent() {
        if (isHasBus()) {
            val disposable = RxBusTools.getDefault().register(EventMap.BaseEvent::class.java, Consumer { onEvent(it) })
            disposables.add(disposable)
        }
    }

    open fun onEvent(event: EventMap.BaseEvent) {
    }

    protected abstract fun initViews(savedInstanceState: Bundle?)

    override fun onDestroy() {
        super.onDestroy()
        LoadingUtils.onDestory()
        disposables.clear()
    }
}