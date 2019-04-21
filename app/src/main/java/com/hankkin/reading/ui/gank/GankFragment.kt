package com.hankkin.reading.ui.gank

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.hankkin.library.widget.view.PageLayout
import com.hankkin.reading.R
import com.hankkin.reading.base.BaseMvpFragment
import com.hankkin.reading.domain.CategoryBean
import com.hankkin.reading.domain.GankToadyBean
import com.hankkin.reading.event.EventMap
import com.hankkin.reading.ui.category.CategoryActivity
import com.kekstudio.dachshundtablayout.indicators.PointMoveIndicator
import kotlinx.android.synthetic.main.fragment_gank.*

/**
 * Created by Hankkin on 2018/11/8.
 */
class GankFragment : BaseMvpFragment<GankPresenter>(), GankContract.IView {

    private var mTitles = arrayListOf<CategoryBean>()

    override fun registerPresenter() = GankPresenter::class.java

    override fun isHasBus() = true

    override fun setGanks(gankBean: GankToadyBean?) {
        gankBean?.apply {
            val list = ArrayList<CategoryBean>()
            gankBean.category.forEach {
                list.add(CategoryBean(it, true))
            }
            setTabs(list)
        }
    }

    override fun getLayoutId() = R.layout.fragment_gank


    override fun initView() {
        initPageLayout(ll_gank, true)
        mPageLayout.setOnRetryListener(object : PageLayout.OnRetryClickListener {
            override fun onRetry() {
                mPageLayout.showLoading()
                getPresenter().getGanksToday()
            }
        })
        iv_gank_sort.setOnClickListener { mTitles.let { it1 -> CategoryActivity.intentTo(context, it1) } }
    }

    override fun initData() {
        getPresenter().getGanksToday()
    }

    private fun setTabs(data: ArrayList<CategoryBean>) {
        mTitles.clear()
        mTitles.addAll(data)
        val adapter = PageAdapter(childFragmentManager, mTitles)
        vp_gank.adapter = adapter
        tab_gank.setupWithViewPager(vp_gank)
        tab_gank.tabMode = TabLayout.MODE_SCROLLABLE
        val indicator = PointMoveIndicator(tab_gank)
        tab_gank.animatedIndicator = indicator
        vp_gank.offscreenPageLimit = mTitles.size
        mPageLayout.hide()
    }

    class PageAdapter(fm: FragmentManager, val data: ArrayList<CategoryBean>) : FragmentStatePagerAdapter(fm) {


        override fun getItem(i: Int): Fragment {
            val bundle = Bundle()
            val fg = GankListFragment()
            bundle.putInt("index", i)
            bundle.putString("bean", data.get(i).title)
            fg.arguments = bundle
            return fg
        }

        override fun getCount(): Int {
            return data.filter { it.isOpen }.size
        }

        override fun getPageTitle(position: Int): CharSequence {
            return data[position].title
        }

    }

    override fun setFail() {
        mPageLayout.showError()
    }


    override fun onEvent(event: EventMap.BaseEvent) {
        if (event is EventMap.CateEvent) {
            setTabs(event.data)
        }
    }
}