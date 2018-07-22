package com.hankkin.reading.ui.home.search

import com.hankkin.reading.domain.HotBean
import com.hankkin.reading.mvp.contract.IBaseLoading
import com.hankkin.reading.mvp.contract.IBaseViewContract
import com.hankkin.reading.mvp.contract.IPresenterContract

interface SearchContract{

    interface IView : IBaseLoading{
        fun getHotResult(data: MutableList<HotBean>)
    }

    interface IPresenter: IPresenterContract{
        fun getHotHttp()
    }

}