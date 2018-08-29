package com.hankkin.reading.ui.person

import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import com.hankkin.library.utils.ToastUtils
import com.hankkin.reading.R
import com.hankkin.reading.adapter.AndroidAdapter
import com.hankkin.reading.adapter.base.XRecyclerView
import com.hankkin.reading.base.BaseMvpActivity
import com.hankkin.reading.control.UserControl
import com.hankkin.reading.domain.ArticleBean
import com.hankkin.reading.event.EventMap
import com.hankkin.reading.utils.ViewHelper
import kotlinx.android.synthetic.main.activity_my_collect.*
import kotlinx.android.synthetic.main.layout_title_bar_back.*

class MyCollectActivity : BaseMvpActivity<MyCollectPresenter>(), MyCollectContract.IView, SwipeRefreshLayout.OnRefreshListener {


    private var mPage: Int = 0
    private lateinit var mAdapter: AndroidAdapter

    override fun getLayoutId(): Int {
        return R.layout.activity_my_collect
    }

    override fun registerPresenter() = MyCollectPresenter::class.java

    override fun isHasBus(): Boolean {
        return true
    }

    override fun initView() {
        setStatusBarColor()
        tv_normal_title.text = resources.getString(R.string.drawer_collect)
        ViewHelper.setRefreshLayout(this, true, refresh_collect, this)
        iv_back_icon.setOnClickListener { finish() }
    }

    override fun initData() {
        val linearLayoutManager = LinearLayoutManager(this)
        xrv_collect.apply {
            mAdapter = AndroidAdapter()
            layoutManager = linearLayoutManager
            setPullRefreshEnabled(false)
            clearHeader()
            adapter = mAdapter
            setLoadingListener(object : XRecyclerView.LoadingListener {
                override fun onLoadMore() {
                    loadData(mPage)
                }

                override fun onRefresh() {
                }
            })
            loadData(mPage)
        }


    }


    fun loadData(page: Int) {
        getPresenter().getCollectHttp(page)
    }

    override fun onRefresh() {
        xrv_collect.reset()
        refresh_collect.isRefreshing = true
        mPage = 0
        loadData(mPage)
    }

    override fun getCollectData(articleBean: ArticleBean) {
        mPage = articleBean.curPage
        if (mPage < 2) {
            xrv_collect.scrollToPosition(0)
            mAdapter.clear()
        }
        mAdapter.addAll(articleBean.datas)
        mAdapter.notifyDataSetChanged()
        if (mAdapter.data.size < 20) {
            xrv_collect.noMoreLoading()
        }
        xrv_collect.refreshComplete()
        if (refresh_collect.isRefreshing) {
            refresh_collect.isRefreshing = false
        }
    }

    override fun setFail() {
        refresh_collect.isRefreshing = false
    }

    override fun cancelCollectResult(id: Int) {
        UserControl.getCurrentUser()!!.collectIds.remove(id.toString())
        ToastUtils.showInfo(this, "取消收藏")
    }

    override fun collectResult(id: Int) {
        UserControl.getCurrentUser()!!.collectIds.add(id.toString())
        ToastUtils.showSuccess(this, "收藏成功")
    }

    override fun onEvent(event: EventMap.BaseEvent) {
        if (event is EventMap.CollectEvent) {
            if (event.flag == EventMap.CollectEvent.COLLECT) {
                getPresenter().collectHttp(event.id)
            } else {
                getPresenter().cancelCollectHttp(event.id)
            }
        }
    }

}
