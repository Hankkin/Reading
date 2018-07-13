package com.hankkin.reading.ui.home.android

import com.hankkin.reading.http.HttpClient
import com.hankkin.reading.http.api.HomeApi
import com.hankkin.reading.mvp.presenter.RxLifePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by huanghaijie on 2018/7/8.
 */
class AndroidPresenter : RxLifePresenter<AndroidContact.IView>(), AndroidContact.IPresenter {
    override fun collectHttp(id: Int) {
        HttpClient.getwanAndroidRetrofit().create(HomeApi::class.java)
                .collectArticle(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeNx ({
                    getMvpView().collectResult()
                },{
                    getMvpView().setFail()
                }).bindRxLifeEx(RxLife.ON_DESTROY)
    }

    override fun getArticle(page: Int) {
        HttpClient.getwanAndroidRetrofit().create(HomeApi::class.java)
                .getArticle(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeNx ({
                    getMvpView().setArticle(it.data)
                },{
                    getMvpView().setFail()
                }).bindRxLifeEx(RxLife.ON_DESTROY)
    }


}