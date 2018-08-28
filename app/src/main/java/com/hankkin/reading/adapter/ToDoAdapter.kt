package com.hankkin.reading.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.hankkin.library.utils.DateUtils
import com.hankkin.reading.R
import com.hankkin.reading.adapter.base.BaseRecyclerViewAdapter
import com.hankkin.reading.adapter.base.BaseRecyclerViewHolder
import com.hankkin.reading.domain.ListBean

/**
 * @author Hankkin
 * @date 2018/8/26
 */
class ToDoAdapter : BaseRecyclerViewAdapter<ListBean>() {

    private val HOR = 0
    private val VER = 1

    companion object {
        val TYPE_ONLY = 0x0
        val TYPE_WORK = 0x1
        val TYPE_STUDY = 0x2
        val TYPE_LIFE = 0x3
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder<ListBean> {
        return HorViewHolder(parent, R.layout.adapter_todo_hor, viewType)
    }

    private class HorViewHolder(parent: ViewGroup, layoutId: Int, viewType: Int) : BaseRecyclerViewHolder<ListBean>(parent, layoutId) {
        private val tvTime by lazy { itemView.findViewById<TextView>(R.id.tv_adapter_todo_time) }
        private val llContainer by lazy { itemView.findViewById<LinearLayout>(R.id.ll_adapter_todo_container) }
        override fun onBindViewHolder(bean: ListBean, position: Int) {
            tvTime.text = DateUtils.milliseconds2String(bean.date)
            llContainer.removeAllViews()
            for (l in bean.todoList){
                val view = LayoutInflater.from(llContainer.context).inflate(R.layout.layout_todo_item,llContainer,false)
                val tvTitle = view.findViewById<TextView>(R.id.tv_adapter_todo_title)
                tvTitle.text = l.title
                val tvContent = view.findViewById<TextView>(R.id.tv_adapter_todo_content)
                tvContent.text = l.content
                val tvTime = view.findViewById<TextView>(R.id.tv_adapter_todo_create_time)
                tvTime.text = l.dateStr
                val tvType = view.findViewById<TextView>(R.id.tv_adapter_todo_type)
                tvType.text = when (l.type) {
                    TYPE_WORK -> "WORK"
                    TYPE_ONLY -> "ONLY"
                    TYPE_LIFE -> "LIFE"
                    TYPE_STUDY -> "STUDY"
                    else -> {
                        ""
                    }
                }
                llContainer.addView(view)
            }
        }
    }

}