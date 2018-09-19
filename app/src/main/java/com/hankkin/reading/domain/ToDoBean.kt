package com.hankkin.reading.domain

import java.io.Serializable

/**
 * @author Hankkin
 * @date 2018/8/26
 */
data class ToDoBean(val doneList: MutableList<ListBean>,val todoList: MutableList<ListBean>,val type: Int)

data class ListBean(val date: Long,val todoList: MutableList<ToDoListBean>)
data class ToDoListBean(val completeDate: Long,
                        val completeDateStr: String,
                        val content: String,
                        val title: String,
                        val date: Long,
                        val dateStr: String,
                        val id: Int,
                        val status: Int,
                        val type: Int,
                        val userId: Int) : Serializable

data class DoneBean(val curPage: Int,
                    val offset: Int,
                    val over: Boolean,
                    val pageCount: Int,
                    val size: Int,
                    val total: Int,
                    val datas: MutableList<ToDoListBean>)