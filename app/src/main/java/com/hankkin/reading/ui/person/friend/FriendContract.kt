package com.hankkin.reading.ui.person.friend

import com.hankkin.reading.domain.FriendBean
import com.hankkin.library.mvp.contract.IPresenterContract
import com.hankkin.library.mvp.contract.IRefresh

interface FriendContract{

    interface IView : IRefresh {
        fun setFriendList(data: FriendBean)
    }

    interface IPresenter : IPresenterContract {
        fun getFriendList(map: HashMap<String,Any>)
    }

}