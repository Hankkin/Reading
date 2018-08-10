package com.hankkin.reading.ui.tools.translate

import com.hankkin.reading.domain.TranslateBean
import com.hankkin.reading.mvp.model.BaseDao

/**
 * Created by huanghaijie on 2018/8/10.
 */
class TranslateDao : BaseDao(),TranslateDaoContract{

    override fun insertTranslateHistory(translateBean: TranslateBean) {
        daoSession.translateBeanDao.insertOrReplace(translateBean)
    }

    override fun queryTranslateHistoty(): MutableList<TranslateBean> =
            daoSession.translateBeanDao.queryBuilder().list()

    override fun deleteTranslateHistory(id: Long) {
        daoSession.translateBeanDao.deleteByKey(id)
    }


}