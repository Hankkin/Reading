package com.hankkin.reading.ui.home.hot.hotlist

import android.content.Intent
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.widget.TextView
import com.bumptech.glide.Glide
import com.hankkin.library.widget.view.PageLayout
import com.hankkin.reading.R
import com.hankkin.reading.adapter.AndroidAdapter
import com.hankkin.reading.adapter.base.XRecyclerView
import com.hankkin.reading.base.BaseMvpFragment
import com.hankkin.reading.domain.ArticleBean
import com.hankkin.reading.domain.BannerBean
import com.hankkin.reading.domain.HotBean
import com.hankkin.reading.event.EventMap
import com.hankkin.reading.ui.home.articledetail.CommonWebActivity
import com.hankkin.reading.ui.home.hot.HotFragment
import com.hankkin.reading.ui.home.search.SearchActivity
import com.hankkin.reading.utils.ViewHelper
import com.hankkin.reading.view.widget.SWImageView
import com.stx.xhb.xbanner.XBanner
import kotlinx.android.synthetic.main.fragment_android.*
import kotlinx.android.synthetic.main.fragment_hot_list.*

/**
 * Created by huanghaijie on 2018/5/15.
 */
class HotListFragment : BaseMvpFragment<HotListPresenter>(), HotListContact.IView, SwipeRefreshLayout.OnRefreshListener {


    private var bannerData = mutableListOf<BannerBean>()
    private lateinit var mAdapter: AndroidAdapter
    private var xBanner: XBanner? = null

    private var hotBean: HotBean? = null
    private var mPage: Int = 0
    private var mIndex: Int = 0

    override fun registerPresenter() = HotListPresenter::class.java

    override fun isHasBus(): Boolean {
        return true
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_hot_list
    }


    override fun initView() {
        initPageLayout(xrv_hot)
        ViewHelper.setRefreshLayout(context, true, refresh_hot, this)
        initXrv()
    }

    override fun initData() {
        hotBean = arguments?.getSerializable("bean") as HotBean
        mIndex = arguments?.getInt("index")!!
        getPresenter().getBannerHttp()
    }

    private fun initXrv() {
        xrv_hot.apply {
            mAdapter = AndroidAdapter()
            layoutManager = LinearLayoutManager(context)
            setPullRefreshEnabled(false)
            clearHeader()
            adapter = mAdapter
            setLoadingListener(object : XRecyclerView.LoadingListener {
                override fun onLoadMore() {
                    getPresenter().queryKey(mPage, hotBean!!.name)
                }

                override fun onRefresh() {
                }

            })
        }


    }

    private fun initBannerHeader() {
        val layoutBanner = layoutInflater.inflate(R.layout.layout_project_banner_header, null)
        xBanner = layoutBanner.findViewById<XBanner>(R.id.banner_project)
        val urlList = mutableListOf<String>()
        val contentList = mutableListOf<String>()
        for (bannerBean in bannerData) {
            urlList.add(bannerBean.imagePath)
            contentList.add(bannerBean.title)
        }
        xBanner?.run {
            setData(R.layout.layout_banner_imageview, urlList, contentList)
            viewPager.pageMargin = 20
            loadImage { _, model, view, position ->
                val iv = view.findViewById<SWImageView>(R.id.iv_banner_item)
                Glide.with(context).load(model as String?).into(iv)
            }
            setOnItemClickListener { _, _, position -> context?.let { CommonWebActivity.loadUrl(it, bannerData[position].url, bannerData[position].title) } }
        }
        xrv_hot.addHeaderView(layoutBanner)
    }

    private fun setAdapter(data: ArticleBean) {
        mPage = data.curPage
        if (mPage < 2) {
            xrv_hot.scrollToPosition(0)
            mAdapter.clear()
        }
        data.datas?.run {
            mAdapter.addAll(data.datas)
            mAdapter.notifyDataSetChanged()
            if (data.datas.size < 20) {
                xrv_hot.noMoreLoading()
            }
            xrv_hot.refreshComplete()
            if (refresh_hot.isRefreshing) {
                refresh_hot.isRefreshing = false
            }
        }
        mPageLayout.hide()
    }

    override fun setBanner(banner: MutableList<BannerBean>) {
        bannerData.addAll(banner)
        if (arguments?.get("index") == 0) {
            initBannerHeader()
        }
        val hotSearch = layoutInflater.inflate(R.layout.layout_header_hot_more, null)?.apply {
            findViewById<TextView>(R.id.tv_hot_more).setOnClickListener { startActivity(Intent(activity, SearchActivity::class.java)) }
        }
        xrv_hot.addHeaderView(hotSearch)
        hotBean?.apply {
            getPresenter().queryKey(mPage, name)
        }
    }

    override fun setData(data: ArticleBean) {
        setAdapter(data)
        mPageLayout.hide()
    }

    override fun onRefresh() {
        xrv_hot.reset()
        refresh_hot.isRefreshing = true
        mPage = 0
        hotBean?.apply {
            getPresenter().queryKey(mPage, name)
        }
    }

    override fun setFail() {
        mPageLayout.showError()
        mPageLayout.setOnRetryListener(object : PageLayout.OnRetryClickListener {
            override fun onRetry() {
                getPresenter().getBannerHttp()
            }

        })
        refresh_hot.apply {
            isRefreshing = false
            isEnabled = true
        }
    }


    override fun onEvent(event: EventMap.BaseEvent) {
        when (event) {
            is EventMap.WifiImgEvent -> mAdapter.notifyDataSetChanged()
            is EventMap.ToUpEvent -> if ((parentFragment as HotFragment).getCurrentIndex() == mIndex && isVisible) {
                xrv_hot.smoothScrollToPosition(0)
            }
            is EventMap.HomeRefreshEvent -> onRefresh()
        }
    }


    override fun onResume() {
        super.onResume()
        xBanner?.run { startAutoPlay() }
    }

    override fun onStop() {
        super.onStop()
        xBanner?.run { stopAutoPlay() }
    }
}