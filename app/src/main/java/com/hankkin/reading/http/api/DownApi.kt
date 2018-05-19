package com.hankkin.reading.http.api

import io.reactivex.Observable
import retrofit2.http.Url
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming



/**
 * Created by huanghaijie on 2018/5/19.
 */
interface DownApi{
    @Streaming
    @GET
    fun downloadFile(@Url fileUrl: String): Observable<ResponseBody>
}