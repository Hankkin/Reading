package com.hankkin.reading.ui.tools.translate

import com.hankkin.reading.domain.TranslateBean
import com.hankkin.reading.mvp.model.BaseDaoContract

/**
 * @author Hankkin
 * @date 2018/8/10
 */
interface TranslateDaoContract : BaseDaoContract{

    fun insertTranslateHistory(translateBean: TranslateBean)
    fun queryTranslateHistoty(): MutableList<TranslateBean>?
    fun deleteTranslateHistory(id: Long)
}