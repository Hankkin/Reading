package com.hankkin.reading.ui.login.register

import com.hankkin.reading.domain.UserBean
import com.hankkin.library.mvp.contract.IBaseLoading
import com.hankkin.library.mvp.contract.IPresenterContract

/**
 * Created by huanghaijie on 2018/5/21.
 */
interface RegisterContract {

    interface IView : IBaseLoading {
        fun regResult(userBean: UserBean)
        fun verifiyFormatResult(msg: String)
    }

    interface IPresenter : IPresenterContract {
        fun regHttp(map: HashMap<String,String>)
        fun verifiyFormat(paramsMap: HashMap<String,String>)
    }
}