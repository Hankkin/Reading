package com.hankkin.reading.http.api

import com.hankkin.reading.domain.BaseResponse
import com.hankkin.reading.domain.CaptchaBean
import com.hankkin.reading.domain.CsrfTokenBean
import io.reactivex.Observable
import retrofit2.http.*

/**
 * Created by huanghaijie on 2018/5/22.
 */
interface LoginApi {

    @GET("captcha/refresh")
    fun getCaptcha(): Observable<CaptchaBean>

    @GET("reading/csrftoken")
    fun getCsrfToken(): Observable<BaseResponse<CsrfTokenBean>>

    @FormUrlEncoded
    @POST("account/login")
    fun login(@FieldMap map: HashMap<String,Any>): Observable<String>

    @FormUrlEncoded
    @POST("account/signup")
    fun signUp(@FieldMap map: HashMap<String,String>): Observable<String>
}

