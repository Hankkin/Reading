package com.hankkin.reading.base

/**
 * Created by huanghaijie on 2018/6/8.
 */
import android.os.Bundle
import com.hankkin.library.utils.StatusBarUtil
import com.hankkin.reading.mvp.contract.IPresenterContract
import com.hankkin.reading.mvp.view.MvpActivity
import com.hankkin.reading.utils.ThemeHelper

abstract class BaseMvpActivity<out P : IPresenterContract> : MvpActivity<P>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        StatusBarUtil.MIUISetStatusBarLightMode(this.getWindow(), true)
        StatusBarUtil.FlymeSetStatusBarLightMode(this.getWindow(), true)

        StatusBarUtil.setColor(this, resources.getColor(ThemeHelper.getCurrentColor(this)), 0)

        init(savedInstanceState)
        initView()
        initData()
    }

    abstract fun getLayoutId(): Int

    open fun init(savedInstanceState: Bundle?) {}

    open fun initView() {}

    open fun initData() {}


}