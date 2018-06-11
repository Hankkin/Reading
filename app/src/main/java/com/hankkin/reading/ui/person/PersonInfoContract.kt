package com.hankkin.reading.ui.person

import com.hankkin.reading.domain.UserInfoBean
import com.hankkin.reading.mvp.contract.IPresenterContract
import com.hankkin.reading.mvp.contract.IRefreshContract

/**
 * Created by huanghaijie on 2018/5/16.
 */
interface PersonInfoContract{

    interface IView : IRefreshContract{
        fun setInfo(userInfoBean: UserInfoBean)
    }

    interface IPresenter : IPresenterContract{
        fun getUserInfo(access_token: String)
    }

}