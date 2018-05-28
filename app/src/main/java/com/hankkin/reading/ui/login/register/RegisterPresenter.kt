package com.hankkin.reading.ui.login.register

import com.hankkin.reading.http.HttpClient
import com.hankkin.reading.http.api.LoginApi
import com.hankkin.reading.http.api.UserApi
import com.hankkin.reading.mvp.presenter.BaseRxLifePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by huanghaijie on 2018/5/21.
 */
class RegisterPresenter(mvpView: RegisterContract.IView) : BaseRxLifePresenter<RegisterContract.IView>(mvpView), RegisterContract.IPresenter {

    companion object {
        const val EMAIL = "email"
        const val NAME = "username"
        const val PASSWORD = "password1"
        const val RPASSWORD = "password2"
        const val CAPTCHA_KEY = "captcha_0"
        const val CAPTCHA_INPUT = "captcha_1"

        const val EMAIL_MSG = "请输入正确的邮箱地址"
        const val NAME_MSG = "请输入用户名至少2个字符"
        const val PASSWORD_MSG = "请输入密码至少8个字符"
        const val RPASSWORD_MSG = "请输入重复密码至少8个字符"
        const val CODE_MSG = "请输入验证码4个字符"
        const val PASSWORD_AND_MSG = "两次密码不一致"

        const val EMAIL_REG = "^([a-z0-9A-Z]+[-|\\.]?+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}\$)"
    }


    override fun verifiyFormat(paramsMap: HashMap<String, String>) {
        if (paramsMap[NAME].isNullOrEmpty()) {
            getMvpView().verifiyFormatResult(NAME_MSG)
            return
        }
        if (paramsMap[EMAIL].isNullOrBlank()) {
            getMvpView().verifiyFormatResult(EMAIL_MSG)
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
        if (paramsMap[CAPTCHA_INPUT].isNullOrEmpty()) {
            getMvpView().verifiyFormatResult(CODE_MSG)
            return
        }
        if (!paramsMap[PASSWORD].equals(paramsMap[RPASSWORD])) {
            getMvpView().verifiyFormatResult(PASSWORD_AND_MSG)
            return
        }
//        if (!Pattern.matches(EMAIL_REG, paramsMap[EMAIL])){
//            getMvpView().verifiyFormatResult(EMAIL_MSG)
//            return
//        }
        regHttp(paramsMap)
    }

    override fun regHttp(map: HashMap<String, String>) {
        getMvpView().showLoading()
        HttpClient.getnorRetrofit().create(LoginApi::class.java)
                .getCsrfToken()
                .flatMap {
                    map.put("csrfmiddlewaretoken", it.data.csrfmiddlewaretoken)
                    HttpClient.getnorRetrofit().create(LoginApi::class.java).signUp(map)
                }
                .flatMap {
                    HttpClient.getnorRetrofit().create(UserApi::class.java).getUserProfile()
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeEx({
//                    getMvpView().regResult(it)
                    getMvpView().hideLoading()
                }, {
                    getMvpView().hideLoading()
                })
    }

    override fun getCapchaHttp() {
        getMvpView().showLoading()
        HttpClient.getnorRetrofit().create(LoginApi::class.java)
                .getCaptcha()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeEx({
                    getMvpView().getCapcha(it)
                    getMvpView().hideLoading()
                }, {
                    getMvpView().hideLoading()
                })
    }

}