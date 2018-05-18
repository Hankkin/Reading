package com.hankkin.reading.ui.dictionary

import com.hankkin.reading.mvp.presenter.BaseRxLifePresenter

/**
 * Created by huanghaijie on 2018/5/16.
 */
class DictionaryPresenter(mvpView: DictionaryContract.IView) : BaseRxLifePresenter<DictionaryContract.IView>(mvpView), DictionaryContract.IPresenter {

}