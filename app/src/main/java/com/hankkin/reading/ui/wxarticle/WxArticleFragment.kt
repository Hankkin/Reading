package com.hankkin.reading.ui.wxarticle

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.hankkin.reading.R
import com.hankkin.reading.base.BaseMvpFragment
import com.hankkin.reading.domain.CateBean
import com.hankkin.reading.domain.WxArticleBean
import com.hankkin.reading.ui.home.project.ProjectFragment
import com.hankkin.reading.ui.home.project.projectlist.ProjectListFragment
import com.hankkin.reading.ui.todo.ToDoListFragment
import com.kekstudio.dachshundtablayout.indicators.PointMoveIndicator
import kotlinx.android.synthetic.main.fragment_project.*
import kotlinx.android.synthetic.main.fragment_wxarticle.*

/**
 * Created by Hankkin on 2018/11/6.
 */
class WxArticleFragment : BaseMvpFragment<WxArticlePresenter>(), WxArticleContact.IView {

    override fun getLayoutId(): Int {
        return R.layout.fragment_wxarticle
    }

    override fun registerPresenter() = WxArticlePresenter::class.java

    override fun initView() {
        getPresenter().getWxTabs()
    }

    override fun initData() {

    }

    override fun setTabs(data: MutableList<WxArticleBean>) {
        val adapter = PageAdapter(childFragmentManager, data)
        vp_wx.adapter = adapter
        tab_wx.setupWithViewPager(vp_wx)
        tab_wx.tabMode = TabLayout.MODE_SCROLLABLE
        val indicator = PointMoveIndicator(tab_wx)
        tab_wx.animatedIndicator = indicator
        vp_wx.offscreenPageLimit = data.size
    }

    class PageAdapter(fm: FragmentManager, val data: MutableList<WxArticleBean>) : FragmentStatePagerAdapter(fm) {

        override fun getItem(i: Int): Fragment {
            val bundle = Bundle()
            val fg = WxArticleListFragment()
            bundle.putInt("index", i)
            bundle.putSerializable("bean", data.get(i))
            fg.arguments = bundle
            return fg
        }

        override fun getCount(): Int {
            return data.size
        }

        override fun getPageTitle(position: Int): CharSequence {
            return data[position].name
        }

    }


}