package com.hankkin.reading.ui.home.cate

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.hankkin.reading.R
import com.hankkin.reading.base.BaseMvpFragment
import com.hankkin.reading.domain.CateBean
import com.hankkin.reading.ui.home.project.ProjectFragment
import com.hankkin.reading.ui.home.project.projectlist.CateListFragment
import com.hankkin.reading.utils.LoadingUtils
import com.kekstudio.dachshundtablayout.indicators.PointMoveIndicator
import kotlinx.android.synthetic.main.fragment_cates.*

/**
 * Created by huanghaijie on 2018/5/15.
 */
class CateFragment : BaseMvpFragment<CatePresenter>(), CateContact.IView {

    public fun newInstance(index: Int) {
        val fragment = CateFragment()
        val args = Bundle()
        args.putInt("index", index)
        fragment.arguments = args
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_cates
    }

    override fun initData() {
        getPresenter().getCatesHttp()
    }


    override fun registerPresenter() = CatePresenter::class.java

    override fun setCates(banner: MutableList<CateBean>) {
        val adapter = PageAdapter(childFragmentManager, banner[0])
        vp_cates.adapter = adapter
        tab_cates.setupWithViewPager(vp_cates)
        tab_cates.tabMode = TabLayout.MODE_FIXED
        val indicator = PointMoveIndicator(tab_cates)
        tab_cates.animatedIndicator = indicator
        vp_cates.offscreenPageLimit = ProjectFragment.tags.size
    }

    override fun initView() {

    }


    class PageAdapter(fm: FragmentManager, val data: CateBean) : FragmentStatePagerAdapter(fm) {

        override fun getItem(i: Int): Fragment {
            val bundle = Bundle()
            val fg = CateListFragment()
            bundle.putInt("index", i)
            bundle.putSerializable("bean",data.children[i])
            fg.arguments = bundle
            return fg
        }

        override fun getCount(): Int {
            return data.children.size
        }

        override fun getPageTitle(position: Int): CharSequence {
            return data.children[position].name
        }

    }

}