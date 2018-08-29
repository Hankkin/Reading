package com.hankkin.reading.domain

import java.io.Serializable

/**
 * Created by huanghaijie on 2018/7/8.
 */
data class ArticleBean(val curPage: Int,
                       val offset: Int,
                       val over: Boolean,
                       val pageCount: Int,
                       val size: Int,
                       val total: Int,
                       val datas: MutableList<ArticleDetailBean>?)

data class ArticleDetailBean(val apkLink: String,
                             val author: String,
                             val chapterId: Int,
                             val chapterName: String,
                             val collect: Boolean,
                             val courseId: Int,
                             val desc: String,
                             val envelopePic: String,
                             val fresh: Boolean,
                             val id: Int?,
                             val link: String,
                             val niceDate: String,
                             val origin: String,
                             val projectLink: String,
                             val publishTime: Long,
                             val superChapterId: Int,
                             val superChapterName: String?,
                             val tags: MutableList<TagBean>?,
                             val title: String,
                             val type: Int,
                             val userId: Int,
                             val visible: Int,
                             val zan: Int,
                             val name: String,
                             val originId: Int) : Serializable

data class TagBean(val name: String,
                   val url: String)