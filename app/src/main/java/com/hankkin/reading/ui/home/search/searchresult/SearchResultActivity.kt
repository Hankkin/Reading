package com.hankkin.reading.ui.home.search.searchresult

import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import com.hankkin.library.utils.ToastUtils
import com.hankkin.reading.R
import com.hankkin.reading.adapter.AndroidAdapter
import com.hankkin.reading.adapter.base.XRecyclerView
import com.hankkin.reading.base.BaseMvpActivity
import com.hankkin.reading.domain.ArticleBean
import com.hankkin.reading.domain.BannerBean
import com.hankkin.reading.ui.home.hot.hotlist.HotListContact
import com.hankkin.reading.ui.home.hot.hotlist.HotListPresenter
import com.hankkin.reading.utils.ViewHelper
import kotlinx.android.synthetic.main.activity_search_result.*
import kotlinx.android.synthetic.main.fragment_hot_list.*
import kotlinx.android.synthetic.main.layout_title_bar_back.*

class SearchResultActivity : BaseMvpActivity<HotListPresenter>(), HotListContact.IView, SwipeRefreshLayout.OnRefreshListener {

    private lateinit var mKey: String
    private var mPage: Int = 0
    private lateinit var mAdapter: AndroidAdapter


    override fun getLayoutId(): Int {
        return R.layout.activity_search_result
    }

    override fun initView() {
        setStatusBarColor()
        iv_back_icon.setOnClickListener { finish() }
        ViewHelper.setRefreshLayout(this, true, refresh_search_result, this)

        xrv_search_result.apply {
            mAdapter = AndroidAdapter()
            layoutManager = LinearLayoutManager(this@SearchResultActivity)
            setPullRefreshEnabled(false)
            clearHeader()
            adapter = mAdapter
            setLoadingListener(object : XRecyclerView.LoadingListener {
                override fun onLoadMore() {
                    getPresenter().queryKey(mPage, mKey)
                }

                override fun onRefresh() {
                }

            })
        }

    }

    override fun initData() {
        mKey = intent.getStringExtra("key")
        tv_normal_title.text = mKey
        getPresenter().queryKey(mPage, mKey)
    }

    override fun registerPresenter() = HotListPresenter::class.java

    override fun setBanner(banner: MutableList<BannerBean>) {
    }

    override fun setData(data: ArticleBean) {
        mPage = data.curPage
        if (mPage < 2) {
            xrv_search_result.scrollToPosition(0)
            mAdapter.clear()
        }
        data.datas?.apply {
            mAdapter.addAll(data.datas)
            mAdapter.notifyDataSetChanged()
            if (data.datas.size < 20) {
                xrv_search_result.noMoreLoading()
            }
            xrv_search_result.refreshComplete()
            if (refresh_search_result.isRefreshing) {
                refresh_search_result.isRefreshing = false
            }
        }
    }

    override fun onRefresh() {
        xrv_search_result.reset()
        refresh_search_result.isRefreshing = true
        mPage = 0
        getPresenter().queryKey(mPage, mKey)
    }

    override fun setFail() {
        ToastUtils.showError(this,resources.getString(R.string.pagelayout_error))
    }

}
