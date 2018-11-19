package com.hankkin.reading.ui.gank

import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import com.hankkin.reading.R
import com.hankkin.reading.adapter.GankAdapter
import com.hankkin.reading.base.BaseMvpFragment
import com.hankkin.reading.domain.GankToadyBean
import com.hankkin.reading.utils.ViewHelper
import kotlinx.android.synthetic.main.fragment_gank_item.*

/**
 * Created by Hankkin on 2018/11/8.
 */
class GankTodayFragment : BaseMvpFragment<GankTodayPresenter>(), GankTodayContract.IView, SwipeRefreshLayout.OnRefreshListener {

    private lateinit var mAdapter: GankAdapter


    override fun registerPresenter() = GankTodayPresenter::class.java


    override fun getLayoutId() = R.layout.fragment_gank_item


    override fun initView() {

        ViewHelper.setRefreshLayout(context!!, true, refresh_gank, this)
        mAdapter = GankAdapter()
        xrv_gank.apply {
            layoutManager = LinearLayoutManager(context)
            setPullRefreshEnabled(false)
            clearHeader()
            adapter = mAdapter
            setLoadingMoreEnabled(false)
        }
    }

    override fun initData() {
        getPresenter().getGanksToday()
    }

    override fun setGanks(gankBean: GankToadyBean?) {
        mAdapter.apply {
            gankBean?.results?.Android?.let { addAll(it) }
            gankBean?.results?.App?.let { addAll(it) }
            gankBean?.results?.iOS?.let { addAll(it) }
            gankBean?.results?.前端?.let { addAll(it) }
            gankBean?.results?.休息视频?.let { addAll(it) }
            gankBean?.results?.拓展资源?.let { addAll(it) }
            gankBean?.results?.福利?.let { addAll(it) }
            gankBean?.results?.瞎推荐?.let { addAll(it) }
            notifyDataSetChanged()
        }
        refresh_gank.isRefreshing = false
    }


    override fun onRefresh() {
        getPresenter().getGanksToday()
    }

}