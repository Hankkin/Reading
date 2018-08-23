package com.hankkin.reading.ui.home.articledetail

import com.hankkin.library.mvp.contract.IPresenterContract
import com.hankkin.library.mvp.contract.IBaseViewContract

/**
 * Created by huanghaijie on 2018/5/16.
 */
interface ArticleDetailContract {

    interface IView : IBaseViewContract {
        fun collectResult()
    }

    interface IPresenter : IPresenterContract {
        fun collectHttp()
    }
}