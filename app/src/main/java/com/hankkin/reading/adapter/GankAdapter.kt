package com.hankkin.reading.adapter

import android.app.Activity
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.view.ViewCompat
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.hankkin.reading.R
import com.hankkin.reading.adapter.base.BaseRecyclerViewAdapter
import com.hankkin.reading.adapter.base.BaseRecyclerViewHolder
import com.hankkin.reading.domain.ResultBean
import com.hankkin.reading.ui.gank.ImageActivity
import com.hankkin.reading.ui.home.articledetail.CommonWebActivity
import com.howshea.basemodule.component.viewGroup.NineGridImageLayout

/**
 * Created by Hankkin on 2018/11/6.
 */
class GankAdapter : BaseRecyclerViewAdapter<ResultBean>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder<ResultBean> {
        return ViewHolder(parent, R.layout.adapter_gank_item)
    }

    private class ViewHolder(parent: ViewGroup, layoutId: Int) : BaseRecyclerViewHolder<ResultBean>(parent, layoutId) {
        val tvAuthor by lazy { itemView.findViewById<TextView>(R.id.tv_adapter_gank_author) }
        val tvChapter by lazy { itemView.findViewById<TextView>(R.id.tv_adapter_gank_type) }
        val tvTime by lazy { itemView.findViewById<TextView>(R.id.tv_adapter_gank_time) }
        val tvTitle by lazy { itemView.findViewById<TextView>(R.id.tv_adapter_gank_title) }
        val picLayout by lazy { itemView.findViewById<NineGridImageLayout>(R.id.img_layout) }
        val ll by lazy { itemView.findViewById<LinearLayout>(R.id.ll_gank) }

        override fun onBindViewHolder(resultBean: ResultBean, position: Int) {
            resultBean.apply {
                tvAuthor.text = who
                tvChapter.text = type + "/" + source
                tvTime.text = publishedAt.substring(0,10).replace("-",".")
                tvTitle.text = desc
                if ("福利".equals(type)){
                    images = mutableListOf()
                    images?.add(url)
                }
                picLayout.visibility = if (images == null) View.GONE else View.VISIBLE
                picLayout.run {
                    images?.run {
                        setData(this,1.0F)
                        onItemClick { v, position ->
                            val intent = ImageActivity.newIntent(context, images!!, position)
                            val options = ViewCompat.getTransitionName(v)?.let { ActivityOptionsCompat.makeSceneTransitionAnimation(context as Activity, v, it) }
                            context.startActivity(intent, options?.toBundle())
                        }
                    }
                    loadImages { v, url ->
                        Glide.with(context)
                                .load(url)
                                .transition(withCrossFade())
                                .apply(RequestOptions().placeholder(R.color.grey1)
                                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                                .into(v)
                    }

                }

                ll.setOnClickListener { CommonWebActivity.loadUrl(itemView.context, url, desc) }
            }
        }
    }
}

