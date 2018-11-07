package com.hankkin.library.fuct.common

import android.view.View

/**
 * Created by Hankkin on 2018/11/7.
 */
abstract class DoubleClickListener : View.OnClickListener{

    private val DOUBLE_TIME = 1000
    private var lastClickTime = 0L

    override fun onClick(v: View?) {
        v?.let {
            val current = System.currentTimeMillis()
            if (current - lastClickTime < DOUBLE_TIME){
                onDoubleClick(v)
            }
            lastClickTime = current
        }
    }

    abstract fun onDoubleClick(v: View?)
}