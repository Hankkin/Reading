package com.hankkin.reading.adapter

import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.hankkin.reading.R
import com.hankkin.reading.adapter.base.BaseRecyclerViewAdapter
import com.hankkin.reading.adapter.base.BaseRecyclerViewHolder
import com.hankkin.reading.adapter.base.OnItemClickListener
import com.hankkin.reading.adapter.base.OnItemLongClickListener
import com.hankkin.reading.domain.TranslateBean

/**
 * @author Hankkin
 * @date 2018/8/10
 */
class TranHistoryAdapter : BaseRecyclerViewAdapter<TranslateBean>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder<TranslateBean> {
        return ViewHolder(parent, R.layout.adapter_translate_search_history_item, listener, onItemLongClickListener)
    }

    private class ViewHolder(parent: ViewGroup, layoutId: Int, val onItemClickListener: OnItemClickListener<TranslateBean>, val onItemLongClickListener: OnItemLongClickListener<TranslateBean>) : BaseRecyclerViewHolder<TranslateBean>(parent, layoutId) {

        val tvKey by lazy { itemView.findViewById<TextView>(R.id.tv_adapter_translae_history_key) }
        val tvContent by lazy { itemView.findViewById<TextView>(R.id.tv_adapter_translae_history_content) }
        val ll by lazy { itemView.findViewById<LinearLayout>(R.id.ll_adapter_translate) }


        override fun onBindViewHolder(bean: TranslateBean?, position: Int) {
            bean?.run {
                tvKey.text = query
                if (explains != null){
                    tvContent.text = explains.toString()
                }
                else{
                    tvContent.text = "暂无释义..."
                }
                ll.setOnLongClickListener {
                    onItemLongClickListener.onLongClick(this, position)
                    false
                }
                ll.setOnClickListener { onItemClickListener.onClick(this, position) }
            }

        }

    }
}