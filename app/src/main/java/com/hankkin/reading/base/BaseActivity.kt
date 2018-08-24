package com.hankkin.reading.base

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper
import com.hankkin.library.utils.RxBusTools
import com.hankkin.reading.R
import com.hankkin.reading.event.EventMap
import com.hankkin.reading.utils.LoadingUtils
import com.hankkin.reading.utils.ThemeHelper
import com.jaeger.library.StatusBarUtil
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer

/**
 * Created by huanghaijie on 2018/5/15.
 */
abstract class BaseActivity : AppCompatActivity(), BGASwipeBackHelper.Delegate {

    protected var disposables = CompositeDisposable()

    protected var activity: Activity? = null

    protected lateinit var mSwipeBackHelper: BGASwipeBackHelper

    protected abstract fun getLayoutId(): Int

    protected abstract fun initViews(savedInstanceState: Bundle?)

    protected abstract fun initData()


    override fun onCreate(savedInstanceState: Bundle?) {
        initSwipeBackFinish()
        super.onCreate(savedInstanceState)
        activity = this
        if (getLayoutId() != 0) setContentView(getLayoutId())
        initViews(savedInstanceState)
        registerEvent()
        initData()
    }

    /**
     * 设置白底黑字
     */
    protected fun setMiuiStatusBar() {
        StatusBarUtil.setColorForSwipeBack(this,resources.getColor(R.color.white),0)
        StatusBarUtil.setLightMode(this)
    }

    /**
     * 是否注册rxbus
     */
    open fun isHasBus(): Boolean {
        return false
    }

    protected fun registerEvent() {
        if (isHasBus()) {
            val disposable = RxBusTools.getDefault().register(EventMap.BaseEvent::class.java, Consumer { onEvent(it) })
            disposables.add(disposable)
        }
    }

    /**
     * 初始化滑动返回。在 super.onCreate(savedInstanceState) 之前调用该方法
     */
    private fun initSwipeBackFinish() {
        mSwipeBackHelper = BGASwipeBackHelper(this, this)

        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回」
        // 下面几项可以不配置，这里只是为了讲述接口用法。

        // 设置滑动返回是否可用。默认值为 true
        mSwipeBackHelper.setSwipeBackEnable(true)
        // 设置是否仅仅跟踪左侧边缘的滑动返回。默认值为 true
        mSwipeBackHelper.setIsOnlyTrackingLeftEdge(true)
        // 设置是否是微信滑动返回样式。默认值为 true
        mSwipeBackHelper.setIsWeChatStyle(false)
        // 设置阴影资源 id。默认值为 R.drawable.bga_sbl_shadow
        mSwipeBackHelper.setShadowResId(R.drawable.bga_sbl_shadow)
        // 设置是否显示滑动返回的阴影效果。默认值为 true
        mSwipeBackHelper.setIsNeedShowShadow(true)
        // 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
        mSwipeBackHelper.setIsShadowAlphaGradient(true)
        // 设置触发释放后自动滑动返回的阈值，默认值为 0.3f
        mSwipeBackHelper.setSwipeBackThreshold(0.3f)
        // 设置底部导航条是否悬浮在内容上，默认值为 false
        mSwipeBackHelper.setIsNavigationBarOverlap(false)
    }

    /**
     * 是否支持滑动返回。这里在父类中默认返回 true 来支持滑动返回，如果某个界面不想支持滑动返回则重写该方法返回 false 即可
     *
     * @return
     */
    override fun isSupportSwipeBack(): Boolean {
        return true
    }

    /**
     * 正在滑动返回
     *
     * @param slideOffset 从 0 到 1
     */
    override fun onSwipeBackLayoutSlide(slideOffset: Float) {}

    /**
     * 没达到滑动返回的阈值，取消滑动返回动作，回到默认状态
     */
    override fun onSwipeBackLayoutCancel() {}

    /**
     * 滑动返回执行完毕，销毁当前 Activity
     */
    override fun onSwipeBackLayoutExecuted() {
        mSwipeBackHelper.swipeBackward()
    }

    override fun onBackPressed() {
        // 正在滑动返回的时候取消返回按钮事件
        if (mSwipeBackHelper.isSliding) {
            return
        }
        mSwipeBackHelper.backward()
    }

    /**
     * 设置状态栏颜色
     *
     * @param color
     */
    protected fun setStatusBarColor() {
        StatusBarUtil.setColorForSwipeBack(this, resources.getColor(ThemeHelper.getCurrentColor(this)), 0)
    }

    open fun onEvent(event: EventMap.BaseEvent) {
    }

    override fun onDestroy() {
        super.onDestroy()
        LoadingUtils.onDestory()
        disposables.clear()
    }
}