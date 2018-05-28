package com.hankkin.reading.ui.login.register

import com.hankkin.reading.domain.BaseResponse
import com.hankkin.reading.domain.CaptchaBean
import com.hankkin.reading.domain.UserBean
import com.hankkin.reading.mvp.contract.IBaseLoadingContract
import com.hankkin.reading.mvp.contract.IBasePresenterContract

/**
 * Created by huanghaijie on 2018/5/21.
 */
interface RegisterContract {

    interface IView : IBaseLoadingContract {
        fun getCapcha(captchaBean: CaptchaBean)
        fun regResult(userBean: UserBean)
        fun verifiyFormatResult(msg: String)
    }

    interface IPresenter : IBasePresenterContract {
        fun getCapchaHttp()
        fun regHttp(map: HashMap<String,String>)
        fun verifiyFormat(paramsMap: HashMap<String,String>)
    }
}