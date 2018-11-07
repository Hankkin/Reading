package com.hankkin.reading.http.api

import com.hankkin.library.domin.BaseResponse
import com.hankkin.reading.domain.*
import io.reactivex.Observable
import retrofit2.http.*
import java.util.*

/**
 * Created by huanghaijie on 2018/7/6.
 */
interface WanAndroidApi {

    @GET("openapi/news_list")
    fun getNewsList(@QueryMap map: HashMap<String, Any>): Observable<MutableList<NewsListBean>>

    @GET("banner/json")
    fun getHomeBanner(): Observable<BaseResponse<MutableList<BannerBean>>>

    /**
     * 知识体系下的文章
     */
    @GET("article/list/{page}/json")
    fun getArticle(@Path("page") page: Int): Observable<BaseResponse<ArticleBean>>

    /**
     * 项目分类
     */
    @GET("tree/json")
    fun getCates(): Observable<BaseResponse<MutableList<CateBean>>>

    /**
     * 知识体系下的文章
     */
    @GET("article/list/{page}/json")
    fun getArticleCid(@Path("page") page: Int, @Query("cid") cid: Int): Observable<BaseResponse<ArticleBean>>

    /**
     * 项目分类
     */
    @GET("project/tree/json")
    fun getProject(): Observable<BaseResponse<MutableList<CateBean>>>

    /**
     * 搜索热词
     */
    @GET("hotkey/json")
    fun getHot(): Observable<BaseResponse<MutableList<HotBean>>>

    /**
     * 搜索
     */
    @FormUrlEncoded
    @POST("article/query/{page}/json")
    fun query(@Path("page") page: Int, @Field("k") key: String): Observable<BaseResponse<ArticleBean>>

    /**
     * 收藏站内文章
     */
    @POST("lg/collect/{id}/json")
    fun collectArticle(@Path("id") id: Int): Observable<BaseResponse<CollectArticleBean>>

    /**
     * 取消收藏
     */
    @POST("lg/uncollect_originId/{id}/json")
    fun cancelCollectArticle(@Path("id") id: Int): Observable<BaseResponse<String>>

    /**
     * 收藏列表
     */
    @GET("lg/collect/list/{page}/json")
    fun getMyCollect(@Path("page") page: Int): Observable<BaseResponse<ArticleBean>>

    /**
     * 登录
     */
    @FormUrlEncoded
    @POST("user/login")
    fun login(@FieldMap map: HashMap<String, Any>): Observable<BaseResponse<UserBean>>

    /**
     * 注册
     */
    @FormUrlEncoded
    @POST("user/register")
    fun signUp(@FieldMap map: HashMap<String, String>): Observable<BaseResponse<UserBean>>

    /**
     * 新建TODO
     */
    @FormUrlEncoded
    @POST("lg/todo/add/json")
    fun addTodo(@FieldMap map: HashMap<String, Any>): Observable<BaseResponse<ToDoListBean>>

    /**
     * 更新TODO
     */
    @FormUrlEncoded
    @POST("lg/todo/update/{id}/json")
    fun updateTodo(@Path("id") id: Int, @FieldMap map: HashMap<String, Any>): Observable<BaseResponse<ToDoListBean>>

    /**
     * 删除TODO
     */
    @POST("lg/todo/delete/{id}/json")
    fun deleteTodo(@Path("id") id: Int): Observable<BaseResponse<String>>

    /**
     * 获取TODO
     */
    @GET("lg/todo/list/{cate}/json")
    fun getTodo(@Path("cate") cate: Int): Observable<BaseResponse<ToDoBean>>

    /**
     * 完成某条TODO
     */
    @FormUrlEncoded
    @POST("lg/todo/done/{id}/json")
    fun completeTodo(@Path("id") id: Int, @Field("status") status: Int): Observable<BaseResponse<ToDoListBean>>

    @GET("lg/todo/listnotdo/{cate}/json/{page}")
    fun listnotdo(@Path("cate") cate: Int, @Path("page") page: Int)

    @POST("lg/todo/listdone/{cate}/json/{page}")
    fun listDone(@Path("cate") cate: Int, @Path("page") page: Int): Observable<BaseResponse<DoneBean>>

    /**
     * 公众号tab
     */
    @GET("wxarticle/chapters/json")
    fun getWxChapters(): Observable<BaseResponse<MutableList<WxArticleBean>>>

    /**
     * 公众号历史数据
     */
    @GET("wxarticle/list/{id}/{page}/json")
    fun getWxList(@Path("id") id: Int,@Path("page") page: Int): Observable<BaseResponse<WxArticleListBean>>

}