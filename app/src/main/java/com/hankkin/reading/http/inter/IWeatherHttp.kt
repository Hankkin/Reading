package com.hankkin.reading.http.inter

import com.hankkin.reading.domain.Weatherbean
import com.hankkin.reading.http.protocol.HttpInterface
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