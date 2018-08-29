package com.hankkin.reading.ui.todo

import com.hankkin.library.mvp.presenter.RxLifePresenter
import com.hankkin.reading.http.HttpClientUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author Hankkin
 * @date 2018/8/28
 */
class AddToDoPresenter : RxLifePresenter<AddToDoContract.IView>(), AddToDoContract.IPresenter {
    override fun addTodo(map: HashMap<String, Any>) {
        HttpClientUtils.Builder.getCommonHttp().addTodo(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeNx({
                    getMvpView().addTodoSuccess()
                }, {
                    getMvpView().setFail()
                }).bindRxLifeEx(RxLife.ON_DESTROY)
    }

    override fun updateTodo(id: Int, map: HashMap<String, Any>) {
        HttpClientUtils.Builder.getCommonHttp().updateTodo(id, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeNx({
                    getMvpView().updateToDoSuccess()
                }, {
                    getMvpView().setFail()
                }).bindRxLifeEx(RxLife.ON_DESTROY)
    }

}