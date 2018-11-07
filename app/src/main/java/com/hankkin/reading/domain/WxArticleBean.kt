package com.hankkin.reading.domain

import java.io.Serializable

/**
 * Created by Hankkin on 2018/11/6.
 */
data class WxArticleBean(val courseId: Int,
                         val id: Int,
                         val name: String,
                         val order: Long,
                         val parentChapterId: Int,
                         val userControlSetTop: Boolean,
                         val visible: Int) : Serializable