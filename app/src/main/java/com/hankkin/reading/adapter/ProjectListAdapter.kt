package com.hankkin.reading.adapter

import android.view.ViewGroup
import android.widget.TextView
import com.hankkin.reading.R
import com.hankkin.reading.adapter.base.BaseRecyclerViewAdapter
import com.hankkin.reading.adapter.base.BaseRecyclerViewHolder

/**
 * Created by huanghaijie on 2018/7/8.
 */
class ProjectListAdapter : BaseRecyclerViewAdapter<String>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder<*> {
        return ViewHolder(parent, R.layout.adapter_project_list)
    }


    private class ViewHolder(parent: ViewGroup, layoutId: Int) : BaseRecyclerViewHolder<String>(parent, layoutId) {

        val tvAuthor by lazy { itemView.findViewById<TextView>(R.id.tv_adapter_project_list) }

        override fun onBindViewHolder(bean: String, position: Int) {
            tvAuthor.text = bean
        }

    }
}