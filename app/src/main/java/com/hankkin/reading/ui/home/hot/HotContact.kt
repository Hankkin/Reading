package com.hankkin.reading.ui.home.hot

import com.hankkin.reading.domain.CateBean
import com.hankkin.reading.domain.HotBean
import com.hankkin.reading.mvp.contract.IBaseViewContract
import com.hankkin.reading.mvp.contract.IPresenterContract

/**
 * Created by huanghaijie on 2018/7/8.
 */
interface HotContact{
    interface IView : IBaseViewContract{
        fun setHot(data: MutableList<HotBean>)
    }

    interface IPresenter : IPresenterContract{
        fun getHot()
    }
}