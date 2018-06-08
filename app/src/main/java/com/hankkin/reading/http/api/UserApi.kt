package com.hankkin.reading.http.api

import com.hankkin.reading.domain.BaseResponse
import com.hankkin.reading.domain.TokenBean
import com.hankkin.reading.domain.UserBean
import io.reactivex.Observable
import retrofit2.http.GET

interface UserApi {

    @GET("account/profile")
    fun getUserProfile(): Observable<BaseResponse<UserBean>>

    @GET("action/openapi/token")
    fun getToken(map: HashMap<String,Any>): Observable<TokenBean>

    @GET("action/openapi/user")
    fun getUser(map: HashMap<String, Any>): Observable<UserBean>
}