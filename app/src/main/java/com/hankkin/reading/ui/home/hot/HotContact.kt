package com.hankkin.reading.ui.home.hot

import com.hankkin.reading.domain.HotBean
import com.hankkin.library.mvp.contract.IBaseViewContract
import com.hankkin.library.mvp.contract.IPresenterContract

/**
 * Created by huanghaijie on 2018/7/8.
 */
interface HotContact{
    interface IView : IBaseViewContract {
        fun setHot(data: MutableList<HotBean>)
        fun setFail()
    }

    interface IPresenter : IPresenterContract {
        fun getHot()
    }
}