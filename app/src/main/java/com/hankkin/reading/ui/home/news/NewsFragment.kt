package com.hankkin.reading.ui.home.news

import android.os.Bundle
import com.hankkin.reading.R
import com.hankkin.reading.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_news.*

/**
 * Created by huanghaijie on 2018/5/15.
 */
class NewsFragment : BaseFragment() {

    override fun initViews() {
        refresh_news_list.setColorSchemeResources(R.color.theme_color_primary)
    }

    public fun newInstance(index: Int){
        val fragment = NewsFragment()
        val args = Bundle()
        args.putInt("index",index)
        fragment.arguments = args
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_news
    }

    override fun initData() {
    }


}