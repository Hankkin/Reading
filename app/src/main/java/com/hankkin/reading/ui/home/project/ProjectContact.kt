package com.hankkin.reading.ui.home.project

import com.hankkin.reading.domain.CateBean
import com.hankkin.library.mvp.contract.IBaseViewContract
import com.hankkin.library.mvp.contract.IPresenterContract

/**
 * Created by huanghaijie on 2018/7/8.
 */
interface ProjectContact{
    interface IView : IBaseViewContract {
        fun setCates(data: MutableList<CateBean>)
    }

    interface IPresenter : IPresenterContract {
        fun getCatesHttp()
    }
}