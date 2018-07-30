package com.hankkin.reading.utils

import com.hankkin.library.utils.RxLogTool
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream

/**
 * Created by huanghaijie on 2018/5/19.
 */
object DownUtils {

    const val TAG = "DownUtils"

    const val TYPE_MP3 = ".mp3"


    fun saveRank(name: String, type: String, body: ResponseBody) {

        val rankPath = FileUtils.SAVE_RANK_PATH + File.separator + "rank"
        val rankFile = File(rankPath)
        if (!rankFile.exists()) rankFile.mkdirs()

        val typePath = rankPath + File.separator + type
        val typeFile = File(typePath)
        if (!typeFile.exists()) typeFile.mkdirs()
        val mp3File = File(typeFile.absolutePath + File.separator + name +".mp3")

        val fileReader = ByteArray(4096)

        val fileSize = body.contentLength()
        var fileSizeDownloaded: Long = 0
        val inputStream = body.byteStream()
        val outputStream = FileOutputStream(mp3File)
        while (true) {
            val read = inputStream.read(fileReader)

            if (read == -1)  break

            outputStream.write(fileReader, 0, read);

            fileSizeDownloaded += read

            RxLogTool.d(TAG+"file download: " + fileSizeDownloaded + " of " + fileSize)
        }

        outputStream.flush()
    }
}