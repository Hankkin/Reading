package com.hankkin.library.mvp.contract

/**
 * Created by huanghaijie on 2018/6/12.
 */
interface IRefresh : IBaseViewContract {
    fun refresh()
    fun refreshStop()
}