package com.hankkin.reading.ui.tools

import com.hankkin.reading.domain.Weatherbean
import com.hankkin.library.mvp.contract.IBaseLoading
import com.hankkin.library.mvp.contract.IPresenterContract

/**
 * Created by huanghaijie on 2018/5/16.
 */
interface ToolsContract{

    interface IView : IBaseLoading {
        fun setWeather(weatherbean: Weatherbean)
        fun setWeatherError()
    }

    interface IPresenter : IPresenterContract {
        fun getWeather(city: String)
    }

}