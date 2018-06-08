package com.hankkin.reading.ui.login

import com.hankkin.reading.domain.CaptchaBean
import com.hankkin.reading.domain.UserBean
import com.hankkin.reading.mvp.contract.IBaseLoadingContract
import com.hankkin.reading.mvp.contract.IPresenterContract

/**
 * Created by huanghaijie on 2018/5/21.
 */
interface LoginContract {

    interface IView : IBaseLoadingContract {
        fun getCapcha(captchaBean: CaptchaBean)
        fun loginResult(userBean: UserBean)
    }

    interface IPresenter : IPresenterContract {
        fun getCapchaHttp()
        fun loginHttp(map: HashMap<String,Any>)
    }
}