package com.hankkin.reading.ui.home.cate.catelist

import com.hankkin.reading.http.HttpClient
import com.hankkin.reading.http.api.HomeApi
import com.hankkin.reading.mvp.presenter.RxLifePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by huanghaijie on 2018/7/8.
 */
class CateListPresenter : RxLifePresenter<CateListContact.IView>(), CateListContact.IPresenter{
    override fun getBannerHttp() {
        HttpClient.getwanAndroidRetrofit().create(HomeApi::class.java)
                .getHomeBanner()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeNx ({
                    getMvpView().setBanner(it.data)
                }).bindRxLifeEx(RxLife.ON_DESTROY)
    }

    override fun getCateList(page: Int,cid: Int) {
        HttpClient.getwanAndroidRetrofit().create(HomeApi::class.java)
                .getArticleCid(page,cid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeNx ({
                    getMvpView().setCateList(it.data)
                }).bindRxLifeEx(RxLife.ON_DESTROY)
    }


}