package com.hankkin.reading.domain

/**
 * Created by huanghaijie on 2018/7/6.
 */
data class NewsListBean(val id: Int,
                        val author: String,
                        val pubDate: String,
                        val title: String,
                        val authorid: Long,
                        val commentCount: Int,
                        val type: Int)