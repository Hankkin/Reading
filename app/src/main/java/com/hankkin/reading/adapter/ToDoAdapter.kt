package com.hankkin.reading.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hankkin.reading.R
import com.hankkin.reading.adapter.base.BaseRecyclerViewAdapter
import com.hankkin.reading.adapter.base.BaseRecyclerViewHolder
import com.hankkin.reading.domain.ListBean
import com.hankkin.reading.domain.ToDoListBean

/**
 * @author Hankkin
 * @date 2018/8/26
 */
class ToDoAdapter : BaseRecyclerViewAdapter<ListBean>() {

    private val HOR = 0
    private val VER = 1

    companion object {
        val TYPE_ONLY = 0x1
        val TYPE_WORK = 0x2
        val TYPE_STUDY = 0x3
        val TYPE_LIFE = 0x4
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder<ListBean> {
        return HorViewHolder(parent, R.layout.adapter_todo_hor, viewType)
    }


    private class HorViewHolder(parent: ViewGroup, layoutId: Int, viewType: Int) : BaseRecyclerViewHolder<ListBean>(parent, layoutId) {
        override fun onBindViewHolder(bean: ListBean, position: Int) {

        }
    }

}