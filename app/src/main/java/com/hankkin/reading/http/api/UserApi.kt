package com.hankkin.reading.http.api

import com.hankkin.reading.domain.BaseResponse
import com.hankkin.reading.domain.UserBean
import io.reactivex.Observable
import retrofit2.http.GET

interface UserApi {

    @GET("account/profile")
    fun getUserProfile(): Observable<BaseResponse<UserBean>>
}