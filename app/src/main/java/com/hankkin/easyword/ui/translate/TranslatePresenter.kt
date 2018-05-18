package com.hankkin.easyword.ui.translate

import com.hankkin.easyword.http.inter.ISearchWordHttp
import com.hankkin.easyword.http.inter.IWeatherHttp
import com.hankkin.easyword.http.protocol.HttpFactory
import com.hankkin.easyword.mvp.presenter.BaseRxLifePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by huanghaijie on 2018/5/16.
 */
class TranslatePresenter(mvpView : TranslateContract.IView) : BaseRxLifePresenter<TranslateContract.IView>(mvpView), TranslateContract.IPresenter {

    override fun downRank(path: String, url: String) {
        HttpFactory.getProtocol(ISearchWordHttp::class.java)
                .downRank(path,url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeEx ({ getMvpView().downRankSuc(it) })
    }



    override fun getWrod(word: String) {
        HttpFactory.getProtocol(ISearchWordHttp::class.java)
                .searchWord(word)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeEx ({
                    getMvpView().searchWordResult(it)
                }, {
                    getMvpView().searchFail()
                })
    }

    override fun getWeather(city: String) {
        HttpFactory.getProtocol(IWeatherHttp::class.java)
                .getWeather(city)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeEx ({
                    getMvpView().setWeather(it)
                },{
                    getMvpView().setWeatherError()
        })

    }

}