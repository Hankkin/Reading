package com.hankkin.reading.ui.person

import com.hankkin.reading.domain.ArticleBean
import com.hankkin.library.mvp.contract.IBaseViewContract
import com.hankkin.library.mvp.contract.IPresenterContract

interface MyCollectContract{

    interface IView : IBaseViewContract {
        fun getCollectData(articleBean: ArticleBean)
        fun setFail()
        fun collectResult(id: Int)
        fun cancelCollectResult(id: Int)
    }

    interface IPresenter : IPresenterContract {
        fun getCollectHttp(page: Int)
        fun collectHttp(id: Int)
        fun cancelCollectHttp(id: Int)
    }
}