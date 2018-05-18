package com.hankkin.reading.ui.translate

import com.hankkin.reading.domain.Weatherbean
import com.hankkin.reading.domain.WordBean
import com.hankkin.reading.mvp.contract.IBasePresenterContract
import com.hankkin.reading.mvp.contract.IBaseViewContract

/**
 * Created by huanghaijie on 2018/5/16.
 */
interface TranslateContract{

    interface IView : IBaseViewContract{

        fun setWeather(weatherbean: Weatherbean)
        fun setWeatherError()
        fun searchWordResult(wordBean: WordBean)
        fun searchFail()
        fun downRankSuc(path: String)

    }

    interface IPresenter : IBasePresenterContract{
        fun getWeather(city: String)

        fun getWrod(word: String)

        fun downRank(path: String,url: String)
    }

}