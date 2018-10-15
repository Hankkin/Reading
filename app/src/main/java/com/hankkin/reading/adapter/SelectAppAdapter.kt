package com.hankkin.reading.adapter

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.hankkin.reading.R
import com.hankkin.reading.adapter.base.BaseRecyclerViewAdapter
import com.hankkin.reading.adapter.base.BaseRecyclerViewHolder
import com.hankkin.reading.adapter.base.OnItemClickListener
import com.hankkin.reading.domain.AppInfo

/**
 * Created by ${Hankkin} on 2018/10/15.
 */
class SelectAppAdapter: BaseRecyclerViewAdapter<AppInfo>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent, R.layout.adapter_select_app,listener)
    }

    class ViewHolder(parent: ViewGroup?, layoutId: Int,val onItemClickListener: OnItemClickListener<AppInfo>) : BaseRecyclerViewHolder<AppInfo>(parent, layoutId) {

        val tvText by lazy { itemView.findViewById<TextView>(R.id.tv_adapter_select_app_name) }
        val ivIcon by lazy { itemView.findViewById<ImageView>(R.id.iv_adapter_select_app_icon) }
        val llRoot by lazy { itemView.findViewById<LinearLayout>(R.id.ll_adapter_select_app) }

        override fun onBindViewHolder(bean: AppInfo?, position: Int) {
            bean?.run {
                tvText.text = name
                ivIcon.setImageDrawable(icon)
                llRoot.setOnClickListener {
                    onItemClickListener.onClick(bean,position)
                }
            }
        }

    }
}