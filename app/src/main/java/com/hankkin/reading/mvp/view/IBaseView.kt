package com.hankkin.reading.mvp.view

import com.hankkin.reading.mvp.contract.IPresenterContract

/**
 * Created by huanghaijie on 2018/6/8.
 */
interface IBaseView<out P : IPresenterContract>{
    fun registerPresenter(): Class<out  P>
}