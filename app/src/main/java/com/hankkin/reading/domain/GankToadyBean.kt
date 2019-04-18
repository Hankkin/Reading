package com.hankkin.reading.domain

import java.io.Serializable

/**
 * Created by Hankkin on 2018/11/8.
 */
data class GankToadyBean(val error: Boolean,
                         val category: MutableList<String>,
                         val results: TodayBean?) : Serializable

data class TodayBean(val Android: MutableList<ResultBean>?,
                     val App: MutableList<ResultBean>?,
                     val iOS: MutableList<ResultBean>?,
                     val 休息视频: MutableList<ResultBean>?,
                     val 前端: MutableList<ResultBean>?,
                     val 拓展资源: MutableList<ResultBean>?,
                     val 瞎推荐: MutableList<ResultBean>?,
                     val 福利: MutableList<ResultBean>?)