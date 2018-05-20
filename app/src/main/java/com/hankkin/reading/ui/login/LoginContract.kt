package com.hankkin.reading.ui.login

import com.hankkin.reading.mvp.contract.IBasePresenterContract
import com.hankkin.reading.mvp.contract.IBaseViewContract

/**
 * Created by huanghaijie on 2018/5/21.
 */
interface LoginContract{
    interface IView : IBaseViewContract
    interface IPresenter : IBasePresenterContract
}