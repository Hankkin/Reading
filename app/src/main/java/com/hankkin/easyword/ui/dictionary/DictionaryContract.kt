package com.hankkin.easyword.ui.dictionary

import com.hankkin.easyword.mvp.contract.IBasePresenterContract
import com.hankkin.easyword.mvp.contract.IBaseViewContract

/**
 * Created by huanghaijie on 2018/5/16.
 */
interface DictionaryContract{

    interface IView : IBaseViewContract

    interface IPresenter : IBasePresenterContract
}