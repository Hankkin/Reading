package com.hankkin.reading.utils

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import com.hankkin.reading.R

/**
 * Created by huanghaijie on 2018/5/19.
 */
object LoadingUtils{

    private var loading: MaterialDialog.Builder? = null

    fun showLoading(context: Context){
        if (loading == null){
            loading = MaterialDialog.Builder(context)
                    .content(context.resources.getString(R.string.loading_wait))
                    .progress(true,0)
        }
        loading!!.show()
    }

    fun hideLoading(){
        if(loading != null ){
            loading!!.build().dismiss()
        }
    }
}