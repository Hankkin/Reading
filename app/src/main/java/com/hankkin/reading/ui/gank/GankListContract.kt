package com.hankkin.reading.ui.gank

import com.hankkin.library.mvp.contract.IBaseViewContract
import com.hankkin.library.mvp.contract.IPresenterContract
import com.hankkin.reading.domain.GankBean

/**
 * Created by Hankkin on 2018/11/8.
 */
interface GankListContract {
    interface IView : IBaseViewContract{
        fun setGanks(gankBean: GankBean)
    }

    interface IPresenter : IPresenterContract {
        fun getGanks(cate: String,page: Int)
    }
}