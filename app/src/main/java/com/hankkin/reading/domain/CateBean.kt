package com.hankkin.reading.domain

import java.io.Serializable

data class CateBean(val id: Int,
                    val courseId: Int,
                    val name: String,
                    val parentChapterId: Int,
                    val visible: Int,
                    val children: MutableList<CateBean>) : Serializable