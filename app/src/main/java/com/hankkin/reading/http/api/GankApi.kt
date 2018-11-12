package com.hankkin.reading.http.api

import com.hankkin.reading.domain.GankBean
import com.hankkin.reading.domain.GankToadyBean
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by Hankkin on 2018/11/8.
 */
interface GankApi {

    @GET("data/{cate}/20/{page}")
    fun getGank(@Path("cate") cate: String, @Path("page") page: Int): Observable<GankBean>

    @GET("today")
    fun getToday(): Observable<GankToadyBean>
}