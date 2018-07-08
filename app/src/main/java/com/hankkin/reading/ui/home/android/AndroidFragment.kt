package com.hankkin.reading.ui.home.android

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import com.hankkin.reading.R
import com.hankkin.reading.adapter.AndroidAdapter
import com.hankkin.reading.base.BaseMvpFragment
import com.hankkin.reading.domain.ArticleBean
import com.hankkin.reading.utils.ThemeHelper
import com.hankkin.reading.utils.ToastUtils
import com.hankkin.reading.view.xrecycleview.XRecyclerView
import kotlinx.android.synthetic.main.fragment_news.*

/**
 * Created by huanghaijie on 2018/5/15.
 */
class AndroidFragment : BaseMvpFragment<AndroidPresenter>(), AndroidContact.IView, SwipeRefreshLayout.OnRefreshListener {


    private var mPage: Int = 0
    private lateinit var mAdapter: AndroidAdapter


    override fun initView() {
        refresh_android.setColorSchemeResources(ThemeHelper.getCurrentColor(context))
    }

    public fun newInstance(index: Int) {
        val fragment = AndroidFragment()
        val args = Bundle()
        args.putInt("index", index)
        fragment.arguments = args
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_news
    }

    override fun initData() {
        mAdapter = AndroidAdapter()
        xrv_android.layoutManager = LinearLayoutManager(context)
        xrv_android.setPullRefreshEnabled(false)
        xrv_android.setLoadingListener(object : XRecyclerView.LoadingListener {
            override fun onLoadMore() {
                loadData(mPage)
            }

            override fun onRefresh() {
            }

        })
        xrv_android.adapter = mAdapter
        refresh_android.setOnRefreshListener(this)
        refresh_android.isRefreshing = true
        loadData(mPage)
    }


    override fun setFail() {
        refresh_android.isRefreshing = false
        xrv_android.refreshComplete()
        ToastUtils.showToast(context, "网络异常...")
    }

    fun loadData(page: Int) {
        getPresenter().getArticle(page)
    }

    override fun onRefresh() {
        mPage = 0
        loadData(mPage)
    }

    override fun registerPresenter() = AndroidPresenter::class.java

    override fun setArticle(articleBean: ArticleBean) {
        mPage = articleBean.curPage
        if (mPage < 2) {
            mAdapter.clear()
        }
        mAdapter.addAll(articleBean.datas)
        mAdapter.notifyDataSetChanged()
        if (articleBean.size < 20) {
            xrv_android.noMoreLoading()
        }
        xrv_android.refreshComplete()
        if (refresh_android.isRefreshing){
            refresh_android.isRefreshing = false
        }
    }

}