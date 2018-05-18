package com.hankkin.easyword.utils

import android.os.Environment
import java.io.File
import java.io.IOException


/**
 * Created by huanghaijie on 2018/5/18.
 */
object FileUtils{
    const val TAG = "fileutils"

    val SAVE_RANK_PATH: String = Environment.getExternalStorageDirectory().getAbsolutePath() + "/easyword/rank"

    fun initSd(){
        val file = File(SAVE_RANK_PATH)
        if (!file.exists()) file.mkdir()
        LogUtils.d("$TAG 初始化文件夹成功 ${file.absolutePath}")
    }

     fun getNameFromUrl(url: String): String {
        return url.substring(url.lastIndexOf("/") + 1)
    }

    @Throws(IOException::class)
     fun isExistDir(saveDir: String): String {
        val downloadFile = File(saveDir)
        if (!downloadFile.mkdirs()) {
            downloadFile.createNewFile()
        }
        return downloadFile.getAbsolutePath()
    }
}