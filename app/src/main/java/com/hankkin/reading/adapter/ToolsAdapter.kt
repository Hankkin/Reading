package com.hankkin.reading.adapter

import android.view.ViewGroup
import android.widget.TextView
import com.bilibili.magicasakura.widgets.TintImageView
import com.hankkin.reading.R
import com.hankkin.reading.adapter.base.BaseRecyclerViewAdapter
import com.hankkin.reading.adapter.base.BaseRecyclerViewHolder
import com.hankkin.reading.adapter.base.OnItemClickListener
import com.hankkin.reading.domain.ToolsBean
import com.hankkin.reading.view.ClickImageView

class ToolsAdapter : BaseRecyclerViewAdapter<ToolsBean>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder<ToolsBean> {
        return ViewHolder(parent, R.layout.adapter_tools_item,listener)
    }


    private class ViewHolder(parent: ViewGroup, layoutId: Int,val listener: OnItemClickListener<ToolsBean>) : BaseRecyclerViewHolder<ToolsBean>(parent, layoutId) {

        val tv by lazy { itemView.findViewById<TextView>(R.id.tv_adapter_tools) }
        val iv by lazy { itemView.findViewById<TintImageView>(R.id.iv_adapter_tools) }

        override fun onBindViewHolder(bean: ToolsBean, position: Int) {
            tv.text = bean.title
            iv.setImageResource(bean.res)
            iv.tint()
            iv.setOnClickListener { listener.onClick(bean,position) }
        }
    }

}