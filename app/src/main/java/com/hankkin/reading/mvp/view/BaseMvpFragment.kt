package com.hankkin.reading.mvp.view

import android.os.Bundle
import android.support.v4.app.Fragment
import com.hankkin.reading.mvp.contract.IBasePresenterContract

/**
 * Created by huanghaijie on 2018/5/16.
 */
abstract class BaseMvpFragment<out T : IBasePresenterContract> : Fragment() {

    private val mPresenter: T by lazy { createmPresenter() }

    fun getmPresenter(): T = mPresenter

    protected abstract fun createmPresenter(): T

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