package com.hankkin.reading.ui.login

import com.hankkin.reading.mvp.presenter.BaseRxLifePresenter

/**
 * Created by huanghaijie on 2018/5/21.
 */
class LoginPresenter(mvpView: LoginContract.IView) : BaseRxLifePresenter<LoginContract.IView>(mvpView),LoginContract.IPresenter{

}