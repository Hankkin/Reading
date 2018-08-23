package com.hankkin.reading.dao

import com.hankkin.reading.EApplication
import com.hankkin.reading.greendao.DaoSession

open class BaseDao{
    var daoSession: DaoSession
    init {
        this.daoSession = EApplication.instance().getDaoSession()
    }
}