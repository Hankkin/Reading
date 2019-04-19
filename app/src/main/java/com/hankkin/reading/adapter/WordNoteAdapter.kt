package com.hankkin.reading.adapter

import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.hankkin.reading.R
import com.hankkin.reading.adapter.base.BaseRecyclerViewAdapter
import com.hankkin.reading.adapter.base.BaseRecyclerViewHolder
import com.hankkin.reading.domain.WordNoteBean
import com.hankkin.reading.adapter.base.OnItemClickListener
import com.hankkin.reading.adapter.base.OnItemLongClickListener

/**
 * @author Hankkin
 * @date 2018/8/12
 */
class WordNoteAdapter : BaseRecyclerViewAdapter<WordNoteBean>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder<WordNoteBean> {
        return ViewHolder(parent, R.layout.adapter_word_note, listener, onItemLongClickListener)
    }

    private class ViewHolder(parent: ViewGroup, layoutId: Int, val onItemClickListener: OnItemClickListener<WordNoteBean>, val onItemLongClickListener: OnItemLongClickListener<WordNoteBean>) : BaseRecyclerViewHolder<WordNoteBean>(parent, layoutId) {

        private val tvKey by lazy { itemView.findViewById<TextView>(R.id.tv_adapter_word_note_key) }
        private val tvContent by lazy { itemView.findViewById<TextView>(R.id.tv_adapter_word_note_content) }
        private val ivImport by lazy { itemView.findViewById<ImageView>(R.id.iv_adapter_word_note_important) }

        override fun onBindViewHolder(bean: WordNoteBean?, position: Int) {
            bean?.run {
                tvKey.text = translateBean.query
                tvContent.text = if(translateBean.explains == null) "..." else translateBean.explains.toString()
                itemView.setOnClickListener { onItemClickListener.onClick(this, position) }
                itemView.setOnLongClickListener {
                    onItemLongClickListener.onLongClick(this, position)
                    false
                }
                ivImport.visibility = if (isEmphasis) View.VISIBLE else View.GONE
            }

        }

    }

}