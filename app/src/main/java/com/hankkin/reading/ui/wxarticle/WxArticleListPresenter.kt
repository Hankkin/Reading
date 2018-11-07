package com.hankkin.reading.ui.wxarticle

import com.hankkin.reading.http.HttpClientUtils
import com.hankkin.library.mvp.presenter.RxLifePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by huanghaijie on 2018/7/8.
 */
class WxArticleListPresenter : RxLifePresenter<WxArticleListContact.IView>(), WxArticleListContact.IPresenter {

    override fun getWxArticleList(id: Int, page: Int) {
        HttpClientUtils.Builder.getCommonHttp()
                .getWxList(id,page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeNx({
                    getMvpView().setWxArticles(it.data)
                }).bindRxLifeEx(RxLife.ON_DESTROY)
    }


}