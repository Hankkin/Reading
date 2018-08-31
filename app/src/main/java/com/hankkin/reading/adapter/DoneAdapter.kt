package com.hankkin.reading.adapter

import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.hankkin.library.utils.RxBusTools
import com.hankkin.reading.R
import com.hankkin.reading.adapter.base.BaseRecyclerViewAdapter
import com.hankkin.reading.adapter.base.BaseRecyclerViewHolder
import com.hankkin.reading.adapter.base.OnItemLongClickListener
import com.hankkin.reading.domain.AccountBean
import com.hankkin.reading.domain.ToDoListBean
import com.hankkin.reading.event.EventMap
import com.hankkin.reading.utils.ViewHelper

/**
 * @author Hankkin
 * @date 2018/8/26
 */
class DoneAdapter : BaseRecyclerViewAdapter<ToDoListBean>() {


    companion object {
        val TYPE_ONLY = 0x0
        val TYPE_WORK = 0x1
        val TYPE_STUDY = 0x2
        val TYPE_LIFE = 0x3
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder<ToDoListBean> {
        return HorViewHolder(parent, R.layout.layout_done_item, viewType,onItemLongClickListener)
    }

    private class HorViewHolder(parent: ViewGroup, layoutId: Int, viewType: Int,val onItemLongClickListener: OnItemLongClickListener<ToDoListBean>) : BaseRecyclerViewHolder<ToDoListBean>(parent, layoutId) {
        private val tvTitle by lazy { itemView.findViewById<TextView>(R.id.tv_adapter_todo_title) }
        private val tvContent by lazy { itemView.findViewById<TextView>(R.id.tv_adapter_todo_content) }
        private val tvTime by lazy { itemView.findViewById<TextView>(R.id.tv_adapter_todo_complete_time) }
        private val tvType by lazy { itemView.findViewById<TextView>(R.id.tv_adapter_todo_type) }
        private val llDone by lazy { itemView.findViewById<LinearLayout>(R.id.ll_adapter_done) }

        override fun onBindViewHolder(bean: ToDoListBean?, position: Int) {
            bean?.run {
                tvTitle.text = title
                tvContent.text = content
                tvTime.text = "完成时间：$completeDateStr"
                tvType.text = when (type) {
                    TYPE_WORK -> "WORK"
                    TYPE_ONLY -> "ONLY"
                    TYPE_LIFE -> "LIFE"
                    TYPE_STUDY -> "STUDY"
                    else -> {
                        ""
                    }
                }
            }

            llDone.setOnLongClickListener {
                onItemLongClickListener.onLongClick(bean,position)
                false
            }
        }
    }

}