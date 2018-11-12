package com.hankkin.reading.ui.gank

import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.hankkin.reading.R
import com.hankkin.reading.base.BaseFragment
import com.kekstudio.dachshundtablayout.indicators.PointMoveIndicator
import kotlinx.android.synthetic.main.fragment_gank.*

/**
 * Created by Hankkin on 2018/11/8.
 */
class GankFragment : BaseFragment(){



    override fun getLayoutId() = R.layout.fragment_gank

    override fun initViews() {

    }


    override fun initData() {
        tab_gank.apply {
            val adapter = PageAdapter(childFragmentManager)
            vp_gank.adapter = adapter
            setupWithViewPager(vp_gank)
            tabMode = TabLayout.MODE_SCROLLABLE
            val indicator = PointMoveIndicator(tab_gank)
            animatedIndicator = indicator
            vp_gank.offscreenPageLimit = 2
        }
    }

    class PageAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        private val mFgList = listOf<Fragment>(
                GankTodayFragment(),
                GankAndroidFragment()
        )
        private val titles = listOf<String>("今日推荐","Android")

        override fun getItem(i: Int): Fragment {
            return mFgList[i]
        }

        override fun getCount(): Int {
            return mFgList.size
        }

        override fun getPageTitle(position: Int): CharSequence {
            return titles[position]
        }

    }

}