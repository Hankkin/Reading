package com.hankkin.easyword.mvp.presenter

import com.hankkin.easyword.mvp.contract.IBasePresenterContract
import com.hankkin.easyword.mvp.contract.IBaseViewContract
import com.hankkin.easyword.utils.LogUtils
import com.hankkin.easyword.utils.RxUtils
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

/**
 * Created by huanghaijie on 2018/5/16.
 */
abstract class BaseRxLifePresenter<out V : IBaseViewContract>(private val mvpView: V) : IBasePresenter<V>, IBasePresenterContract {

    enum class RxLife {
        ON_CREATE, ON_START, ON_RESUME, ON_PAUSE, ON_STOP, ON_DESTROY
    }

    private val mRxLifeMap = HashMap<RxLife, ArrayList<Disposable>>()

    override fun getMvpView() = mvpView

    override fun onCreate() {
        destroyRxLife(RxLife.ON_CREATE)
    }

    override fun onStart() {
        destroyRxLife(RxLife.ON_START)
    }

    override fun onResume() {
        destroyRxLife(RxLife.ON_RESUME)
    }

    override fun onPause() {
        destroyRxLife(RxLife.ON_PAUSE)
    }

    override fun onStop() {
        destroyRxLife(RxLife.ON_STOP)
    }

    override fun onDestroy() {
        destroyRxLife(RxLife.ON_DESTROY)
    }

    private fun destroyRxLife(rxLife: RxLife) {
        mRxLifeMap[rxLife]?.map {
            RxUtils.dispose(it)
        }
        mRxLifeMap[rxLife]?.clear()
    }

    /**
     * 扩展方法：用于管理RxJava生命周期
     * */
    fun Disposable.bindRxLifeEx(lifeLv: RxLife): Disposable {
        if (mRxLifeMap[lifeLv] != null) {
            mRxLifeMap[lifeLv]!!.add(this)
        } else {
            val rxList = ArrayList<Disposable>()
            rxList.add(this)
            mRxLifeMap[lifeLv] = rxList
        }
        return this
    }

    /**
     * 扩展方法：用于处理订阅事件发生时的公共代码
     * */
    fun <T> Observable<T>.subscribeEx(onNext: (data: T) -> Unit = {}, onError: (e: Throwable) -> Unit = {}, onComplete: () -> Unit = {}): Disposable {
        return this.subscribe({
            //编写订阅触发时的公共代码
            onNext.invoke(it)
        }, {
            //编写订阅失败的公共代码
            LogUtils.e(it)
            onError.invoke(it)
        }, {
            //编写订阅完成后的公共代码
            onComplete.invoke()
        })
    }
}