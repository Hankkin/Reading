package com.hankkin.reading.ui.home.project.projectlist

import com.hankkin.reading.domain.ArticleBean
import com.hankkin.reading.domain.BannerBean
import com.hankkin.reading.mvp.contract.IBaseViewContract
import com.hankkin.reading.mvp.contract.IPresenterContract

/**
 * Created by huanghaijie on 2018/7/8.
 */
interface ProjectListContact {
    interface IView : IBaseViewContract {
        fun setCateList(data: ArticleBean)
    }

    interface IPresenter : IPresenterContract {
        fun getCateList(page: Int,cid: Int)
    }
}