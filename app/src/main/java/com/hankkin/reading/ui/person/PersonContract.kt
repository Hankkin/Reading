package com.hankkin.reading.ui.person

import com.hankkin.reading.domain.NoticeBean
import com.hankkin.reading.mvp.contract.IPresenterContract
import com.hankkin.reading.mvp.contract.IRefreshContract

/**
 * Created by huanghaijie on 2018/5/16.
 */
interface PersonContract {

    interface IView : IRefreshContract {
        fun setNotice(noticeBean: NoticeBean)
    }

    interface IPresenter : IPresenterContract {
        fun getUserNotice(access_token: String)
    }

}