package com.hankkin.reading.ui.home.hot

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.hankkin.library.widget.view.PageLayout
import com.hankkin.reading.R
import com.hankkin.reading.base.BaseMvpFragment
import com.hankkin.reading.domain.HotBean
import com.hankkin.reading.ui.home.hot.hotlist.HotListFragment
import com.kekstudio.dachshundtablayout.indicators.PointMoveIndicator
import kotlinx.android.synthetic.main.fragment_hot.*

/**
 * Created by huanghaijie on 2018/5/15.
 */
class HotFragment : BaseMvpFragment<HotPresenter>(), HotContact.IView {


    override fun getLayoutId(): Int {
        return R.layout.fragment_hot
    }

    override fun initData() {
        getPresenter().getHot()
    }


    override fun registerPresenter() = HotPresenter::class.java


    override fun setHot(data: MutableList<HotBean>) {
        val temp = mutableListOf<HotBean>()
        temp.add(data[0])
        temp.add(data[1])
        temp.add(data[2])
        tab_hot.apply {
            val adapter = PageAdapter(childFragmentManager, temp)
            vp_hot.adapter = adapter
            setupWithViewPager(vp_hot)
            tabMode = TabLayout.MODE_FIXED
            val indicator = PointMoveIndicator(tab_hot)
            animatedIndicator = indicator
            vp_hot.offscreenPageLimit = temp.size
        }
        mPageLayout.hide()

    }

    override fun setFail() {
        mPageLayout.showError()
    }

    override fun initView() {
        initPageLayout(ll_hot, true)
        mPageLayout.setOnRetryListener(object : PageLayout.OnRetryClickListener{
            override fun onRetry() {
                mPageLayout.showLoading()
                getPresenter().getHot()
            }
        })
    }

    fun getCurrentIndex() = vp_hot.currentItem



    class PageAdapter(fm: FragmentManager, val data: MutableList<HotBean>) : FragmentStatePagerAdapter(fm) {

        override fun getItem(i: Int): Fragment {
            val bundle = Bundle()
            val fg = HotListFragment()
            bundle.putInt("index", i)
            bundle.putSerializable("bean", data[i])
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