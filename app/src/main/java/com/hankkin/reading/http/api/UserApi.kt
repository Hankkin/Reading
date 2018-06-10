package com.hankkin.reading.http.api

import com.hankkin.reading.domain.AuthorizeResultBean
import com.hankkin.reading.domain.BaseResponse
import com.hankkin.reading.domain.TokenBean
import com.hankkin.reading.domain.UserBean
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface UserApi {

    @GET("account/profile")
    fun getUserProfile(): Observable<BaseResponse<UserBean>>

    @GET("openapi/token")
    fun getToken(@QueryMap map: HashMap<String,Any>): Observable<TokenBean>

    @GET("openapi/user")
    fun getUser(@QueryMap map: HashMap<String, Any>): Observable<UserBean>
}