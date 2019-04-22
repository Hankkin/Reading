package com.hankkin.library.utils

import android.content.Context
import android.os.Looper
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import java.io.File

/**
 * Created by huanghaijie on 2018/8/17.
 */
object CacheUtils {

    // 图片缓存最大容量，150M，根据自己的需求进行修改
    const val GLIDE_CATCH_SIZE = 150 * 1000 * 1000

    // 图片缓存子目录
    const val GLIDE_CARCH_DIR = "image_catch"

    private fun clearGlideImg(context: Context) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Thread(Runnable {
                Glide.get(context).clearDiskCache()
            }).start()
        } else {
            Glide.get(context).clearDiskCache()
        }
    }

    fun clearCacte(context: Context) {
        clearGlideImg(context)
    }

    fun getCachesSize(context: Context) =
            FileUtils.getFormatSize(getGlideCacheSize(context))


    private fun getGlideCacheSize(context: Context): Double =
            FileUtils.getFolderSize(
                    File(context.cacheDir.absolutePath + "/" + InternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR)).toDouble()
}