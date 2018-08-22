package com.hankkin.library.utils

/**
 * Created by huanghaijie on 2018/8/22.
 */
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils

object PhoneUtils {


    fun callPhone(context: Context?, phone: String?) {
        if (TextUtils.isEmpty(phone) || context == null) return
        var telPhone = phone
        if (!phone!!.contains("tel:")) {
            telPhone = "tel:$phone"
        }
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse(telPhone))
        context.startActivity(intent)
    }
}