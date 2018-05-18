package com.hankkin.easyword.ui.person

import com.hankkin.easyword.mvp.presenter.BaseRxLifePresenter

/**
 * Created by huanghaijie on 2018/5/16.
 */
class PersonPresenter(mvpView : PersonContract.IView) : BaseRxLifePresenter<PersonContract.IView>(mvpView), PersonContract.IPresenter {

}