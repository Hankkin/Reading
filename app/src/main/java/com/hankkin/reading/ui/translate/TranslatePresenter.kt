package com.hankkin.reading.ui.translate

import com.hankkin.reading.common.Constant
import com.hankkin.reading.http.HttpClient
import com.hankkin.reading.http.api.ApiTranslate
import com.hankkin.reading.mvp.presenter.BaseRxLifePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by huanghaijie on 2018/5/16.
 */
class TranslatePresenter(mvpView : TranslateContract.IView) : BaseRxLifePresenter<TranslateContract.IView>(mvpView), TranslateContract.IPresenter {


    override fun downRank(path: String, url: String,type: String) {
//        HttpFactory.getProtocol(ISearchWordHttp::class.java)
//                .downRank(path,url,type)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeEx ({ getMvpView().downRankSuc(it) })
    }



    override fun getWrod(word: String) {
        HttpClient.getnorRetrofit().create(ApiTranslate::class.java)
                .searchWord(word)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeEx ({
                    getMvpView().searchWordResult(it)
                }, {
                    getMvpView().searchFail()
                }).bindRxLifeEx(RxLife.ON_DESTROY)
    }

    override fun getWeather(city: String) {
        val map = HashMap<String,Any>()
        map.put("key",Constant.WEATHER_KEY)
        map.put("location",city)
        HttpClient.getWeaRetrofit().create(ApiTranslate::class.java).
                getWeather(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeEx ({
                    getMvpView().setWeather(it)
                },{
                    getMvpView().setWeatherError()
                }).bindRxLifeEx(RxLife.ON_DESTROY)

    }

}