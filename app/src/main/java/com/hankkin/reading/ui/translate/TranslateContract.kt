package com.hankkin.reading.ui.translate

import com.hankkin.reading.domain.Weatherbean
import com.hankkin.reading.domain.WordBean
import com.hankkin.reading.mvp.contract.IBaseLoading
import com.hankkin.reading.mvp.contract.IPresenterContract
import okhttp3.ResponseBody

/**
 * Created by huanghaijie on 2018/5/16.
 */
interface TranslateContract{

    interface IView : IBaseLoading{

        fun setWeather(weatherbean: Weatherbean)
        fun setWeatherError()
        fun searchWordResult(reponse: WordBean)
        fun searchFail()
        fun downRankSuc(body: ResponseBody,name: String,type: String,int: Int)

    }

    interface IPresenter : IPresenterContract {
        fun getWeather(city: String)

        fun getWrod(word: String)

        fun downRank(path: String,url: String,type: String,int: Int)
    }

}