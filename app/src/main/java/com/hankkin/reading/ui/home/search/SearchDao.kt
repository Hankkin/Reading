package com.hankkin.reading.ui.home.search

import com.hankkin.reading.domain.HotBean
import com.hankkin.reading.greendao.HotBeanDao
import com.hankkin.reading.mvp.model.BaseDao

class SearchDao : BaseDao(),SearchDaoContract{

    override fun insertHot(hotBean: HotBean) {
        hotBeanDao.insert()
    }

    override fun query() {

    }

    var hotBeanDao: HotBeanDao

    init {
        this.hotBeanDao = daoSession.hotBeanDao
    }


}
