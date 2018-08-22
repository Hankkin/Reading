package com.hankkin.library.utils

import android.os.Environment
import android.text.TextUtils
import java.io.*
import java.math.BigDecimal
import java.nio.charset.Charset


/**
 * Created by huanghaijie on 2018/8/17.
 */
object FileUtils{
    const val TAG = "fileutils"

    fun initSd(apkName: String) {
        val file = File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+apkName)
        if (!file.exists()) file.mkdirs()
        LogUtils.d("$TAG 初始化文件夹成功 ${file.absolutePath}")
    }

    /**
     * 文件或者文件夹是否存在.
     */
    fun fileExists(filePath: String): Boolean {
        val file = File(filePath)
        return file.exists()
    }

    /**
     * 文件复制.
     */
    fun copy(oldPath: String, newPath: String): Boolean {
        try {
            var bytesum = 0
            var byteread = 0
            val oldfile = File(oldPath)
            val newFile = File(newPath)
            if (!newFile.exists()) {
                newFile.createNewFile()
            }
            if (oldfile.exists()) { //文件存在时
                val inStream = FileInputStream(oldPath) //读入原文件
                val fs = FileOutputStream(newPath)
                val buffer = ByteArray(1444)
                val length: Int
                while (inStream.read(buffer) != -1) {
                    byteread = inStream.read(buffer)
                    bytesum += byteread //字节数 文件大小
                    println(bytesum)
                    fs.write(buffer, 0, byteread)
                }
                inStream.close()
            }
        } catch (e: Exception) {
            println("复制单个文件操作出错")
            e.printStackTrace()
        }



        return true
    }

    /**
     * 复制整个文件夹内.
     *
     * @param oldPath string 原文件路径如：c:/fqf.
     * @param newPath string 复制后路径如：f:/fqf/ff.
     */
    fun copyFolder(oldPath: String, newPath: String) {
        try {
            File(newPath).mkdirs() // 如果文件夹不存在 则建立新文件夹
            val a = File(oldPath)
            val file = a.list()
            var temp: File? = null
            for (i in file!!.indices) {
                if (oldPath.endsWith(File.separator)) {
                    temp = File(oldPath + file[i])
                } else {
                    temp = File(oldPath + File.separator + file[i])
                }

                if (temp.isFile) {
                    val input = FileInputStream(temp)
                    val output = FileOutputStream(newPath + "/" + temp.name.toString())
                    val b = ByteArray(1024 * 5)
                    var len: Int
                    while (input.read(b) != -1) {
                        len = input.read(b)
                        output.write(b, 0, len)
                    }
                    output.flush()
                    output.close()
                    input.close()
                }
                if (temp.isDirectory) {// 如果是子文件夹
                    copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i])
                }
            }
        } catch (e: NullPointerException) {
        } catch (e: Exception) {
        }

    }

    /**
     * 获取指定文件夹内所有文件大小的和
     *
     * @param file file
     * @return size
     * @throws Exception
     */
    @Throws(Exception::class)
    fun getFolderSize(file: File): Long {
        var size: Long = 0
        try {
            val fileList = file.listFiles()
            for (aFileList in fileList) {
                if (aFileList.isDirectory()) {
                    size = size + getFolderSize(aFileList)
                } else {
                    size = size + aFileList.length()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return size
    }

    /**
     * 删除指定目录下的文件，这里用于缓存的删除
     *
     * @param filePath       filePath
     * @param deleteThisPath deleteThisPath
     */
    fun deleteFolderFile(filePath: String, deleteThisPath: Boolean) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                val file = File(filePath)
                if (file.isDirectory) {
                    val files = file.listFiles()
                    for (file1 in files!!) {
                        deleteFolderFile(file1.absolutePath, true)
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory) {
                        file.delete()
                    } else {
                        if (file.listFiles()!!.size == 0) {
                            file.delete()
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    /**
     * 格式化单位
     *
     * @param size size
     * @return size
     */
    fun getFormatSize(size: Double): String {

        val kiloByte = size / 1024
        if (kiloByte < 1) {
            return "0KB"
        }

        val megaByte = kiloByte / 1024
        if (megaByte < 1) {
            val result1 = BigDecimal(java.lang.Double.toString(kiloByte))
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB"
        }

        val gigaByte = megaByte / 1024
        if (gigaByte < 1) {
            val result2 = BigDecimal(java.lang.Double.toString(megaByte))
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB"
        }

        val teraBytes = gigaByte / 1024
        if (teraBytes < 1) {
            val result3 = BigDecimal(java.lang.Double.toString(gigaByte))
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB"
        }
        val result4 = BigDecimal(teraBytes)

        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB"
    }

    /**
     * 用UTF8保存一个文件.
     */
    @Throws(IOException::class)
    fun saveFileUTF8(path: String, content: String, append: Boolean?) {
        val fos = FileOutputStream(path, append!!)
        val out = OutputStreamWriter(fos, "UTF-8")
        out.write(content)
        out.flush()
        out.close()
        fos.flush()
        fos.close()
    }

    /**
     * 用UTF8读取一个文件.
     */
    fun getFileUTF8(path: String): String {
        var result = ""
        var fin: InputStream? = null
        try {
            fin = FileInputStream(path)
            val length = fin.available()
            val buffer = ByteArray(length)
            fin.read(buffer)
            fin.close()
            result = String(buffer, Charset.forName("UTF-8"))
        } catch (e: Exception) {
        }

        return result
    }

    /**
     * 删除指定文件夹下所有文件, 不保留文件夹.
     */
    fun delAllFile(path: String): Boolean {
        val flag = false
        val file = File(path)
        if (!file.exists()) {
            return flag
        }
        if (file.isFile) {
            file.delete()
            return true
        }
        val files = file.listFiles()
        for (i in files.indices) {
            val exeFile = files[i]
            if (exeFile.isDirectory) {
                delAllFile(exeFile.absolutePath)
            } else {
                exeFile.delete()
            }
        }
        file.delete()

        return flag
    }
}