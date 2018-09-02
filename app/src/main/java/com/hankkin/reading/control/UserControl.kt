package com.hankkin.reading.control

import com.hankkin.library.http.cookies.CookiesManager
import com.hankkin.library.utils.LogUtils
import com.hankkin.library.utils.SPUtils
import com.hankkin.reading.domain.UserBean

object UserControl {
    const val USER = "user"
    const val TOKEN = "access_token"

    private var user: UserBean? = null

    init {

    }

    fun setCurrentUser(userBean: UserBean) {
        this.user = userBean
        saveUserSp(userBean)
    }

    fun getCurrentUser(): UserBean? {
        if (this.user == null){
            this.user = SPUtils.getObject(USER,UserBean::class.java) as UserBean
        }
        return this.user
    }

    private fun saveUserSp(userBean: UserBean) {
        SPUtils.saveObject(USER, userBean)
        LogUtils.d(USER + userBean.id)
    }

    fun isLogin(): Boolean {
        val user = SPUtils.getObject(USER, UserBean::class.java)
        if (user != null) return true
        return false
    }

    fun logout(){
        this.user = null
        SPUtils.clear()
        CookiesManager.clearAllCookies()
    }
}