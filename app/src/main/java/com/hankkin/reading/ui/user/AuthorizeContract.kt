package com.hankkin.reading.ui.user

import com.hankkin.reading.domain.UserBean
import com.hankkin.library.mvp.contract.IBaseLoading
import com.hankkin.library.mvp.contract.IPresenterContract

/**
 * Created by huanghaijie on 2018/6/8.
 */
interface AuthorizeContract{

    interface IView : IBaseLoading {
        fun saveUserInfo(userBean: UserBean)
    }

    interface IPresenter : IPresenterContract {
        fun getToken(code: String)
    }

}