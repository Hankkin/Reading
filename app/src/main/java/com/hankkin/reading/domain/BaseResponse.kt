package com.hankkin.reading.domain

/**
 * Created by huanghaijie on 2018/5/19.
 */
data class BaseResponse<T>(val state: Int, val message: String,val data: T)