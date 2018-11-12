package com.hankkin.library.widget.toasty

import android.content.Context
import android.os.CountDownTimer
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
import com.hankkin.library.R

/**
 * Created by Hankkin on 2018/11/12.
 */
class ToastView(var mContext: Context) {
    private lateinit var parentView: View
    private var mOffsetY = 20
    private var mOffsetX = 0
    private lateinit var mPopupWindow: PopupWindow
    private var mDuration = 3000
    private lateinit var mCountDownTimer: CountDownTimer
    private var mWindowState = WindowState.onIdle

    enum class WindowState {
        onStart,
        onStop,
        onIdle,
        onShow
    }

    fun setParentView(parent: View): ToastView{
        this.parentView = parent
        return this
    }


    init {
        val contentView = View.inflate(mContext, R.layout.layout_toast_view,null)
        contentView.setLayoutParams(ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT))//防止调用measure时为空指针
        mPopupWindow = PopupWindow(contentView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, false)
        mPopupWindow.setContentView(contentView)
        mPopupWindow.setOutsideTouchable(false)
        mPopupWindow.setTouchable(true)
    }

    fun show(){
        if (mWindowState == WindowState.onStop) return
        mPopupWindow.showAsDropDown(parentView,mOffsetX,mOffsetY)
    }

}