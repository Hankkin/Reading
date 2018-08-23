package com.hankkin.reading.ui.home.search

import com.hankkin.reading.domain.HotBean
import com.hankkin.reading.http.HttpClientUtils
import com.hankkin.reading.dao.DaoFactory
import com.hankkin.library.mvp.presenter.RxLifePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SearchPresenter : RxLifePresenter<SearchContract.IView>(), SearchContract.IPresenter {

    override fun delete(id: Long) {
        DaoFactory.getProtocol(SearchDaoContract::class.java).delete(id)
        getMvpView().deleteResult()
    }

    override fun insertDao(hotBean: HotBean) {
        DaoFactory.getProtocol(SearchDaoContract::class.java).insertHot(hotBean)
        getMvpView().insertDao(hotBean.id)
    }

    override fun queryDao() {
        val hotBean = DaoFactory.getProtocol(SearchDaoContract::class.java).query()
        getMvpView().queryResult(hotBean)
    }


    override fun getHotHttp() {
        getMvpView().showLoading()
        HttpClientUtils.Builder.getCommonHttp()
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