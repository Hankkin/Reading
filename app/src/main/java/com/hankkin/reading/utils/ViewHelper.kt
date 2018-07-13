package com.hankkin.reading.utils

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import com.afollestad.materialdialogs.MaterialDialog
import com.hankkin.reading.R

/**
 * Created by huanghaijie on 2018/7/10.
 */
object ViewHelper{

    fun setRefreshLayout(context: Context?, isAutoRefresh: Boolean,
                         layout: SwipeRefreshLayout,
                         onRefreshListener: SwipeRefreshLayout.OnRefreshListener){
        layout.setColorSchemeResources(ThemeHelper.getCurrentColor(context))
        layout.setOnRefreshListener(onRefreshListener)
        if (isAutoRefresh) layout.isRefreshing = true
    }

    fun changeRefreshColor(layout: SwipeRefreshLayout,context: Context?){
        layout.setColorSchemeResources(ThemeHelper.getCurrentColor(context))
    }

    fun showConfirmDialog(context: Context,content: String,callback: MaterialDialog.SingleButtonCallback){
        MaterialDialog.Builder(context)
                .content(content)
                .positiveText(context.resources.getString(R.string.ok))
                .negativeText(context.resources.getString(R.string.cancel))
                .onPositive(callback)
                .show()

    }

}