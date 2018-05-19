package com.hankkin.reading.utils

import android.content.Context
import android.os.Environment
import java.io.File
import java.io.IOException


/**
 * Created by huanghaijie on 2018/5/18.
 */
object FileUtils {
    const val TAG = "fileutils"

    val SAVE_RANK_PATH: String = Environment.getExternalStorageDirectory().getAbsolutePath() + "/reading"
    val RANK_PATH: String = Environment.getExternalStorageDirectory().getAbsolutePath() + "/reading/rank/"

    fun initSd() {
        val file = File(SAVE_RANK_PATH)
        if (!file.exists()) file.mkdirs()
        LogUtils.d("$TAG 初始化文件夹成功 ${file.absolutePath}")
    }

    fun getNameFromUrl(url: String): String {
        return url.substring(url.lastIndexOf("/") + 1)
    }

    @Throws(IOException::class)
    fun isExistDir(saveDir: String, type: String): String {
        val path = File(SAVE_RANK_PATH + "/$type")
        if (!path.exists()) path.mkdirs()
        val downloadFile = File(path.absolutePath + "/$saveDir")
        if (!downloadFile.exists()) {
            downloadFile.createNewFile()
        }
        return downloadFile.getAbsolutePath()
    }

    fun isDownloadedRank(name: String, type: String): Boolean {
        val path = SAVE_RANK_PATH + File.separator + "rank" + File.separator + type + File.separator + name + ".mp3"
        if (File(path).exists()){
            return true
        }
        return false
    }
}