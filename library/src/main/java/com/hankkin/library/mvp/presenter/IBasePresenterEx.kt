package com.hankkin.library.mvp.presenter

import android.app.Activity
import android.content.Context
import android.support.v4.app.Fragment
import com.hankkin.library.mvp.presenter.IBasePresenter
import java.lang.IllegalStateException

/**
 * Created by huanghaijie on 2018/5/16.
 */
fun IBasePresenter<*>.getContext(): Context = when {
    getMvpView() is Activity -> getMvpView() as Activity
    getMvpView() is Fragment -> (getMvpView() as Fragment).activity!!
    else -> throw IllegalStateException("the presenter not found context")
}