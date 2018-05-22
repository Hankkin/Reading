package com.hankkin.reading.ui.login

import com.hankkin.reading.http.HttpClient
import com.hankkin.reading.http.api.LoginApi
import com.hankkin.reading.mvp.presenter.BaseRxLifePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by huanghaijie on 2018/5/21.
 */
class LoginPresenter(mvpView: LoginContract.IView) : BaseRxLifePresenter<LoginContract.IView>(mvpView),LoginContract.IPresenter{

    override fun getCapchaHttp() {
        getMvpView().showLoading()
        HttpClient.getnorRetrofit().create(LoginApi::class.java)
                .getCaptcha()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeEx ({
                    getMvpView().getCapcha(it)
                    getMvpView().hideLoading()
                },{
                    getMvpView().hideLoading()
                })
    }

}