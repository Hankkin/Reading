package com.hankkin.library.utils

import android.content.Context
import es.dmoral.toasty.Toasty

/**
 * Created by huanghaijie on 2018/8/13.
 */
object ToastUtils{


    fun showToast(context: Context,str: String){
        Toasty.normal(context,str).show()
    }

    fun showSuccess(context: Context,str: String){
        Toasty.success(context,str).show()
    }

    fun showInfo(context: Context,str: String){
        Toasty.info(context,str).show()
    }

    fun showError(context: Context,str: String){
        Toasty.error(context,str).show()
    }

}