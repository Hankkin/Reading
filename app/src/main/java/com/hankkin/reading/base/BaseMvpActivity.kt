package com.hankkin.reading.base

/**
 * Created by huanghaijie on 2018/6/8.
 */
import android.os.Bundle
import com.hankkin.reading.mvp.contract.IPresenterContract
import com.hankkin.reading.mvp.view.MvpActivity

abstract class BaseMvpActivity<out P : IPresenterContract> : MvpActivity<P>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        init(savedInstanceState)
        initView()
        initData()
    }

    abstract fun getLayoutId(): Int

    open fun init(savedInstanceState: Bundle?) {}

    open fun initView() {}

    open fun initData() {}

}