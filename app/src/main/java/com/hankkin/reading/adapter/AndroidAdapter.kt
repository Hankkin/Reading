package com.hankkin.reading.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bilibili.magicasakura.widgets.TintImageView
import com.bilibili.magicasakura.widgets.TintTextView
import com.hankkin.reading.R
import com.hankkin.reading.adapter.base.BaseRecyclerViewAdapter
import com.hankkin.reading.adapter.base.BaseRecyclerViewHolder
import com.hankkin.reading.control.UserControl
import com.hankkin.reading.domain.ArticleDetailBean
import com.hankkin.reading.domain.TagBean
import com.hankkin.reading.event.EventMap
import com.hankkin.reading.ui.home.articledetail.CommonWebActivity
import com.hankkin.reading.ui.login.LoginActivity
import com.hankkin.reading.utils.GlideUtils
import com.hankkin.reading.utils.RxBus

/**
 * Created by huanghaijie on 2018/7/8.
 */
class AndroidAdapter : BaseRecyclerViewAdapter<ArticleDetailBean>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder<*> {
        return ViewHolder(parent, R.layout.adapter_android_item)
    }


    private class ViewHolder(parent: ViewGroup, layoutId: Int) : BaseRecyclerViewHolder<ArticleDetailBean>(parent, layoutId) {

        val tvAuthor by lazy { itemView.findViewById<TextView>(R.id.tv_adapter_android_author) }
        val tvDesc by lazy { itemView.findViewById<TextView>(R.id.tv_adapter_android_desc) }
        val tvChapter by lazy { itemView.findViewById<TextView>(R.id.tv_adapter_android_chapter) }
        val tvTime by lazy { itemView.findViewById<TextView>(R.id.tv_adapter_android_time) }
        val ivPic by lazy { itemView.findViewById<ImageView>(R.id.iv_adapter_android_pic) }
        val llTags by lazy { itemView.findViewById<LinearLayout>(R.id.ll_adapter_android_tags) }
        val ivCollect by lazy { itemView.findViewById<ImageView>(R.id.iv_adapter_android_collect_normal) }
        val ivCollected by lazy { itemView.findViewById<TintImageView>(R.id.iv_adapter_android_collected) }

        override fun onBindViewHolder(bean: ArticleDetailBean, position: Int) {
            tvAuthor.text = bean.author

            tvChapter.text = if (bean.superChapterName == null) bean.chapterName else bean.superChapterName + " / " + bean.chapterName
            if (bean.desc.isEmpty()) {
                tvDesc.text = bean.title
            } else {
                tvDesc.text = bean.desc
            }
            tvTime.text = bean.niceDate
            if (bean.envelopePic.isEmpty()) {
                ivPic.visibility = View.GONE
            } else {
                ivPic.visibility = View.VISIBLE
                GlideUtils.loadImageView(ivPic.context, bean.envelopePic, ivPic)
            }
            llTags.removeAllViews()
            if (bean.tags != null) {
                for (s in bean.tags!!) {
                    val tv = LayoutInflater.from(llTags.context).inflate(R.layout.layout_adapter_android_tag, null) as TintTextView
                    tv.text = s.name
                    llTags.addView(tv)
                }
                if (bean.fresh) {
                    val tv = LayoutInflater.from(llTags.context).inflate(R.layout.layout_adapter_android_tag, null) as TintTextView
                    tv.text = "新"
                    llTags.addView(tv)
                }
            }

            itemView.setOnClickListener {
                CommonWebActivity.loadUrl(itemView.context, bean.link, bean.title)
            }
            if (!UserControl.isLogin()) {
                ivCollect.visibility = View.VISIBLE
                ivCollected.visibility = View.GONE
            } else {
                if (UserControl.getCurrentUser()!!.collectIds.contains(bean.id.toString())||UserControl.getCurrentUser()!!.collectIds.contains(bean.originId.toString())) {
                    ivCollected.visibility = View.VISIBLE
                    ivCollect.visibility = View.GONE
                } else {
                    ivCollect.visibility = View.VISIBLE
                    ivCollected.visibility = View.GONE
                }
            }
            ivCollect.setOnClickListener {
                if (!UserControl.isLogin()) {
                    ivCollect.context.startActivity(Intent(ivCollect.context, LoginActivity::class.java))
                } else {
                    ivCollect.visibility = View.GONE
                    ivCollected.visibility = View.VISIBLE
                    val id = bean.id ?: bean.originId
                    RxBus.getDefault().post(EventMap.CollectEvent(EventMap.CollectEvent.COLLECT, id))
                }
            }
            ivCollected.setOnClickListener {
                if (!UserControl.isLogin()) {
                    ivCollect.context.startActivity(Intent(ivCollect.context, LoginActivity::class.java))
                } else {
                    ivCollect.visibility = View.VISIBLE
                    ivCollected.visibility = View.GONE
                    val id = bean.id ?: bean.originId
                    RxBus.getDefault().post(EventMap.CollectEvent(EventMap.CollectEvent.UNCOLLECT, id))
                }
            }
        }

    }
}