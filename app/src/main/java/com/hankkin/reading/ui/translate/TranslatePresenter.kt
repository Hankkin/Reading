package com.hankkin.reading.ui.translate

import com.hankkin.reading.common.Constant
import com.hankkin.reading.http.HttpClient
import com.hankkin.reading.http.api.DownApi
import com.hankkin.reading.http.api.TranslateApi
import com.hankkin.reading.mvp.presenter.BaseRxLifePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by huanghaijie on 2018/5/16.
 */
class TranslatePresenter(mvpView: TranslateContract.IView) : BaseRxLifePresenter<TranslateContract.IView>(mvpView), TranslateContract.IPresenter {


    override fun downRank(name: String, url: String, type: String,int: Int) {
        HttpClient.getnorRetrofit().create(DownApi::class.java)
                .downloadFile(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeNx({
                    getMvpView().downRankSuc(it,name,type,int)
                }).bindRxLifeEx(RxLife.ON_DESTROY)
    }


    override fun getWrod(word: String) {
        getMvpView().showLoading()
        HttpClient.getnorRetrofit().create(TranslateApi::class.java)
                .searchWord(word)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeEx({
                    getMvpView().searchWordResult(it)
                    getMvpView().hideLoading()
                }, {
                    getMvpView().searchFail()
                    getMvpView().hideLoading()
                }).bindRxLifeEx(RxLife.ON_DESTROY)
    }

    override fun getWeather(city: String) {
        val map = HashMap<String, Any>()
        map.put("key", Constant.WEATHER_KEY)
        map.put("location", city)
        HttpClient.getWeaRetrofit().create(TranslateApi::class.java).
                getWeather(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeNx({
                    getMvpView().setWeather(it)
                }, {
                    getMvpView().setWeatherError()
                }).bindRxLifeEx(RxLife.ON_DESTROY)

    }

}