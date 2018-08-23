package com.hankkin.reading.utils

import android.content.Context
import com.hankkin.library.widget.dialog.HLoading

/**
 * Created by huanghaijie on 2018/5/19.
 */
object LoadingUtils {

    private var loading: HLoading? = null
    
    fun showLoading(context: Context?) { 
        if (loading == null){
            loading = HLoading(context!!, context.resources.getColor(ThemeHelper.getCurrentColor(context)))
        }
        loading!!.show()
    }

    fun hideLoading() {
        if (loading != null){
            loading!!.dismiss()
        }
    }

    fun onDestory(){
        if(loading != null){
            loading = null
        }
    }
}