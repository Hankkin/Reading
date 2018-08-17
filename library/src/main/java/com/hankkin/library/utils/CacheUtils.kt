package com.hankkin.library.utils

import android.content.Context
import android.os.Looper
import com.bumptech.glide.Glide
import java.io.File

/**
 * Created by huanghaijie on 2018/8/17.
 */
object CacheUtils{

    // 图片缓存最大容量，150M，根据自己的需求进行修改
    const val GLIDE_CATCH_SIZE = 150 * 1000 * 1000

    // 图片缓存子目录
    const val GLIDE_CARCH_DIR = "image_catch"

    fun clearGlideImg(context: Context){
        if (Looper.myLooper() == Looper.getMainLooper()){
            Thread(Runnable {
                Glide.get(context).clearDiskCache()
                Glide.get(context).clearMemory()
            }).start()
        }
        else{
            Glide.get(context).clearDiskCache()
            Glide.get(context).clearMemory()
        }
    }

    fun getGlideCacheSize(context: Context): String =
            FileUtils.getFormatSize(FileUtils.getFolderSize(
                    File(context.cacheDir.absolutePath + "/" + GLIDE_CARCH_DIR)).toDouble())
}