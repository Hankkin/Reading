package com.hankkin.reading.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hankkin.reading.event.EventMap
import com.hankkin.library.utils.RxBusTools
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer

/**
 * Created by huanghaijie on 2018/5/15.
 */
abstract class BaseFragment : Fragment() {


    protected var TAG: String? = null

    protected var activity: Activity? = null

    private var isShowVisible = false

    private var isInitView = false

    private var isFirstLoad = true

     var disposables = CompositeDisposable()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        TAG = javaClass.simpleName
        activity = context as Activity?
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        this.activity = activity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val convertView: View = inflater?.inflate(getLayoutId(), container, false)!!
        return convertView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
        registerEvent()
        isInitView = true
        lazyLoadData()
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

    protected abstract fun getLayoutId(): Int

    protected abstract fun initData()

    protected abstract fun initViews()


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        if (isVisibleToUser) {
            isShowVisible = true
            lazyLoadData()
        } else isShowVisible = false
        super.setUserVisibleHint(isVisibleToUser)
    }

    protected fun lazyLoadData() {
        if (!isFirstLoad || !isShowVisible || !isInitView) return
        initData()
        isFirstLoad = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

}