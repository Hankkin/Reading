package com.hankkin.reading.ui.tools.translate

import com.hankkin.reading.domain.TranslateBean
import com.hankkin.reading.dao.BaseDao

/**
 * Created by huanghaijie on 2018/8/10.
 */
class TranslateDao : BaseDao(), TranslateDaoContract {
    override fun insertTranslates(data: MutableList<TranslateBean>) {
        daoSession.translateBeanDao.insertOrReplaceInTx(data)
    }

    /**
     * 添加搜索单词历史
     */
    override fun insertTranslateHistory(translateBean: TranslateBean) {
        daoSession.translateBeanDao.insertOrReplace(translateBean)
    }

    /**
     * 查询搜索单词历史
     */
    override fun queryTranslateHistoty(): MutableList<TranslateBean> =
            daoSession.translateBeanDao.queryBuilder().list()

    /**
     * 删除搜索历史
     */
    override fun deleteTranslateHistory(id: Long) {
        daoSession.translateBeanDao.deleteByKey(id)
    }


}