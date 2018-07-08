package com.hankkin.reading.domain

/**
 * Created by huanghaijie on 2018/7/8.
 */
data class BannerBean(val id: Int,
                      val desc: String,
                      val imagePath: String,
                      val isVisible: Int,
                      val order: Int,
                      val title: String,
                      val url: String)