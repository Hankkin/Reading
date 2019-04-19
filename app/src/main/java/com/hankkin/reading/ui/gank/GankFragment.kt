package com.hankkin.reading.ui.gank

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.hankkin.library.widget.view.PageLayout
import com.hankkin.reading.R
import com.hankkin.reading.base.BaseMvpFragment
import com.hankkin.reading.domain.GankToadyBean
import com.kekstudio.dachshundtablayout.indicators.PointMoveIndicator
import kotlinx.android.synthetic.main.fragment_gank.*

/**
 * Created by Hankkin on 2018/11/8.
 */
class GankFragment : BaseMvpFragment<GankPresenter>(), GankContract.IView{

    override fun registerPresenter() = GankPresenter::class.java

    override fun setGanks(gankBean: GankToadyBean?) {
        gankBean?.apply {
            val adapter = PageAdapter(childFragmentManager,this.category)
            vp_gank.adapter = adapter
            tab_gank.setupWithViewPager(vp_gank)
            tab_gank.tabMode = TabLayout.MODE_SCROLLABLE
            val indicator = PointMoveIndicator(tab_gank)
            tab_gank.animatedIndicator = indicator
            vp_gank.offscreenPageLimit = gankBean.category.size
            mPageLayout.hide()
        }
    }
    override fun getLayoutId() = R.layout.fragment_gank


    override fun initView() {
        initPageLayout(ll_gank, true)
        mPageLayout.setOnRetryListener(object : PageLayout.OnRetryClickListener{
            override fun onRetry() {
                mPageLayout.showLoading()
                getPresenter().getGanksToday()
            }
        })
    }

    override fun initData() {
        getPresenter().getGanksToday()
    }

    class PageAdapter(fm: FragmentManager,val data: MutableList<String>) : FragmentStatePagerAdapter(fm) {


        override fun getItem(i: Int): Fragment {
            val bundle = Bundle()
            val fg = GankListFragment()
            bundle.putInt("index", i)
            bundle.putString("bean", data.get(i))
            fg.arguments = bundle
            return fg
        }

        override fun getCount(): Int {
            return data.size
        }

        override fun getPageTitle(position: Int): CharSequence {
            return data[position]
        }

    }

    override fun setFail() {
        mPageLayout.showError()
    }

}