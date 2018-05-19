package com.hankkin.reading.ui.translate

import com.hankkin.reading.domain.BaseResponse
import com.hankkin.reading.domain.Weatherbean
import com.hankkin.reading.domain.WordBean
import com.hankkin.reading.mvp.contract.IBaseLoadingContract
import com.hankkin.reading.mvp.contract.IBasePresenterContract
import okhttp3.ResponseBody

/**
 * Created by huanghaijie on 2018/5/16.
 */
interface TranslateContract{

    interface IView : IBaseLoadingContract{

        fun setWeather(weatherbean: Weatherbean)
        fun setWeatherError()
        fun searchWordResult(reponse: BaseResponse<WordBean>)
        fun searchFail()
        fun downRankSuc(body: ResponseBody,name: String,type: String,int: Int)

    }

    interface IPresenter : IBasePresenterContract{
        fun getWeather(city: String)

        fun getWrod(word: String)

        fun downRank(path: String,url: String,type: String,int: Int)
    }

}