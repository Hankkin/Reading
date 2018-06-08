package com.hankkin.reading.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hankkin.reading.mvp.contract.IPresenterContract
import com.hankkin.reading.mvp.view.MvpFragment

/**
 * Created by huanghaijie on 2018/5/15.
 */
abstract class BaseMvpFragment<out T : IPresenterContract> : MvpFragment<T>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        initData()
    }

    open fun init(savedInstanceState: Bundle?) {}
    abstract fun getLayoutId(): Int
    open fun initView() {}
    open fun initData() {}
}