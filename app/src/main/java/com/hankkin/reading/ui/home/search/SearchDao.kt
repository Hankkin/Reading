package com.hankkin.reading.ui.home.search

import android.content.Context
import com.hankkin.reading.domain.HotBean
import com.hankkin.reading.greendao.HotBeanDao
import com.hankkin.reading.mvp.model.BaseDao

class SearchDao(context: Context) : BaseDao(context),SearchDaoContract{

    override fun insertHot(hotBean: HotBean) {

    }

    override fun query() {
    }

    var hotBeanDao: HotBeanDao

    init {
        this.hotBeanDao = daoSession.hotBeanDao
    }


}
