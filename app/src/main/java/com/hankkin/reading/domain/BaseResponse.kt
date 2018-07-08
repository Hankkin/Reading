package com.hankkin.reading.domain

/**
 * Created by huanghaijie on 2018/5/19.
 */
data class BaseResponse<T>(val errorCode: Int, val errorMsg: String,val data: T)