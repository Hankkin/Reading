package com.hankkin.reading.ui.person

import com.hankkin.reading.domain.UserInfoBean
import com.hankkin.library.mvp.contract.IPresenterContract
import com.hankkin.library.mvp.contract.IRefresh

/**
 * Created by huanghaijie on 2018/5/16.
 */
interface PersonInfoContract{

    interface IView : IRefresh {
        fun setInfo(userInfoBean: UserInfoBean)
    }

    interface IPresenter : IPresenterContract {
        fun getUserInfo(access_token: String)
    }

}