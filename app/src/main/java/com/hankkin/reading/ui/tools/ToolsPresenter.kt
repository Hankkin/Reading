package com.hankkin.reading.ui.tools

import com.hankkin.reading.common.Constant
import com.hankkin.reading.http.HttpClient
import com.hankkin.reading.http.api.TranslateApi
import com.hankkin.reading.mvp.presenter.RxLifePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by huanghaijie on 2018/5/16.
 */
class ToolsPresenter : RxLifePresenter<ToolsContract.IView>(), ToolsContract.IPresenter {

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