package com.hankkin.easyword.mvp.view

import android.app.Activity
import android.os.Bundle
import com.hankkin.easyword.mvp.contract.IBaseViewContract
import com.hankkin.easyword.mvp.contract.IBasePresenterContract

/**
 * Created by huanghaijie on 2018/5/16.
 */
 abstract class BaseMvpActivity<out T : IBasePresenterContract> : Activity() , IBaseViewContract {

    private val mPresenter: T by lazy { createPresenter() }

    fun getPresenter(): T = mPresenter

    protected abstract fun createPresenter(): T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter.onCreate()
    }

    override fun onStart() {
        super.onStart()
        mPresenter.onStart()
    }

    override fun onResume() {
        super.onResume()
        mPresenter.onResume()
    }

    override fun onPause() {
        super.onPause()
        mPresenter.onPause()
    }

    override fun onStop() {
        super.onStop()
        mPresenter.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.onDestroy()
    }

}