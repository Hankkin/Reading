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

    override fun insertDao(hotBean: HotBean) {
        searchModel.hotBeanDao.insert(hotBean)
        getMvpView().insertDao(hotBean.id)
    }

    override fun queryDao(id: Long) {
        val hotBean = searchModel.hotBeanDao.queryBuilder().where(HotBeanDao.Properties.Id.eq(id)).list()
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