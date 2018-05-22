package com.hankkin.reading.http.api

import com.hankkin.reading.domain.BaseResponse
import com.hankkin.reading.domain.CaptchaBean
import io.reactivex.Observable
import retrofit2.http.GET

/**
 * Created by huanghaijie on 2018/5/22.
 */
interface LoginApi {

    @GET("captcha/refresh")
    fun getCaptcha(): Observable<CaptchaBean>

}