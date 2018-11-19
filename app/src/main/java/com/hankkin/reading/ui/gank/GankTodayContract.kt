package com.hankkin.reading.ui.gank

import com.hankkin.library.mvp.contract.IBaseViewContract
import com.hankkin.library.mvp.contract.IPresenterContract
import com.hankkin.reading.domain.GankToadyBean

/**
 * Created by Hankkin on 2018/11/8.
 */
interface GankTodayContract {
    interface IView : IBaseViewContract{
        fun setGanks(gankBean: GankToadyBean?)
    }

    interface IPresenter : IPresenterContract {
        fun getGanksToday()
    }
}