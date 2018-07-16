package com.hankkin.reading.domain

/**
 * Created by huanghaijie on 2018/7/14.
 */
data class CollectArticleBean(val curPage: Int,
                              val datas: MutableList<CollectArticleBean>,
                              val offset: Int,
                              val over: Boolean,
                              val pageCount: Int,
                              val size: Int,
                              val total: Int)