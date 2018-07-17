package com.hankkin.reading.ui.user.collect

import com.hankkin.reading.domain.ArticleBean
import com.hankkin.reading.mvp.contract.IBaseViewContract
import com.hankkin.reading.mvp.contract.IPresenterContract

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