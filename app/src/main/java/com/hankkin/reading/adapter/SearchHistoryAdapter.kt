package com.hankkin.reading.adapter

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.hankkin.reading.R
import com.hankkin.reading.adapter.base.BaseRecyclerViewAdapter
import com.hankkin.reading.adapter.base.BaseRecyclerViewHolder
import com.hankkin.reading.domain.HotBean
import com.hankkin.reading.event.EventMap
import com.hankkin.library.utils.RxBusTools

class SearchHistoryAdapter : BaseRecyclerViewAdapter<HotBean>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder<HotBean> {
        return ViewHolder(parent, R.layout.adapter_search_history_item)
    }

    class ViewHolder(parent: ViewGroup, layoutId: Int) : BaseRecyclerViewHolder<HotBean>(parent, layoutId){

        val tvName by lazy { itemView.findViewById<TextView>(R.id.tv_adapter_search_history) }
        val ivDelete by lazy { itemView.findViewById<ImageView>(R.id.iv_search_history_delete) }

        override fun onBindViewHolder(hotBean: HotBean?, position: Int) {

            hotBean?.run {
                tvName.text = name
            }
            ivDelete.setOnClickListener { RxBusTools.getDefault().post(EventMap.SearchHistoryDeleteEvent(position)) }

        }
    }


}