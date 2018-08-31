package com.hankkin.reading.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.hankkin.reading.R
import com.hankkin.reading.adapter.base.BaseRecyclerViewAdapter
import com.hankkin.reading.adapter.base.BaseRecyclerViewHolder
import com.hankkin.reading.adapter.base.OnItemClickListener
import com.hankkin.reading.domain.ThemeItemBean

/**
 * @author Hankkin
 * @date 2018/8/23
 */
class ThemePicAdapter : BaseRecyclerViewAdapter<ThemeItemBean>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder<ThemeItemBean> {
        return ViewHolder(parent, R.layout.adapter_theme_pic_item, listener)
    }


    private class ViewHolder(parent: ViewGroup, layoutId: Int, val onItemClickListener: OnItemClickListener<ThemeItemBean>) : BaseRecyclerViewHolder<ThemeItemBean>(parent, layoutId) {

        private val ivPic by lazy { itemView.findViewById<ImageView>(R.id.iv_adapter_theme_pic) }
        private val ivSelect by lazy { itemView.findViewById<ImageView>(R.id.iv_adapter_theme_select) }

        override fun onBindViewHolder(bean: ThemeItemBean?, position: Int) {
            bean?.apply {
                ivPic.setImageResource(icon)
                ivSelect.visibility = if (isSelected) View.VISIBLE else View.GONE
                ivPic.setOnClickListener { onItemClickListener.onClick(this, position) }
            }

        }

    }

}