package com.hankkin.reading.ui.home.project

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.hankkin.reading.R
import com.hankkin.reading.base.BaseMvpFragment
import com.hankkin.reading.domain.CateBean
import com.hankkin.reading.ui.home.project.projectlist.ProjectListFragment
import com.kekstudio.dachshundtablayout.indicators.PointMoveIndicator
import kotlinx.android.synthetic.main.fragment_project.*

/**
 * Created by huanghaijie on 2018/5/15.
 */
class ProjectFragment : BaseMvpFragment<ProjectPresenter>(), ProjectContact.IView {


    override fun getLayoutId(): Int {
        return R.layout.fragment_project
    }

    override fun initData() {
        getPresenter().getCatesHttp()
    }


    override fun registerPresenter() = ProjectPresenter::class.java

    override fun setCates(data: MutableList<CateBean>) {
        val adapter = PageAdapter(childFragmentManager, data)
        vp_project.adapter = adapter
        tab_project.setupWithViewPager(vp_project)
        tab_project.tabMode = TabLayout.MODE_SCROLLABLE
        val indicator = PointMoveIndicator(tab_project)
        tab_project.animatedIndicator = indicator
        vp_project.offscreenPageLimit = data.size
    }

    override fun initView() {

    }

    fun getCurrentIndex() = vp_project.currentItem

    class PageAdapter(fm: FragmentManager, val data: MutableList<CateBean>) : FragmentStatePagerAdapter(fm) {

        override fun getItem(i: Int): Fragment {
            val bundle = Bundle()
            val fg = ProjectListFragment()
            bundle.putInt("index", i)
            bundle.putSerializable("bean",data.get(i))
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