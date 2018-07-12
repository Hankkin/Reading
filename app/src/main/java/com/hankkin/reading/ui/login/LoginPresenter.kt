package com.hankkin.reading.ui.login

import com.hankkin.reading.http.HttpClient
import com.hankkin.reading.http.api.LoginApi
import com.hankkin.reading.http.api.UserApi
import com.hankkin.reading.mvp.presenter.RxLifePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by huanghaijie on 2018/5/21.
 */
class LoginPresenter: RxLifePresenter<LoginContract.IView>(), LoginContract.IPresenter {

    override fun loginHttp(map: HashMap<String, Any>) {
        getMvpView().showLoading()
        HttpClient.getwanAndroidRetrofit().create(LoginApi::class.java)
                .login(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeEx({
                    getMvpView().loginResult(it)
                    getMvpView().hideLoading()
                }, {
                    getMvpView().hideLoading()
                })

    }

}