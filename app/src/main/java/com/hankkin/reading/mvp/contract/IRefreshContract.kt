package com.hankkin.reading.mvp.contract

/**
 * Created by huanghaijie on 2018/5/19.
 */
interface IRefreshContract : IBaseViewContract {
    fun refresh()
    fun refreshStop()
}