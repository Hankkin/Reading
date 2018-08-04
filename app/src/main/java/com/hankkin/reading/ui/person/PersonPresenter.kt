package com.hankkin.reading.ui.person

import com.hankkin.reading.http.HttpClient
import com.hankkin.reading.mvp.presenter.RxLifePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by huanghaijie on 2018/5/16.
 */
class PersonPresenter : RxLifePresenter<PersonContract.IView>(), PersonContract.IPresenter {
    override fun getUserNotice(access_token: String) {
        getMvpView().refresh()
        HttpClient.Builder.getOsChinaHttp()
                .getUserNotice(access_token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeNx ({
                    getMvpView().setNotice(it)
                    getMvpView().refreshStop()
                },{
                    getMvpView().refreshStop()
                })
    }

}