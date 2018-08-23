package com.hankkin.reading.utils

import android.content.Context
import android.os.Environment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hankkin.library.utils.AppUtils
import com.hankkin.library.utils.FileUtils
import com.hankkin.library.utils.SPUtils
import com.hankkin.reading.common.Constant
import com.hankkin.reading.domain.AccountBean
import com.hankkin.reading.domain.TranslateBean
import com.hankkin.reading.domain.WordNoteBean
import com.hankkin.reading.dao.DaoFactory
import com.hankkin.reading.ui.tools.acount.AccountDaoContract
import com.hankkin.reading.ui.tools.translate.TranslateDaoContract
import com.hankkin.reading.ui.tools.wordnote.WordNoteDaoContract
import java.io.File
import java.io.IOException

/**
 * @author Hankkin
 * @date 2018/8/19
 */
object DBUtils {

    fun deleteData(context: Context){
        FileUtils.delAllFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + AppUtils.getAppName(context) + "/" + "db")
    }

    /**
     * 是否需要还原
     */
    fun isNeedSync(context: Context): Boolean {
        if (getUpdateTime(context) != -1L &&
                getUpdateTime(context) >= SPUtils.getLong(Constant.SP_KEY.DB_UPDATE_TIME)) {
            return true
        }
        return false
    }

    /**
     * 首页主动弹还原
     */
    fun isAutoSync(context: Context): Boolean {
        if (SPUtils.getLong(Constant.SP_KEY.DB_UPDATE_TIME) == -1L && getUpdateTime(context) != -1L) {
            return true
        }
        return false
    }

    /**
     * 是否需要备份数据
     */
    fun isNeedBackup(context: Context): Boolean {
        val wordNotesTime = getFileInTxt(Constant.SP_KEY.WORDNOTE, context)
        val accountStrTime = getFileInTxt(Constant.SP_KEY.ACCOUNT, context)
        if ((wordNotesTime != null && !wordNotesTime.isNullOrEmpty()) || (accountStrTime != null && !accountStrTime.isNullOrEmpty())) {
            var time = -1L
            if (wordNotesTime != null) {
                time = wordNotesTime.substring(wordNotesTime.indexOf("<") + 1, wordNotesTime.indexOf(">")).toLong()
            }
            if (accountStrTime != null) {
                time = accountStrTime.substring(accountStrTime.indexOf("<") + 1, accountStrTime.indexOf(">")).toLong()
            }
            return time < SPUtils.getLong(Constant.SP_KEY.DB_UPDATE_TIME)
        }
        else{
            return true
        }
        return false
    }

    @Throws(IOException::class)
    fun saveDBData(context: Context) {
        val gson = Gson()
        val wordNotes = DaoFactory.getProtocol(WordNoteDaoContract::class.java).queryWordNotes()
        if (wordNotes != null && wordNotes.size > 0) {
            var wordNotesStr = "<${SPUtils.getLong(Constant.SP_KEY.DB_UPDATE_TIME)}>" + gson.toJson(wordNotes)
            writeTxtToFile(wordNotesStr, Constant.SP_KEY.WORDNOTE, context)
        }
        val accounts = DaoFactory.getProtocol(AccountDaoContract::class.java).queryAllAccount()
        if (accounts.size > 0) {
            var accountsStr = "<${SPUtils.getLong(Constant.SP_KEY.DB_UPDATE_TIME)}>" + gson.toJson(accounts)
            writeTxtToFile(accountsStr, Constant.SP_KEY.ACCOUNT, context)
        }
    }


    fun loadDBData(context: Context) {
        val gson = Gson()
        var wordNotes = getFileInTxt(Constant.SP_KEY.WORDNOTE, context)
        if (wordNotes != null) {
            wordNotes = wordNotes.substringAfter(">")
        }
        var accountStr = getFileInTxt(Constant.SP_KEY.ACCOUNT, context)
        if (accountStr != null) {
            accountStr = accountStr.substringAfter(">")
        }
        if (wordNotes != null && wordNotes.isNotEmpty()) {
            val wordNoteData = gson.fromJson<WordNoteBean>(wordNotes, object : TypeToken<MutableList<WordNoteBean>>() {}.type) as MutableList<WordNoteBean>
            var translates = mutableListOf<TranslateBean>()
            for (w in wordNoteData){
                translates.add(w.myTranslate)
            }
            DaoFactory.getProtocol(TranslateDaoContract::class.java).insertTranslates(translates)
            DaoFactory.getProtocol(WordNoteDaoContract::class.java).insertWordNotes(wordNoteData)
        }
        if (accountStr != null && accountStr.isNotEmpty()) {
            val accountData = gson.fromJson<AccountBean>(accountStr, object : TypeToken<List<AccountBean>>() {}.type) as MutableList<AccountBean>
            DaoFactory.getProtocol(AccountDaoContract::class.java).insertAccounts(accountData)
        }
    }

    @Throws(IOException::class)
    private fun writeTxtToFile(str: String, path: String, context: Context) {
        val file = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + AppUtils.getAppName(context) + "/" + "db"
        if (!FileUtils.fileExists(file)) {
            File(file).mkdir()
        }
        val txt = file + "/" + path + ".txt"
        if (!FileUtils.fileExists(txt)) {
            File(txt).createNewFile()
        } else {
            File(txt).delete()
        }
        FileUtils.saveFileUTF8(txt, str, true)
    }

    @Throws(IOException::class)
    private fun getFileInTxt(name: String, context: Context): String? {
        val file = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + AppUtils.getAppName(context) + "/" + "db"
        if (FileUtils.fileExists(file)) {
            var newPath = file + "/" + name + ".txt"
            if (FileUtils.fileExists(newPath)) {
                return FileUtils.getFileUTF8(newPath)
            }
        }
        return null
    }

    private fun getUpdateTime(context: Context): Long {
        val wordNotesTime = getFileInTxt(Constant.SP_KEY.WORDNOTE, context)
        val accountStrTime = getFileInTxt(Constant.SP_KEY.ACCOUNT, context)
        var time = -1L
        if (wordNotesTime != null) {
            time = wordNotesTime.substring(wordNotesTime.indexOf("<") + 1, wordNotesTime.indexOf(">")).toLong()
        }
        if (accountStrTime != null) {
            time = accountStrTime.substring(accountStrTime.indexOf("<") + 1, accountStrTime.indexOf(">")).toLong()
        }
        return time
    }
}