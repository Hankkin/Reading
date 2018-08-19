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
import com.hankkin.reading.domain.WordNoteBean
import com.hankkin.reading.mvp.model.DaoFactory
import com.hankkin.reading.ui.tools.acount.AccountDaoContract
import com.hankkin.reading.ui.tools.wordnote.WordNoteDaoContract
import java.io.File

/**
 * @author Hankkin
 * @date 2018/8/19
 */
object DBUtils{
    fun saveDBData(context: Context){
        val gson = Gson()
        val wordNotes = DaoFactory.getProtocol(WordNoteDaoContract::class.java).queryWordNotes()
        var wordNotesStr = gson.toJson(wordNotes)
        writeTxtToFile(wordNotesStr,Constant.SP_KEY.WORDNOTE,context)
        val accounts = DaoFactory.getProtocol(AccountDaoContract::class.java).queryAllAccount()
        var accountsStr = gson.toJson(accounts)
        writeTxtToFile(accountsStr,Constant.SP_KEY.ACCOUNT,context)
    }

    fun loadDBData(context: Context){
        val gson = Gson()
        val wordNotes = getFileInTxt(Constant.SP_KEY.WORDNOTE,context)
        val accountStr = getFileInTxt(Constant.SP_KEY.ACCOUNT,context)
        if (wordNotes!!.isNotEmpty()){
            val wordNoteData = gson.fromJson<WordNoteBean>(wordNotes,object : TypeToken<MutableList<WordNoteBean>>() {}.type) as MutableList<WordNoteBean>
            DaoFactory.getProtocol(WordNoteDaoContract::class.java).insertWordNotes(wordNoteData)
        }
        if (accountStr!!.isNotEmpty()){
            val accountData = gson.fromJson<AccountBean>(accountStr,object : TypeToken<List<AccountBean>>() {}.type) as MutableList<AccountBean>
            DaoFactory.getProtocol(AccountDaoContract::class.java).insertAccounts(accountData)
        }
    }

    private fun writeTxtToFile(str: String,path:String,context: Context){
        val file = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+ AppUtils.getAppName(context)+"/"+"db"
        if (!FileUtils.fileExists(file)){
            File(file).mkdir()
        }
        val txt = file+"/"+path+".txt"
        if (!FileUtils.fileExists(txt)){
            File(txt).createNewFile()
        }
        else{
            File(txt).delete()
        }
        FileUtils.saveFileUTF8(txt,str,true)
    }

    private fun getFileInTxt(name: String,context: Context): String? {
        val file = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+ AppUtils.getAppName(context)+"/"+"db"
        if (FileUtils.fileExists(file)){
            var newPath = file+"/"+name+".txt"
            if (FileUtils.fileExists(newPath)){
                return FileUtils.getFileUTF8(newPath)
            }
        }
        return null
    }
}