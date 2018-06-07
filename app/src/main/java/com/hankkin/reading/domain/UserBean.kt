package com.hankkin.reading.domain

/**
 * Created by huanghaijie on 2018/5/22.
 */
data class UserBean(val id: Int, val password: String, val last_login: String, val is_superuser: Int,
                    val username: String, val first_name: String, val last_name: String, val email: String,
                    val is_staff: Int, val is_active: Int, val date_joined: String, val profileBean: ProfileBean)

data class ProfileBean(val email_valid: Boolean)