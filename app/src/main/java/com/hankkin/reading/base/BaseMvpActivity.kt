package com.hankkin.reading.base

/**
 * Created by huanghaijie on 2018/6/8.
 */
import android.os.Bundle
import com.hankkin.library.utils.StatusBarUtil
import com.hankkin.reading.event.EventMap
import com.hankkin.reading.mvp.contract.IPresenterContract
import com.hankkin.reading.mvp.view.MvpActivity
import com.hankkin.reading.utils.RxBusTools
import com.hankkin.reading.utils.ThemeHelper
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer

abstract class BaseMvpActivity<out P : IPresenterContract> : MvpActivity<P>() {


    var disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        StatusBarUtil.MIUISetStatusBarLightMode(this.getWindow(), true)
        StatusBarUtil.FlymeSetStatusBarLightMode(this.getWindow(), true)

        StatusBarUtil.setColor(this, resources.getColor(ThemeHelper.getCurrentColor(this)), 0)

        init(savedInstanceState)
        initView()
        registerEvent()
        initData()
    }

    open fun isHasBus(): Boolean{
        return false
    }

    protected fun registerEvent(){
        if (isHasBus()){
            val disposable = RxBusTools.getDefault().register(EventMap.BaseEvent::class.java, Consumer { onEvent(it) })
            disposables.add(disposable)
        }
    }

    open  fun onEvent(event: EventMap.BaseEvent){
    }

    abstract fun getLayoutId(): Int

    open fun init(savedInstanceState: Bundle?) {}

    open fun initView() {}

    open fun initData() {}


    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }


}