package com.hankkin.reading.utils

import android.content.Context
import com.hankkin.library.widget.HLoading
import com.hankkin.reading.EApplication
import com.hankkin.reading.R

/**
 * Created by huanghaijie on 2018/5/19.
 */
object LoadingUtils{

    private var loading: HLoading? = null

    fun showLoading(context: Context){
        if (loading == null){
            loading = HLoading(context,context.resources.getColor(R.color.colorPrimary))
        }
        loading!!.show()
    }

    fun hideLoading(){
        if(loading != null && loading!!.isShowing){
            loading!!.hide()
        }
    }
}