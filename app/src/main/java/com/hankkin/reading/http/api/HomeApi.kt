package com.hankkin.reading.http.api

import com.hankkin.reading.domain.NewsListBean
import com.hankkin.reading.domain.UserBean
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.QueryMap

/**
 * Created by huanghaijie on 2018/7/6.
 */
interface HomeApi{

    @GET("openapi/news_list")
    fun getNewsList(@QueryMap map: HashMap<String, Any>): Observable<MutableList<NewsListBean>>
}