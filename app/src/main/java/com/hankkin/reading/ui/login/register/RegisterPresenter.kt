package com.hankkin.reading.ui.login.register

import com.hankkin.reading.http.HttpClientUtils
import com.hankkin.library.mvp.presenter.RxLifePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by huanghaijie on 2018/5/21.
 */
class RegisterPresenter : RxLifePresenter<RegisterContract.IView>(), RegisterContract.IPresenter {

    companion object {
        const val NAME = "username"
        const val PASSWORD = "password"
        const val RPASSWORD = "repassword"

        const val NAME_MSG = "请输入用户名至少2个字符"
        const val PASSWORD_MSG = "请输入密码"
        const val RPASSWORD_MSG = "请输入重复密码"
        const val PASSWORD_AND_MSG = "两次密码不一致"

        const val EMAIL_REG = "^([a-z0-9A-Z]+[-|\\.]?+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}\$)"
    }


    override fun verifiyFormat(paramsMap: HashMap<String, String>) {
        if (paramsMap[NAME].isNullOrBlank()) {
            getMvpView().verifiyFormatResult(NAME_MSG)
            return
        }
        if (paramsMap[PASSWORD].isNullOrEmpty()) {
            getMvpView().verifiyFormatResult(PASSWORD_MSG)
            return
        }
        if (paramsMap[RPASSWORD].isNullOrEmpty()) {
            getMvpView().verifiyFormatResult(RPASSWORD_MSG)
            return
        }
        if (!paramsMap[PASSWORD].equals(paramsMap[RPASSWORD])) {
            getMvpView().verifiyFormatResult(PASSWORD_AND_MSG)
            return
        }
        regHttp(paramsMap)
    }

    override fun regHttp(map: HashMap<String, String>) {
        getMvpView().showLoading()
        HttpClientUtils.Builder.getCommonHttp()
                .signUp(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeEx({
                    getMvpView().regResult(it)
                    getMvpView().hideLoading()
                }, {
                    getMvpView().hideLoading()
                })
    }


}