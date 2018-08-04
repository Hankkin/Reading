package com.hankkin.reading.ui.home.project.projectlist

import com.hankkin.reading.http.HttpClient
import com.hankkin.reading.mvp.presenter.RxLifePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by huanghaijie on 2018/7/8.
 */
class ProjectListPresenter : RxLifePresenter<ProjectListContact.IView>(), ProjectListContact.IPresenter{

    override fun getCateList(page: Int,cid: Int) {
        HttpClient.Builder.getCommonHttp()
                .getArticleCid(page,cid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeNx ({
                    getMvpView().setCateList(it.data)
                }).bindRxLifeEx(RxLife.ON_DESTROY)
    }


}