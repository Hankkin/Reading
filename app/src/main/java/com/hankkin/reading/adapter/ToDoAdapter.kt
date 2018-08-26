package com.hankkin.reading.adapter

import android.view.ViewGroup
import com.hankkin.reading.R
import com.hankkin.reading.adapter.base.BaseRecyclerViewAdapter
import com.hankkin.reading.adapter.base.BaseRecyclerViewHolder
import com.hankkin.reading.domain.ToDoBean

/**
 * @author Hankkin
 * @date 2018/8/26
 */
class ToDoAdapter : BaseRecyclerViewAdapter<ToDoBean>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder<ToDoBean> {
        return ViewHolder(parent, R.layout.adapter_todo_hor)
    }

    private class ViewHolder(parent: ViewGroup, layoutId: Int) : BaseRecyclerViewHolder<ToDoBean>(parent, layoutId){
        override fun onBindViewHolder(bean: ToDoBean?, position: Int) {

        }

    }

}