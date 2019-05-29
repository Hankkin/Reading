package com.hankkin.library.mvp.presenter

import com.hankkin.library.mvp.contract.IBaseViewContract


/**
 * Created by huanghaijie on 2018/5/16.
 */
interface IBasePresenter<out V : IBaseViewContract> {

    fun getMvpView(): V
}