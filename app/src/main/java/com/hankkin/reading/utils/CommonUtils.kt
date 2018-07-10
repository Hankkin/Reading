package com.hankkin.reading.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri

/**
 * Created by huanghaijie on 2018/7/10.
 */
object CommonUtils {

    fun share(context: Activity, text: String){
        val intent = Intent(Intent.ACTION_SEND)
        intent.setType("text/plain")
        intent.putExtra(Intent.EXTRA_TEXT, text)
        context.startActivity(intent)
    }

    fun openBroswer(context: Activity,url: String){
        val intent = Intent()
        intent.setAction("android.intent.action.VIEW")
        intent.setData(Uri.parse(url))
        context.startActivity(intent)
    }
}