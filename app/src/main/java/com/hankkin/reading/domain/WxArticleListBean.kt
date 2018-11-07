package com.hankkin.reading.domain

/**
 * Created by Hankkin on 2018/11/6.
 */

data class WxArticleListBean(val curPage: Int,
                       val offset: Int,
                       val over: Boolean,
                       val pageCount: Int,
                       val size: Int,
                       val total: Int,
                       val datas: MutableList<WxArticleListItemBean>?)

data class WxArticleListItemBean(val apkLink: String,
                             val author: String,
                             val chapterId: Int,
                             val chapterName: String,
                             val collect: Boolean,
                             val courseId: Int,
                             val desc: String,
                             val envelopePic: String,
                             val fresh: Boolean,
                             val id: Int,
                             val link: String,
                             val niceDate: String,
                             val origin: String,
                             val projectLink: String,
                             val publishTime: Long,
                             val superChapterId: Int,
                             val title: String,
                             val type: Int,
                             val tags: MutableList<TagBean>?,
                             val userId: Int,
                             val visible: Int,
                             val zan: Int)