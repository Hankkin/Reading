package com.hankkin.reading.dao

import com.hankkin.reading.ui.home.search.SearchDao
import com.hankkin.reading.ui.home.search.SearchDaoContract
import com.hankkin.reading.ui.tools.acount.AccountDao
import com.hankkin.reading.ui.tools.acount.AccountDaoContract
import com.hankkin.reading.ui.tools.translate.TranslateDao
import com.hankkin.reading.ui.tools.translate.TranslateDaoContract
import com.hankkin.reading.ui.tools.wordnote.WordNoteDao
import com.hankkin.reading.ui.tools.wordnote.WordNoteDaoContract

/**
 * @author Hankkin
 * @date 2018/08/10
 */
object DaoFactoryUtils {

    fun <T> getDao(clazz: Class<T>): T = when (clazz) {

        SearchDaoContract::class.java -> SearchDao()
        TranslateDaoContract::class.java -> TranslateDao()
        WordNoteDaoContract::class.java -> WordNoteDao()
        AccountDaoContract::class.java -> AccountDao()

        else -> throw ClassNotFoundException("Not found : ${clazz.name}")
    } as T
}