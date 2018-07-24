package com.hankkin.reading.ui.home.search

import android.content.Context
import com.hankkin.reading.greendao.HotBeanDao
import com.hankkin.reading.mvp.model.BaseModel

class SearchModel(context: Context) : BaseModel(context){

    var hotBeanDao: HotBeanDao

    init {
        this.hotBeanDao = daoSession.hotBeanDao
    }

}
