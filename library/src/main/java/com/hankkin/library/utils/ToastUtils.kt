package com.hankkin.library.utils

import android.content.Context
import android.view.View
import com.hankkin.library.R
import com.hankkin.library.widget.toasty.Toasty

/**
 * Created by huanghaijie on 2018/8/13.
 */
object ToastUtils{

    fun showToast(context: Context,str: String){
        Toasty.normal(context)
                .text(str)
                .show()
    }

    fun showTarget(context: Context,str: String,target: View){
        Toasty.info(context)
                .text(str)
                .showIcon(false)
                .target(target)
                .animate(true)
                .offsetY(20)
                .show()
    }

    fun showSuccess(context: Context,str: String){
        Toasty.success(context).
                text(str)
                .show()
    }

    fun showInfo(context: Context,str: String){
        Toasty.info(context)
                .text(str)
                .show()
    }

    fun showInfoTarget(context: Context,str: String,target: View){
        Toasty.info(context)
                .text(str)
                .target(target)
                .animate(true)
                .show()
    }

    fun showError(context: Context,str: String){
        Toasty.error(context)
                .text(str).show()
    }

    fun showWarning(context: Context,str: String){
        Toasty.warning(context)
                .text(str)
                .show()
    }

}