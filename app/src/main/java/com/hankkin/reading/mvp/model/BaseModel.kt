package com.hankkin.reading.mvp.model

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.hankkin.reading.common.Constant
import com.hankkin.reading.greendao.DaoMaster
import com.hankkin.reading.greendao.DaoSession

open class BaseModel(val context: Context){
    var devOpenHelper: DaoMaster.DevOpenHelper
    var sqLiteDatabase: SQLiteDatabase
    var daoMaster: DaoMaster
    var daoSession: DaoSession

    init {
        this.devOpenHelper = DaoMaster.DevOpenHelper(context, Constant.DB.DB_NAME,null)
        this.sqLiteDatabase = devOpenHelper.writableDatabase
        this.daoMaster = DaoMaster(sqLiteDatabase)
        this.daoSession = daoMaster.newSession()
    }
}