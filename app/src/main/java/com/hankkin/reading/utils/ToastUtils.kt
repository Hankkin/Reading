package com.hankkin.reading.utils

import android.content.Context
import android.widget.Toast

/**
 * Created by huanghaijie on 2018/5/19.
 */
object ToastUtils{

    fun showToast(context: Context, msg: String) {
        val mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
        mToast.setText(msg)
        mToast.show()
    }

    fun showToast(context: Context, resId: Int) {
        val mToast = Toast.makeText(context, resId, Toast.LENGTH_SHORT)
        mToast.setText(resId)
        mToast.show()
    }

}