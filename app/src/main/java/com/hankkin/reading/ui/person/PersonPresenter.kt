package com.hankkin.reading.ui.person

import com.hankkin.reading.mvp.presenter.BaseRxLifePresenter

/**
 * Created by huanghaijie on 2018/5/16.
 */
class PersonPresenter(mvpView : PersonContract.IView) : BaseRxLifePresenter<PersonContract.IView>(mvpView), PersonContract.IPresenter {

}