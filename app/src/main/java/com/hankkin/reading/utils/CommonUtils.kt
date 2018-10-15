package com.hankkin.reading.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import com.hankkin.library.utils.ToastUtils
import com.hankkin.reading.domain.AppInfo
import java.util.*


/**
 * Created by huanghaijie on 2018/7/10.
 */
object CommonUtils {

    fun share(context: Activity, text: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.setType("text/plain")
        intent.putExtra(Intent.EXTRA_TEXT, text)
        context.startActivity(intent)
    }

    fun openBroswer(context: Activity, url: String) {
        val intent = Intent()
        intent.setAction("android.intent.action.VIEW")
        intent.setData(Uri.parse(url))
        context.startActivity(intent)
    }

    fun feedBack(context: Activity) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "message/rfc822"
        intent.putExtra(Intent.EXTRA_EMAIL, "1019283569@qq.com")
        intent.putExtra(Intent.EXTRA_SUBJECT, "您的建议")
        intent.putExtra(Intent.EXTRA_TEXT, "希望能得到您的建议")
        context.startActivity(Intent.createChooser(intent, "Select email application"))
    }

    fun gradeApp(context: Context) {
        try {
            val uri = Uri.parse("market://details?id=${context.packageName}")
            val intent = Intent(Intent(Intent.ACTION_VIEW, uri))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            ToastUtils.showError(context, "您的手机还没有安装任何安装安装应用市场")
        }
    }

    fun getApps(context: Context): MutableList<AppInfo> {
        val list = mutableListOf<AppInfo>()
        val appInfos = context.packageManager.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES)
        Collections.sort(appInfos, ApplicationInfo.DisplayNameComparator(context.packageManager))
        appInfos.forEach {
            if ((it.flags and ApplicationInfo.FLAG_SYSTEM) <= 0) {
                val applicationInfo = AppInfo(it.packageName, it.loadLabel(context.packageManager).toString(), it.loadIcon(context.packageManager))
                list.add(applicationInfo)
            } else if ((it.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                val applicationInfo = AppInfo(it.packageName, it.loadLabel(context.packageManager).toString(), it.loadIcon(context.packageManager))
                list.add(applicationInfo)
            }
        }
        return list
    }

}