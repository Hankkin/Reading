package com.hankkin.reading.ui.gank

import android.os.Handler
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.hankkin.library.fuct.common.DoubleClickListener
import com.hankkin.library.utils.ToastUtils
import com.hankkin.reading.R
import com.hankkin.reading.adapter.GankAdapter
import com.hankkin.reading.adapter.base.XRecyclerView
import com.hankkin.reading.base.BaseMvpFragment
import com.hankkin.reading.domain.GankBean
import com.hankkin.reading.utils.ViewHelper
import kotlinx.android.synthetic.main.fragment_gank_item.*

/**
 * Created by Hankkin on 2018/11/8.
 */
class GankAndroidFragment : BaseMvpFragment<GankPresenter>(), GankContract.IView, SwipeRefreshLayout.OnRefreshListener {
    private val GANK_CATE = "Android"

    private lateinit var mAdapter: GankAdapter
    private var mPage = 1


    override fun registerPresenter() = GankPresenter::class.java


    override fun getLayoutId() = R.layout.fragment_gank_item


    override fun initView() {

        ViewHelper.setRefreshLayout(context!!, true, refresh_gank, this)
        mAdapter = GankAdapter()
        xrv_gank.apply {
            layoutManager = LinearLayoutManager(context)
            setPullRefreshEnabled(false)
            clearHeader()
            adapter = mAdapter
            setLoadingListener(object : XRecyclerView.LoadingListener {
                override fun onLoadMore() {
                    mPage++
                    getPresenter().getGanks(GANK_CATE, mPage)
                }

                override fun onRefresh() {
                }

            })
        }
    }

    override fun initData() {
        getPresenter().getGanks(GANK_CATE, mPage)
    }

    override fun setGanks(gankBean: GankBean) {
        if (mPage == 1) {
            mAdapter.clear()
        }
        mAdapter.addAll(gankBean.results)
        mAdapter.notifyDataSetChanged()
        if (gankBean.results.size < 20) {
            xrv_gank.noMoreLoading()
        }
        if (refresh_gank.isRefreshing) {
            refresh_gank.isRefreshing = false
        }
        xrv_gank.refreshComplete()
    }


    override fun onRefresh() {
        mPage = 1
        getPresenter().getGanks(GANK_CATE, mPage)
    }

}