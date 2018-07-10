package com.hankkin.reading.ui.home.cate

import com.hankkin.reading.http.HttpClient
import com.hankkin.reading.http.api.HomeApi
import com.hankkin.reading.mvp.presenter.RxLifePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by huanghaijie on 2018/7/8.
 */
class CatePresenter : RxLifePresenter<CateContact.IView>(), CateContact.IPresenter {
    override fun getCatesHttp() {
        HttpClient.getwanAndroidRetrofit().create(HomeApi::class.java)
                .getCates()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeNx({
                    getMvpView().setCates(it.data)
                }).bindRxLifeEx(RxLife.ON_DESTROY)

    }


}