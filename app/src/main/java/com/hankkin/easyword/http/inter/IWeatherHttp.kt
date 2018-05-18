package com.hankkin.easyword.http.inter

import com.hankkin.easyword.domain.Weatherbean
import com.hankkin.easyword.http.protocol.HttpInterface
import io.reactivex.Observable

/**
 * Created by huanghaijie on 2018/5/17.
 */

/**
 * Created by huanghaijie on 2018/5/16.
 */
interface IWeatherHttp : HttpInterface {
    fun getWeather(city: String) : Observable<Weatherbean>
}