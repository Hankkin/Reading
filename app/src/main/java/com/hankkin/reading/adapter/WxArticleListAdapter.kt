package com.hankkin.reading.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.bilibili.magicasakura.widgets.TintTextView
import com.hankkin.reading.R
import com.hankkin.reading.adapter.base.BaseRecyclerViewAdapter
import com.hankkin.reading.adapter.base.BaseRecyclerViewHolder
import com.hankkin.reading.domain.WxArticleListItemBean
import com.hankkin.reading.ui.home.articledetail.CommonWebActivity

/**
 * Created by Hankkin on 2018/11/6.
 */
class WxArticleListAdapter : BaseRecyclerViewAdapter<WxArticleListItemBean>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder<WxArticleListItemBean> {
        return ViewHolder(parent, R.layout.adapter_wxarticle_item)
    }

    private class ViewHolder(parent: ViewGroup, layoutId: Int) : BaseRecyclerViewHolder<WxArticleListItemBean>(parent, layoutId) {


        val tvAuthor by lazy { itemView.findViewById<TextView>(R.id.tv_adapter_wx_author) }
        val tvChapter by lazy { itemView.findViewById<TextView>(R.id.tv_adapter_wx_chapter) }
        val tvTime by lazy { itemView.findViewById<TextView>(R.id.tv_adapter_wx_time) }
        val tvTitle by lazy { itemView.findViewById<TextView>(R.id.tv_adapter_wx_title) }
        val llTags: LinearLayout by lazy { itemView.findViewById<LinearLayout>(R.id.ll_adapter_wx_tags) }
        val ll by lazy { itemView.findViewById<LinearLayout>(R.id.ll_wx) }

        override fun onBindViewHolder(wxArticleListBean: WxArticleListItemBean, position: Int) {
            wxArticleListBean.apply {
                tvAuthor.text = author
                tvChapter.text = chapterName
                tvTime.text = niceDate
                tvTitle.text = title
                llTags.removeAllViews()
                tags?.let {
                    for (s in it) {
                        llTags.addView((LayoutInflater.from(llTags.context).inflate(R.layout.layout_adapter_android_tag, null) as TintTextView).apply {
                            text = s.name
                        })
                    }
                    if (fresh) {
                        llTags.addView((LayoutInflater.from(llTags.context).inflate(R.layout.layout_adapter_android_tag, null) as TintTextView).apply {
                            text = "新"
                        })
                    }
                }
                ll.setOnClickListener { CommonWebActivity.loadUrl(itemView.context, link, title) }
            }
        }
    }
}

