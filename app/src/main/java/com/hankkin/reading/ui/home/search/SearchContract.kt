package com.hankkin.reading.ui.home.search

import com.hankkin.reading.domain.HotBean
import com.hankkin.library.mvp.contract.IBaseLoading
import com.hankkin.library.mvp.contract.IPresenterContract

interface SearchContract {

    interface IView : IBaseLoading {
        fun getHotResult(data: MutableList<HotBean>)
        fun insertDao(id: Long)
        fun queryResult(hotBean: MutableList<HotBean>)
        fun deleteResult()
    }

    interface IPresenter : IPresenterContract {
        fun getHotHttp()
        fun insertDao(hotBean: HotBean)
        fun queryDao()
        fun delete(id: Long)
    }

}