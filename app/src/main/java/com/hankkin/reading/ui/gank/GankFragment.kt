package com.hankkin.reading.ui.gank

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hankkin.library.utils.SPUtils
import com.hankkin.library.widget.view.PageLayout
import com.hankkin.reading.R
import com.hankkin.reading.base.BaseMvpFragment
import com.hankkin.reading.common.Constant
import com.hankkin.reading.domain.CategoryBean
import com.hankkin.reading.domain.GankToadyBean
import com.hankkin.reading.event.EventMap
import com.hankkin.reading.ui.category.CategoryActivity
import com.kekstudio.dachshundtablayout.indicators.PointMoveIndicator
import kotlinx.android.synthetic.main.fragment_gank.*
import org.json.JSONArray


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
        val titles = SPUtils.getString(Constant.SP_KEY.CATEGORY_SORT)
        if (titles.isNotEmpty()) {
            setTabs(ArrayList(Gson().fromJson(titles,Array<CategoryBean>::class.java).asList()))
        }else {
            getPresenter().getGanksToday()
        }
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

        private val temp = data.filter { it.isOpen }
        override fun getItem(i: Int): Fragment {
            val bundle = Bundle()
            val fg = GankListFragment()
            bundle.putInt("index", i)
            bundle.putString("bean", temp.get(i).title)
            fg.arguments = bundle
            return fg
        }

        override fun getCount(): Int {
            return temp.size
        }

        override fun getPageTitle(position: Int): CharSequence {
            return temp[position].title
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