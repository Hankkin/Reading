package com.hankkin.reading.ui.home.search

import com.hankkin.reading.domain.HotBean
import com.hankkin.reading.dao.BaseDao

class SearchDao : BaseDao(), SearchDaoContract {

    override fun query(): MutableList<HotBean> =
            daoSession.hotBeanDao.queryBuilder().list()

    override fun delete(id: Long) {
        daoSession.hotBeanDao.deleteByKey(id)
    }

    override fun insertHot(hotBean: HotBean) {
        daoSession.hotBeanDao.insertOrReplace(hotBean)
    }


}
