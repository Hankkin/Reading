package com.hankkin.reading.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.hankkin.library.utils.DateUtils
import com.hankkin.library.utils.RxBusTools
import com.hankkin.reading.R
import com.hankkin.reading.adapter.base.BaseRecyclerViewAdapter
import com.hankkin.reading.adapter.base.BaseRecyclerViewHolder
import com.hankkin.reading.domain.ListBean
import com.hankkin.reading.event.EventMap
import com.hankkin.reading.ui.todo.AddToDoActivity
import com.hankkin.reading.utils.ViewHelper

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

        val longClickItems = mutableListOf<String>(parent.context.resources.getString(R.string.todo_complete), parent.context.resources.getString(R.string.todo_update), parent.context.resources.getString(R.string.todo_delete))
        private val tvTime by lazy { itemView.findViewById<TextView>(R.id.tv_adapter_todo_time) }
        private val llContainer by lazy { itemView.findViewById<LinearLayout>(R.id.ll_adapter_todo_container) }

        @SuppressLint("WrongViewCast")
        override fun onBindViewHolder(bean: ListBean?, position: Int) {
            bean?.run {
                tvTime.text = DateUtils.milliseconds2String(date)
                llContainer.removeAllViews()
                for (l in todoList) {
                    val view = LayoutInflater.from(llContainer.context).inflate(R.layout.layout_todo_item, llContainer, false)
                            .apply {
                                findViewById<TextView>(R.id.tv_adapter_todo_title).text = l.title
                                findViewById<TextView>(R.id.tv_adapter_todo_content).text = l.content
                                findViewById<TextView>(R.id.tv_adapter_todo_create_time).text = l.dateStr
                                findViewById<TextView>(R.id.tv_adapter_todo_type).text = when (l.type) {
                                    TYPE_WORK -> "WORK"
                                    TYPE_ONLY -> "ONLY"
                                    TYPE_LIFE -> "LIFE"
                                    TYPE_STUDY -> "STUDY"
                                    else -> {
                                        throw Exception("Not found this type")
                                    }
                                }
                                setOnLongClickListener {
                                    ViewHelper.showListTitleDialog(it.context, "操作",
                                            longClickItems, MaterialDialog.ListCallback { dialog, itemView, which, text ->
                                        when (which) {
                                            0 -> {
                                                RxBusTools.getDefault().post(EventMap.CompleteToDoEvent(l))
                                            }
                                            1 -> {
                                                AddToDoActivity.intentTo(it.context, l)
                                            }
                                            2 -> {
                                                ViewHelper.showConfirmDialog(it.context!!,
                                                        it.context.resources.getString(R.string.todo_delete_hint),
                                                        MaterialDialog.SingleButtonCallback { dialog, which ->
                                                            RxBusTools.getDefault().post(EventMap.DeleteToDoEvent(l.id))
                                                        })
                                            }
                                        }
                                    })
                                    false
                                }
                                setOnClickListener { AddToDoActivity.intentTo(it.context, l) }
                            }
                    llContainer.addView(view)
                }
            }
        }
    }

}