package com.hankkin.reading.ui.todo

import com.hankkin.library.mvp.presenter.RxLifePresenter
import com.hankkin.reading.http.HttpClientUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author Hankkin
 * @date 2018/8/26
 */
class ToDoPresenter : RxLifePresenter<ToDoContract.IView>(),ToDoContract.IPresenter{

    override fun getListDone(cate: Int) {
        HttpClientUtils.Builder.getCommonHttp()
                .getTodo(cate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeNx ({
                    getMvpView().setListDone(it.data)
                },{
                    getMvpView().setFail()
                }).bindRxLifeEx(RxLife.ON_DESTROY)
    }

}