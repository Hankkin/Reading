package com.hankkin.easyword.http.impl

import com.hankkin.easyword.common.Constant
import com.hankkin.easyword.domain.Weatherbean
import com.hankkin.easyword.domain.WordBean
import com.hankkin.easyword.http.HttpClient
import com.hankkin.easyword.http.JsonParser
import com.hankkin.easyword.http.inter.ISearchWordHttp
import com.hankkin.easyword.http.inter.IWeatherHttp
import com.hankkin.easyword.http.protocol.BaseHttpProtocol
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

/**
 * Created by huanghaijie on 2018/5/16.
 */
object TranslateHttpProtocol : BaseHttpProtocol(), IWeatherHttp,ISearchWordHttp {
    override fun downRank(path: String,url: String): Observable<String> {
        return HttpClient.downFile(path,url)
    }

    override fun searchWord(word: String): Observable<WordBean> {
        val url = Constant.ConfigUrl.SEARCH_WORD_URL+"word/search/"+ word+" /?format=json"
        return createObservable(url
                ,HttpClient.METHOD_GET,null){
            JsonParser.fromJsonObj(it,WordBean::class.java)
        }
    }

    override fun getWeather(city: String): Observable<Weatherbean> {
        val params = HashMap<String,String>()
        cp(params,"key",Constant.WEATHER_KEY)
        cp(params,"location",city)
        return createObservable(Constant.ConfigUrl.WEATHER_URL,HttpClient.METHOD_GET,params){
            JsonParser.fromJsonObj(it,Weatherbean::class.java)
        }
    }



}