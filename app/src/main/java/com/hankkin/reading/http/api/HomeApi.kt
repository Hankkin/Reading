package com.hankkin.reading.http.api

import com.hankkin.reading.domain.*
import io.reactivex.Observable
import retrofit2.http.*

/**
 * Created by huanghaijie on 2018/7/6.
 */
interface HomeApi {

    @GET("openapi/news_list")
    fun getNewsList(@QueryMap map: HashMap<String, Any>): Observable<MutableList<NewsListBean>>

    @GET("banner/json")
    fun getHomeBanner(): Observable<BaseResponse<MutableList<BannerBean>>>

    @GET("article/list/{page}/json")
    fun getArticle(@Path("page") page: Int): Observable<BaseResponse<ArticleBean>>

    @GET("tree/json")
    fun getCates(): Observable<BaseResponse<MutableList<CateBean>>>

    @GET("article/list/{page}/json")
    fun getArticleCid(@Path("page") page: Int, @Query("cid") cid: Int): Observable<BaseResponse<ArticleBean>>

    @GET("project/tree/json")
    fun getProject(): Observable<BaseResponse<MutableList<CateBean>>>

    @GET("hotkey/json")
    fun getHot(): Observable<BaseResponse<MutableList<HotBean>>>

    @FormUrlEncoded
    @POST("article/query/{page}/json")
    fun query(@Path("page") page: Int,@Field("k") key: String): Observable<BaseResponse<ArticleBean>>

    @FormUrlEncoded
    @POST("lg/collect/{id}/json")
    fun collectArticle(@Path("id") id: Int): Observable<BaseResponse<String>>

}