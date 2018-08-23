package com.hankkin.reading.ui.login

import com.hankkin.reading.domain.UserBean
import com.hankkin.library.mvp.contract.IBaseLoading
import com.hankkin.library.mvp.contract.IPresenterContract

/**
 * Created by huanghaijie on 2018/5/21.
 */
interface LoginContract {

    interface IView : IBaseLoading {
        fun loginResult(userBean: UserBean)
    }

    interface IPresenter : IPresenterContract {
        fun loginHttp(map: HashMap<String,Any>)
    }
}