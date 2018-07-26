package com.hankkin.reading.ui.home.search

import com.hankkin.reading.domain.HotBean
import com.hankkin.reading.greendao.HotBeanDao
import com.hankkin.reading.http.HttpClient
import com.hankkin.reading.http.api.WanAndroidApi
import com.hankkin.reading.mvp.presenter.RxLifePresenter
import com.hankkin.reading.mvp.presenter.getContext
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SearchPresenter : RxLifePresenter<SearchContract.IView>(), SearchContract.IPresenter {

    private val searchModel by lazy { SearchDao(getContext()) }


    override fun delete(id: Long) {
        searchModel.hotBeanDao.deleteByKey(id)
        getMvpView().deleteResult()
    }

    override fun insertDao(hotBean: HotBean) {
        searchModel.hotBeanDao.insertOrReplace(hotBean)
        getMvpView().insertDao(hotBean.id)
    }

    override fun queryDao() {
        val hotBean = searchModel.hotBeanDao.queryBuilder().list()
        getMvpView().queryResult(hotBean)
    }


    override fun getHotHttp() {
        getMvpView().showLoading()
        HttpClient.getwanAndroidRetrofit().create(WanAndroidApi::class.java)
                .getHot()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeNx({
                    getMvpView().getHotResult(it.data)
                    getMvpView().hideLoading()
                },{
                    getMvpView().hideLoading()
                }).bindRxLifeEx(RxLife.ON_DESTROY)
    }




}