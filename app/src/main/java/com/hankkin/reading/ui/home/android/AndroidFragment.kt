package com.hankkin.reading.ui.home.android

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import com.hankkin.reading.R
import com.hankkin.reading.adapter.AndroidAdapter
import com.hankkin.reading.adapter.base.XRecyclerView
import com.hankkin.reading.base.BaseMvpFragment
import com.hankkin.reading.domain.ArticleBean
import com.hankkin.reading.event.EventMap
import com.hankkin.reading.utils.RxBus
import com.hankkin.reading.utils.ThemeHelper
import com.hankkin.reading.utils.ToastUtils
import com.hankkin.reading.utils.ViewHelper
import kotlinx.android.synthetic.main.fragment_android.*

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
        return R.layout.fragment_android
    }

    override fun initData() {
        xrv_android.overScrollMode
        initXrv()
        loadData(mPage)
        RxBus.getDefault().toObservable(EventMap.BaseEvent::class.java)
                .subscribe({
                    if (it is EventMap.ToUpEvent){
                        xrv_android.scrollToPosition(0)
                    }else if (it is EventMap.HomeRefreshEvent){
                        onRefresh()
                    }
                    else if (it is EventMap.ChangeThemeEvent){
                        ViewHelper.changeRefreshColor(refresh_android,context)
                    }
                })
    }

    fun initXrv() {
        ViewHelper.setRefreshLayout(context,true,refresh_android,this)
        mAdapter = AndroidAdapter()
        val linearLayoutManager = LinearLayoutManager(context)
        xrv_android.layoutManager = linearLayoutManager
        xrv_android.setPullRefreshEnabled(false)
        xrv_android.clearHeader()
        xrv_android.adapter = mAdapter
        xrv_android.setLoadingListener(object : XRecyclerView.LoadingListener {
            override fun onLoadMore() {
                loadData(mPage)
            }

            override fun onRefresh() {
            }

        })
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
        xrv_android.reset()
        refresh_android.isRefreshing = true
        mPage = 0
        loadData(mPage)
    }

    override fun registerPresenter() = AndroidPresenter::class.java

    override fun setArticle(articleBean: ArticleBean) {
        mPage = articleBean.curPage
        if (mPage < 2) {
            xrv_android.scrollToPosition(0)
            mAdapter.clear()
        }
        mAdapter.addAll(articleBean.datas)
        mAdapter.notifyDataSetChanged()
        if (articleBean.size < 20) {
            xrv_android.noMoreLoading()
        }
        xrv_android.refreshComplete()
        if (refresh_android.isRefreshing) {
            refresh_android.isRefreshing = false
        }
    }

}