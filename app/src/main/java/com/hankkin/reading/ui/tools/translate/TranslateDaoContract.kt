package com.hankkin.reading.ui.tools.translate

import com.hankkin.reading.domain.TranslateBean

/**
 * @author Hankkin
 * @date 2018/8/10
 */
interface TranslateDaoContract{

    fun insertTranslateHistory(translateBean: TranslateBean)
    fun queryTranslateHistoty(): MutableList<TranslateBean>?
    fun deleteTranslateHistory(id: Long)
}