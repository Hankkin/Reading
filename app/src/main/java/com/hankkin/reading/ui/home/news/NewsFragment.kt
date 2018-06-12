package com.hankkin.reading.ui.home.news

import android.os.Bundle
import com.hankkin.reading.R
import com.hankkin.reading.base.BaseFragment

/**
 * Created by huanghaijie on 2018/5/15.
 */
class NewsFragment : BaseFragment() {
    override fun initViews() {

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