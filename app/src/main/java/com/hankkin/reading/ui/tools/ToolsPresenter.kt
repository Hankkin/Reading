package com.hankkin.reading.ui.tools

import com.hankkin.reading.common.Constant
import com.hankkin.reading.http.HttpClientUtils
import com.hankkin.library.mvp.presenter.RxLifePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by huanghaijie on 2018/5/16.
 */
class ToolsPresenter : RxLifePresenter<ToolsContract.IView>(), ToolsContract.IPresenter {

    override fun getWeather(city: String) {
        val map = HashMap<String, Any>()
        map.put("key", Constant.COMMON.WEATHER_KEY)
        map.put("location", city)
        HttpClientUtils.Builder.getToolsHttp().getWeather(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeNx({
                    getMvpView().setWeather(it)
                }, {
                    getMvpView().setWeatherError()
                }).bindRxLifeEx(RxLife.ON_DESTROY)

    }

}