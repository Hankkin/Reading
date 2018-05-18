package com.hankkin.reading.view

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * Created by Hankkin on 16/4/19.
 */
class NoScrollViewPager(context: Context, attrs: AttributeSet) : ViewPager(context, attrs) {

    private val enabled = false



    //触摸没有反应就可以了
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (this.enabled) {
            super.onTouchEvent(event)
        } else false
    }


    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return if (this.enabled) {
            super.onInterceptTouchEvent(event)
        } else false

    }
}
