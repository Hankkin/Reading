package com.hankkin.easyword.ui.translate

import com.hankkin.easyword.domain.Weatherbean
import com.hankkin.easyword.domain.WordBean
import com.hankkin.easyword.mvp.contract.IBasePresenterContract
import com.hankkin.easyword.mvp.contract.IBaseViewContract

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