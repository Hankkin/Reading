package com.hankkin.reading.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hankkin.reading.event.EventMap
import com.hankkin.library.mvp.contract.IPresenterContract
import com.hankkin.library.mvp.view.MvpFragment
import com.hankkin.library.utils.AppUtils
import com.hankkin.library.utils.RxBusTools
import com.hankkin.library.widget.view.PageLayout
import com.hankkin.reading.R
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer

/**
 * Created by huanghaijie on 2018/5/15.
 */
abstract class BaseMvpFragment<out T : IPresenterContract> : MvpFragment<T>() {

    protected var TAG: String? = null

    protected var activity: Activity? = null

    private var isShowVisible = false

    private var isInitView = false

    private var isFirstLoad = true

    var disposables = CompositeDisposable()

    protected lateinit var mPageLayout: PageLayout



    override fun onAttach(context: Context?) {
        super.onAttach(context)
        TAG = javaClass.simpleName
        activity = context as Activity?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    protected fun initPageLayout(targetView: Any, isShowLoading: Boolean = false){
        mPageLayout = PageLayout.Builder(context!!)
                .initPage(targetView)
                .setDefaultEmptyText(resources.getString(R.string.pagelayout_empty))
                .setDefaultErrorText(resources.getString(R.string.pagelayout_error))
                .setDefaultLoadingBlinkText(AppUtils.getAppName(this.context!!)!!)
                .create()
        if (isShowLoading) mPageLayout.showLoading()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        registerEvent()
        isInitView = true
        lazyLoadData()
    }

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

    open fun isHasBus(): Boolean {
        return false
    }

    protected fun registerEvent() {
        if (isHasBus()) {
            val disposable = RxBusTools.getDefault().register(EventMap.BaseEvent::class.java, Consumer { onEvent(it) })
            disposables.add(disposable)
        }
    }

    open fun onEvent(event: EventMap.BaseEvent) {
    }

    open fun init(savedInstanceState: Bundle?) {}
    abstract fun getLayoutId(): Int
    open fun initView() {}
    open fun initData() {}

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }
}