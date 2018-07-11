package com.hankkin.reading.ui.home.cate.catelist

import com.hankkin.reading.domain.ArticleBean
import com.hankkin.reading.domain.BannerBean
import com.hankkin.reading.domain.CateBean
import com.hankkin.reading.mvp.contract.IBaseViewContract
import com.hankkin.reading.mvp.contract.IPresenterContract

/**
 * Created by huanghaijie on 2018/7/8.
 */
interface CateListContact {
    interface IView : IBaseViewContract {
        fun setBanner(banner: MutableList<BannerBean>)
        fun setCateList(data: ArticleBean)
    }

    interface IPresenter : IPresenterContract {
        fun getCateList(page: Int,cid: Int)
        fun getBannerHttp()
    }
}