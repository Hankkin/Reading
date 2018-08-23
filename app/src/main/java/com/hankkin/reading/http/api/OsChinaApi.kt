package com.hankkin.reading.http.api

import com.hankkin.library.domin.BaseResponse
import com.hankkin.reading.domain.*
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface OsChinaApi{
    @GET("account/profile")
    fun getUserProfile(): Observable<BaseResponse<UserBean>>

    @GET("openapi/token")
    fun getToken(@QueryMap map: HashMap<String,Any>): Observable<TokenBean>

    @GET("openapi/user")
    fun getUser(@QueryMap map: HashMap<String, Any>): Observable<UserBean>

    @GET("openapi/my_information")
    fun getUserInfo(@QueryMap map: HashMap<String, Any>): Observable<UserInfoBean>

    @GET("openapi/user_notice")
    fun getUserNotice(@Query("access_token") access_token: String): Observable<NoticeBean>

    @GET("openapi/friends_list")
    fun getFriendList(@QueryMap map: HashMap<String, Any>): Observable<FriendBean>
}