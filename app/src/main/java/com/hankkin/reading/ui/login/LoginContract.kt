package com.hankkin.reading.ui.login

import com.hankkin.reading.domain.BaseResponse
import com.hankkin.reading.domain.CaptchaBean
import com.hankkin.reading.domain.CsrfTokenBean
import com.hankkin.reading.domain.UserBean
import com.hankkin.reading.mvp.contract.IBaseLoadingContract
import com.hankkin.reading.mvp.contract.IBasePresenterContract

/**
 * Created by huanghaijie on 2018/5/21.
 */
interface LoginContract {

    interface IView : IBaseLoadingContract {
        fun getCapcha(captchaBean: CaptchaBean)
        fun loginResult(userBean: UserBean)
    }

    interface IPresenter : IBasePresenterContract {
        fun getCapchaHttp()
        fun loginHttp(map: HashMap<String,Any>)
    }
}