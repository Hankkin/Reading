package com.hankkin.reading.ui.home.cate

import com.hankkin.reading.domain.CateBean
import com.hankkin.reading.mvp.contract.IBaseViewContract
import com.hankkin.reading.mvp.contract.IPresenterContract

/**
 * Created by huanghaijie on 2018/7/8.
 */
interface CateContact{
    interface IView : IBaseViewContract{
        fun setCates(banner: MutableList<CateBean>)
    }

    interface IPresenter : IPresenterContract{
        fun getCatesHttp()
    }
}