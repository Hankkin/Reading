package com.hankkin.easyword.mvp.presenter

import com.hankkin.easyword.mvp.contract.IBaseViewContract

/**
 * Created by huanghaijie on 2018/5/16.
 */
interface IBasePresenter<out V : IBaseViewContract>{

    fun getMvpView(): V
}