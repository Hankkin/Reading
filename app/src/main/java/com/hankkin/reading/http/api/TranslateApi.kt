package com.hankkin.reading.http.api

import com.hankkin.reading.domain.BaseResponse
import com.hankkin.reading.domain.Weatherbean
import com.hankkin.reading.domain.WordBean
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

/**
 * Created by huanghaijie on 2018/5/19.
 */
interface TranslateApi {

    @GET("v3/weather/now.json")
    fun getWeather(@QueryMap params: HashMap<String,Any>) : Observable<Weatherbean>

    @GET("en-us/word/search/{word}/?format=json")
    fun searchWord(@Path("word") word: String) : Observable<BaseResponse<WordBean>>
}