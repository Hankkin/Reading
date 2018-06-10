package com.hankkin.reading.ui.person

import com.hankkin.reading.mvp.contract.IBaseLoadingContract
import com.hankkin.reading.mvp.contract.IPresenterContract
import com.hankkin.reading.mvp.contract.IBaseViewContract

/**
 * Created by huanghaijie on 2018/5/16.
 */
interface PersonInfoContract{

    interface IView : IBaseLoadingContract

    interface IPresenter : IPresenterContract

}