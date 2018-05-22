package com.hankkin.reading.control

import com.hankkin.reading.domain.UserBean
import com.hankkin.reading.utils.LogUtils
import com.hankkin.reading.utils.SPUtils

object UserControl {
    const val USER = "user"

    private lateinit var user: UserBean

    fun setCurrentUser(userBean: UserBean) {
        this.user = userBean
    }

    fun getCurrentUser(): UserBean {
        return this.user
    }

    fun saveUserSp(userBean: UserBean) {
        SPUtils.saveObject(USER, userBean)
        LogUtils.d(USER + userBean.id)
    }

    fun isLogin(): Boolean {
        val user = SPUtils.getObject(USER, UserBean::class.java)
        if (user != null) return true
        return false
    }
}