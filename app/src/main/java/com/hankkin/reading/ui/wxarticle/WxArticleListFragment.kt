package com.hankkin.reading.ui.wxarticle

import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import com.hankkin.reading.R
import com.hankkin.reading.adapter.WxArticleListAdapter
import com.hankkin.reading.adapter.base.XRecyclerView
import com.hankkin.reading.base.BaseMvpFragment
import com.hankkin.reading.domain.WxArticleBean
import com.hankkin.reading.domain.WxArticleListBean
import com.hankkin.reading.event.EventMap
import com.hankkin.reading.utils.ViewHelper
import kotlinx.android.synthetic.main.fragment_wxarticle_list.*

/**
 * Created by huanghaijie on 2018/5/15.
 */
class WxArticleListFragment : BaseMvpFragment<WxArticleListPresenter>(), WxArticleListContact.IView, SwipeRefreshLayout.OnRefreshListener {

    private lateinit var mAdapter: WxArticleListAdapter
    private lateinit var wxArticleListBean: WxArticleBean
    private var mPage: Int = 1
    private var mIndex: Int? = 0

    override fun registerPresenter() = WxArticleListPresenter::class.java

    override fun isHasBus() = true

    override fun getLayoutId(): Int {
        return R.layout.fragment_wxarticle_list
    }

    override fun initView() {
        initPageLayout(refresh_wx_list)
        ViewHelper.setRefreshLayout(context, true, refresh_wx_list, this)
        initXrv()
    }

    override fun initData() {
        mIndex = arguments?.getInt("index")
        wxArticleListBean = (arguments?.getSerializable("bean")) as WxArticleBean
        getPresenter().getWxArticleList(wxArticleListBean.id,mPage)
    }

    private fun initXrv() {
        mAdapter = WxArticleListAdapter()
        xrv_wx_list.apply {
            layoutManager = LinearLayoutManager(context)
            setPullRefreshEnabled(false)
            clearHeader()
            adapter = mAdapter
            setLoadingListener(object : XRecyclerView.LoadingListener {
                override fun onLoadMore() {
                    getPresenter().getWxArticleList(wxArticleListBean.id,mPage)
                }

                override fun onRefresh() {
                }

            })
        }
    }

    override fun setWxArticles(data: WxArticleListBean) {
        mPage = data.curPage +1
        if (mPage < 2) {
            xrv_wx_list.scrollToPosition(0)
            mAdapter.clear()
        }
        mAdapter.addAll(data.datas)
        mAdapter.notifyDataSetChanged()
        if (data.datas?.size!! < 20) {
            xrv_wx_list.noMoreLoading()
        }
        xrv_wx_list.refreshComplete()
        if (refresh_wx_list.isRefreshing) {
            refresh_wx_list.isRefreshing = false
        }
        mPageLayout.hide()
    }


    override fun onRefresh() {
        xrv_wx_list.reset()
        refresh_wx_list.isRefreshing = true
        mPage = 0
        getPresenter().getWxArticleList(wxArticleListBean.id,mPage)
    }

    override fun onEvent(event: EventMap.BaseEvent) {
        if (event is EventMap.XrvScollToPosEvent && event.index == mIndex){
            xrv_wx_list.smoothScrollToPosition(0)
        }
    }

}