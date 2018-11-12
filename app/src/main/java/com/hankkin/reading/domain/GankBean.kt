package com.hankkin.reading.domain

import java.io.Serializable

/**
 * Created by Hankkin on 2018/11/8.
 */
data class GankBean(val error: Boolean,
                    val results: MutableList<ResultBean>)
data class ResultBean(val _id: String,
                      val createdAt: String,
                      val desc: String,
                      var images: MutableList<String>?,
                      val publishedAt: String,
                      val source: String,
                      val type: String,
                      val url: String,
                      val used: Boolean,
                      val who: String) : Serializable