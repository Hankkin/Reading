package com.hankkin.easyword.base

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import butterknife.ButterKnife
import com.hankkin.easyword.mvp.contract.IBasePresenterContract
import com.hankkin.easyword.mvp.view.BaseMvpActivity
import com.hankkin.easyword.mvp.view.BaseMvpFragmentActivity

/**
 * Created by huanghaijie on 2018/5/15.
 */
abstract class BaseActivity<out T : IBasePresenterContract>  : BaseMvpFragmentActivity<T>() {

    protected var activity: Activity? = null

    protected abstract fun getLayoutId(): Int

    protected abstract fun initData()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this
        if (getLayoutId() != 0) setContentView(getLayoutId())
        initBind()
        initViews(savedInstanceState)
        initData()
    }

    protected fun initBind(){
        ButterKnife.bind(this)
    }

    protected abstract fun initViews(savedInstanceState: Bundle?)
}