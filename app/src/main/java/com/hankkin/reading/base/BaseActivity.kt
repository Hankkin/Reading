package com.hankkin.reading.base

import android.app.Activity
import android.os.Bundle
import com.hankkin.reading.mvp.contract.IBasePresenterContract
import com.hankkin.reading.mvp.view.BaseMvpFragmentActivity

/**
 * Created by huanghaijie on 2018/5/15.
 */
abstract class BaseActivity<out T : IBasePresenterContract>  : BaseMvpFragmentActivity<T>() {

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