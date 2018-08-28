package com.hankkin.reading.base

/**
 * Created by huanghaijie on 2018/6/8.
 */
import android.os.Bundle
import com.hankkin.library.mvp.contract.IPresenterContract
import com.hankkin.library.mvp.view.MvpActivity
import com.hankkin.library.utils.RxBusTools
import com.hankkin.reading.R
import com.hankkin.reading.event.EventMap
import com.hankkin.reading.utils.LoadingUtils
import com.hankkin.reading.utils.MyStatusBarUtil
import com.hankkin.reading.utils.ThemeHelper
import com.jaeger.library.StatusBarUtil
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer

abstract class BaseMvpActivity<out P : IPresenterContract> : MvpActivity<P>() {


    var disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())


        init(savedInstanceState)
        initView()
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

    abstract fun getLayoutId(): Int

    open fun init(savedInstanceState: Bundle?) {}

    open fun initView() {}

    open fun initData() {}


    /**
     * 设置状态栏颜色
     *
     * @param color
     */
    protected fun setStatusBarColor() {
        MyStatusBarUtil.setColorForSwipeBack(this, resources.getColor(ThemeHelper.getCurrentColor(this)), 0)
    }

    /**
     * 设置白底黑字
     */
    protected fun setMiuiStatusBar() {
        MyStatusBarUtil.setColorForSwipeBack(this,resources.getColor(R.color.white),0)
        StatusBarUtil.setLightMode(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
        LoadingUtils.onDestory()
    }


}