package com.hankkin.reading.ui.home.project.projectlist

import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import com.hankkin.reading.R
import com.hankkin.reading.adapter.AndroidAdapter
import com.hankkin.reading.adapter.base.XRecyclerView
import com.hankkin.reading.base.BaseMvpFragment
import com.hankkin.reading.domain.ArticleBean
import com.hankkin.reading.domain.BannerBean
import com.hankkin.reading.domain.CateBean
import com.hankkin.reading.event.EventMap
import com.hankkin.reading.utils.ViewHelper
import com.stx.xhb.xbanner.XBanner
import kotlinx.android.synthetic.main.fragment_android.*
import kotlinx.android.synthetic.main.fragment_hot_list.*
import kotlinx.android.synthetic.main.fragment_project_list.*

/**
 * Created by huanghaijie on 2018/5/15.
 */
class ProjectListFragment : BaseMvpFragment<ProjectListPresenter>(), ProjectListContact.IView, SwipeRefreshLayout.OnRefreshListener {

    private lateinit var mAdapter: AndroidAdapter

    private lateinit var cateBean: CateBean
    private var mPage: Int = 0

    override fun registerPresenter() = ProjectListPresenter::class.java

    override fun isHasBus(): Boolean {
        return true
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_project_list
    }


    override fun initView() {
        ViewHelper.setRefreshLayout(context, true, refresh_project, this)
        initXrv()
    }

    override fun initData() {
        cateBean = (arguments!!.getSerializable("bean")) as CateBean
        getPresenter().getCateList(mPage, cateBean.id)
    }

    fun initXrv() {
        ViewHelper.setRefreshLayout(context, true, refresh_project, this)
        xrv_project.apply {
            mAdapter = AndroidAdapter()
            layoutManager = LinearLayoutManager(context)
            setPullRefreshEnabled(false)
            clearHeader()
            adapter = mAdapter
            setLoadingListener(object : XRecyclerView.LoadingListener {
                override fun onLoadMore() {
                    getPresenter().getCateList(mPage, cateBean.id)
                }

                override fun onRefresh() {
                }

            })
        }
    }


    fun setAdapter(data: ArticleBean) {

        mPage = data.curPage
        if (mPage < 2) {
            xrv_project.scrollToPosition(0)
            mAdapter.clear()
        }
        data.datas?.apply {
            mAdapter.addAll(data.datas)
            mAdapter.notifyDataSetChanged()
            if (data.datas.size < 20) {
                xrv_project.noMoreLoading()
            }
            xrv_project.refreshComplete()
            if (refresh_project.isRefreshing) {
                refresh_project.isRefreshing = false
            }
        }

    }


    override fun setCateList(data: ArticleBean) {
        setAdapter(data)
    }

    override fun onRefresh() {
        xrv_project.reset()
        refresh_project.isRefreshing = true
        mPage = 0
        getPresenter().getCateList(mPage, cateBean.id)
    }


    override fun onEvent(event: EventMap.BaseEvent) {
        if (event is EventMap.WifiImgEvent) {
            mAdapter.notifyDataSetChanged()
        }
    }


}