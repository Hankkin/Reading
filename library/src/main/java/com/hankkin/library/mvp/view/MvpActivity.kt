package com.hankkin.library.mvp.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.hankkin.library.base.BaseSwipeBackActivity
import com.hankkin.library.mvp.contract.IBaseViewContract
import com.hankkin.library.mvp.contract.IPresenterContract

/**
 * Created by huanghaijie on 2018/5/16.
 */
 abstract class MvpActivity<out T : IPresenterContract> : BaseSwipeBackActivity() , IBaseView<T>, IBaseViewContract {

    private val mPresenter: T by lazy {
        val clazz = registerPresenter()
        val constructor = clazz.getConstructor()
        val presenter = constructor.newInstance()
        presenter.registerMvpView(this)
        presenter
    }

    fun getPresenter(): T = mPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(mPresenter)
    }

}