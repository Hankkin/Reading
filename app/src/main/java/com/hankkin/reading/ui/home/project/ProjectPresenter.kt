package com.hankkin.reading.ui.home.project

import com.hankkin.reading.http.HttpClientUtils
import com.hankkin.library.mvp.presenter.RxLifePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by huanghaijie on 2018/7/8.
 */
class ProjectPresenter : RxLifePresenter<ProjectContact.IView>(), ProjectContact.IPresenter {
    override fun getCatesHttp() {
        HttpClientUtils.Builder.getCommonHttp()
                .getProject()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeNx({
                    getMvpView().setCates(it.data)
                }).bindRxLifeEx(RxLife.ON_DESTROY)

    }


}