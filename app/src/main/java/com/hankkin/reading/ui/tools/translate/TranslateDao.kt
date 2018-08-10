package com.hankkin.reading.ui.tools.translate

import com.hankkin.reading.domain.TranslateBean
import com.hankkin.reading.mvp.model.BaseDao

/**
 * Created by huanghaijie on 2018/8/10.
 */
class TranslateDao : BaseDao(){

    fun saveSearchHistory(translateBean: TranslateBean){
        daoSession.translateBeanDao.save(translateBean)
    }
}