package com.hankkin.reading.base

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.hankkin.library.base.BaseSwipeBackActivity
import com.hankkin.library.utils.AppUtils
import com.hankkin.library.utils.RxBusTools
import com.hankkin.library.widget.view.PageLayout
import com.hankkin.reading.R
import com.hankkin.reading.event.EventMap
import com.hankkin.reading.utils.LoadingUtils
import com.hankkin.reading.utils.MyStatusBarUtil
import com.hankkin.reading.utils.ThemeHelper
import com.jaeger.library.StatusBarUtil
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer

/**
 * Created by huanghaijie on 2018/5/15.
 */
abstract class BaseActivity : BaseSwipeBackActivity() {

    protected var disposables = CompositeDisposable()

    protected var activity: Activity? = null

    protected lateinit var mPageLayout: PageLayout

    protected abstract fun getLayoutId(): Int

    protected abstract fun initViews(savedInstanceState: Bundle?)

    protected abstract fun initData()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())

        activity = this
        initViews(savedInstanceState)
        registerEvent()
        initData()
    }

    protected fun initPageLayout(targetView: Any){
        mPageLayout = PageLayout.Builder(this)
                .initPage(targetView)
                .setDefaultEmptyText(resources.getString(R.string.pagelayout_empty))
                .setDefaultErrorText(resources.getString(R.string.pagelayout_error))
                .setDefaultLoadingBlinkText(AppUtils.getAppName(this)!!)
                .create()
    }

    /**
     * 设置白底黑字
     */
    protected fun setMiuiStatusBar() {
        MyStatusBarUtil.setColorForSwipeBack(this,resources.getColor(R.color.white),0)
        StatusBarUtil.setLightMode(this)
    }

    /**
     * 是否注册rxbus
     */
    open fun isHasBus(): Boolean {
        return false
    }

    protected fun registerEvent() {
        if (isHasBus()) {
            val disposable = RxBusTools.getDefault().register(EventMap.BaseEvent::class.java, Consumer { onEvent(it) })
            disposables.add(disposable)
        }
    }

    /**
     * 设置状态栏颜色
     *
     * @param color
     */
    protected fun setStatusBarColor() {
        MyStatusBarUtil.setColorForSwipeBack(this, resources.getColor(ThemeHelper.getCurrentColor(this)), 0)
    }

    open fun onEvent(event: EventMap.BaseEvent) {
    }

    override fun onDestroy() {
        super.onDestroy()
        LoadingUtils.onDestory()
        disposables.clear()
    }
}