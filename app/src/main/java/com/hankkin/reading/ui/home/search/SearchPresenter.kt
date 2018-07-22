package com.hankkin.reading.ui.home.search

import com.hankkin.reading.http.HttpClient
import com.hankkin.reading.http.api.WanAndroidApi
import com.hankkin.reading.mvp.presenter.RxLifePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SearchPresenter : RxLifePresenter<SearchContract.IView>(), SearchContract.IPresenter {
    override fun getHotHttp() {
        getMvpView().showLoading()
        HttpClient.getwanAndroidRetrofit().create(WanAndroidApi::class.java)
                .getHot()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeNx({
                    getMvpView().getHotResult(it.data)
                    getMvpView().hideLoading()
                },{
                    getMvpView().hideLoading()
                }).bindRxLifeEx(RxLife.ON_DESTROY)
    }


}