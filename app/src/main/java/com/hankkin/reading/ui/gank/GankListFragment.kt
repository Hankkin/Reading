package com.hankkin.reading.ui.gank

import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import com.hankkin.library.widget.view.PageLayout
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
class GankListFragment : BaseMvpFragment<GankListPresenter>(), GankListContract.IView, SwipeRefreshLayout.OnRefreshListener {

    private lateinit var mAdapter: GankAdapter
    private var mPage = 1
    private lateinit var mCate: String


    override fun registerPresenter() = GankListPresenter::class.java


    override fun getLayoutId() = R.layout.fragment_gank_item


    override fun initView() {
        mCate = arguments?.getString("bean").toString()
        initPageLayout(xrv_gank)
        mPageLayout.setOnRetryListener(object : PageLayout.OnRetryClickListener{
            override fun onRetry() {
                mPageLayout.showLoading()
                getPresenter().getGanks(mCate, mPage)
            }
        })
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
                    getPresenter().getGanks(mCate, mPage)
                }

                override fun onRefresh() {
                }

            })
        }
    }

    override fun initData() {
        getPresenter().getGanks(mCate, mPage)
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
        mPageLayout.hide()
    }


    override fun onRefresh() {
        mPage = 1
        getPresenter().getGanks(mCate, mPage)
    }

}