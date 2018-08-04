package com.hankkin.reading.ui.person.friend

import com.hankkin.reading.http.HttpClient
import com.hankkin.reading.mvp.presenter.RxLifePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class FriendPresenter : RxLifePresenter<FriendContract.IView>(),FriendContract.IPresenter{
    override fun getFriendList(map: HashMap<String, Any>) {
        getMvpView().refresh()
        HttpClient.Builder.getOsChinaHttp()
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
