package com.hankkin.reading.ui.home.hot

import com.hankkin.reading.http.HttpClient
import com.hankkin.reading.http.api.WanAndroidApi
import com.hankkin.reading.mvp.presenter.RxLifePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by huanghaijie on 2018/7/8.
 */
class HotPresenter : RxLifePresenter<HotContact.IView>(), HotContact.IPresenter {
    override fun getHot() {
        HttpClient.getwanAndroidRetrofit().create(WanAndroidApi::class.java)
                .getHot()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeNx({
                    getMvpView().setHot(it.data)
                }).bindRxLifeEx(RxLife.ON_DESTROY)
    }

}