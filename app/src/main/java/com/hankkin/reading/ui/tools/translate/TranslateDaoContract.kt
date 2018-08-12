package com.hankkin.reading.ui.tools.translate

import com.hankkin.reading.domain.TranslateBean
import com.hankkin.reading.domain.WordNoteBean

/**
 * @author Hankkin
 * @date 2018/8/10
 */
interface TranslateDaoContract{

    fun insertTranslateHistory(translateBean: TranslateBean)
    fun queryTranslateHistoty(): MutableList<TranslateBean>?
    fun deleteTranslateHistory(id: Long)
}