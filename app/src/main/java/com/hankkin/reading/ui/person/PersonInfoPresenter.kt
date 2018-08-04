package com.hankkin.reading.ui.person

import com.hankkin.reading.http.HttpClient
import com.hankkin.reading.mvp.presenter.RxLifePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PersonInfoPresenter : RxLifePresenter<PersonInfoContract.IView>(),PersonInfoContract.IPresenter{
    override fun getUserInfo(access_token: String) {
        getMvpView().refresh()
        var map = HashMap<String,Any>()
        map.put("access_token",access_token)
        HttpClient.Builder.getOsChinaHttp()
                .getUserInfo(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeNx ({
                    getMvpView().setInfo(it)
                    getMvpView().refreshStop()
                },{
                    getMvpView().refreshStop()
                }).bindRxLifeEx(RxLife.ON_DESTROY)
    }

}