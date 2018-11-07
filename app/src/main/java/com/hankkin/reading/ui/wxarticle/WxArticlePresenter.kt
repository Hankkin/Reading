package com.hankkin.reading.ui.wxarticle

import com.hankkin.reading.http.HttpClientUtils
import com.hankkin.library.mvp.presenter.RxLifePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by huanghaijie on 2018/7/8.
 */
class WxArticlePresenter : RxLifePresenter<WxArticleContact.IView>(), WxArticleContact.IPresenter {
    override fun getWxTabs() {
        HttpClientUtils.Builder.getCommonHttp()
                .getWxChapters()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeNx({
                    getMvpView().setTabs(it.data)
                }).bindRxLifeEx(RxLife.ON_DESTROY)
    }

}