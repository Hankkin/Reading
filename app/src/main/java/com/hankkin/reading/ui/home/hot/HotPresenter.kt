package com.hankkin.reading.ui.home.hot

import com.hankkin.reading.http.HttpClientUtils
import com.hankkin.library.mvp.presenter.RxLifePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by huanghaijie on 2018/7/8.
 */
class HotPresenter : RxLifePresenter<HotContact.IView>(), HotContact.IPresenter {
    override fun getHot() {
        HttpClientUtils.Builder.getCommonHttp()
                .getHot()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeNx({
                    getMvpView().setHot(it.data)
                }, {
                    getMvpView().setFail()
                }).bindRxLifeEx(RxLife.ON_DESTROY)
    }

}