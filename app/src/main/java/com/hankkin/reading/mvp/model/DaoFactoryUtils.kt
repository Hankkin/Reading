package com.hankkin.reading.mvp.model

import com.hankkin.reading.ui.home.search.SearchDao
import com.hankkin.reading.ui.home.search.SearchDaoContract
import com.hankkin.reading.ui.tools.translate.TranslateDao
import com.hankkin.reading.ui.tools.translate.TranslateDaoContract

/**
 * @author Hankkin
 * @date 2018/08/10
 */
object DaoFactoryUtils {

    fun <T> getDao(clazz: Class<T>): T = when (clazz) {

            SearchDaoContract::class.java -> SearchDao()
            TranslateDaoContract::class.java -> TranslateDao()

        else -> throw ClassNotFoundException("Not found : ${clazz.name}")
    } as T
}