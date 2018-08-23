package com.hankkin.library.mvp.view

import com.hankkin.library.mvp.contract.IPresenterContract


/**
 * Created by huanghaijie on 2018/6/8.
 */
interface IBaseView<out P : IPresenterContract>{
    fun registerPresenter(): Class<out  P>
}