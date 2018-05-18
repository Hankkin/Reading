package com.hankkin.reading.ui.main

import com.hankkin.reading.mvp.contract.IBasePresenterContract
import com.hankkin.reading.mvp.contract.IBaseViewContract

/**
 * Created by huanghaijie on 2018/5/16.
 */
interface MainContract{

    interface IView : IBaseViewContract

    interface IPresenter : IBasePresenterContract

}