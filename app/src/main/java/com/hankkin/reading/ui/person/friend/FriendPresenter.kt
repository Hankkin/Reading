package com.hankkin.reading.ui.person.friend

import com.hankkin.reading.http.HttpClientUtils
import com.hankkin.library.mvp.presenter.RxLifePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class FriendPresenter : RxLifePresenter<FriendContract.IView>(),FriendContract.IPresenter{
    override fun getFriendList(map: HashMap<String, Any>) {
        getMvpView().refresh()
        HttpClientUtils.Builder.getOsChinaHttp()
                .getFriendList(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeNx ({
                    getMvpView().setFriendList(it)
                    getMvpView().refreshStop()
                },{
                    getMvpView().refreshStop()
                })
    }

}
