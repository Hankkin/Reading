package com.hankkin.reading.domain

/**
 * @author Hankkin
 * @date 2018/8/26
 */
data class ToDoBean(val errorCode: Int,val errorMsg: String,val data: DataBean)

data class DataBean(val doneList: ListBean,val todoList: ListBean,val type: Int)

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
                        val userId: Int)