package com.hankkin.reading.ui.home.hot.hotlist

import android.content.Intent
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.TextView
import com.hankkin.reading.R
import com.hankkin.reading.adapter.AndroidAdapter
import com.hankkin.reading.adapter.base.XRecyclerView
import com.hankkin.reading.base.BaseMvpFragment
import com.hankkin.reading.domain.ArticleBean
import com.hankkin.reading.domain.BannerBean
import com.hankkin.reading.domain.HotBean
import com.hankkin.reading.event.EventMap
import com.hankkin.reading.ui.home.articledetail.CommonWebActivity
import com.hankkin.reading.ui.home.search.SearchActivity
import com.hankkin.reading.glide.GlideUtils
import com.hankkin.reading.utils.ViewHelper
import com.hankkin.reading.view.widget.SWImageView
import com.stx.xhb.xbanner.XBanner
import kotlinx.android.synthetic.main.fragment_hot_list.*

/**
 * Created by huanghaijie on 2018/5/15.
 */
class HotListFragment : BaseMvpFragment<HotListPresenter>(), HotListContact.IView, SwipeRefreshLayout.OnRefreshListener {


    private var bannerData = mutableListOf<BannerBean>()
    private lateinit var mAdapter: AndroidAdapter
    private var banner_project: XBanner? = null

    private lateinit var hotBean: HotBean
    private var mPage: Int = 0

    override fun registerPresenter() = HotListPresenter::class.java

    override fun isHasBus(): Boolean {
        return true
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_hot_list
    }


    override fun initView() {
        ViewHelper.setRefreshLayout(context, true, refresh_hot, this)
        initXrv()
    }

    override fun initData() {
        hotBean = arguments?.getSerializable("bean") as HotBean
        getPresenter().getBannerHttp()
    }

    fun initXrv() {
        ViewHelper.setRefreshLayout(context, true, refresh_hot, this)
        xrv_hot.apply {
            mAdapter = AndroidAdapter()
            layoutManager = LinearLayoutManager(context)
            setPullRefreshEnabled(false)
            clearHeader()
            adapter = mAdapter
            setLoadingListener(object : XRecyclerView.LoadingListener {
                override fun onLoadMore() {
                    getPresenter().queryKey(mPage, hotBean.name)
                }

                override fun onRefresh() {
                }

            })
        }


    }

    fun initBannerHeader() {
        val layoutBanner = layoutInflater.inflate(R.layout.layout_project_banner_header, null)
        banner_project = layoutBanner.findViewById<XBanner>(R.id.banner_project)
        val urlList = mutableListOf<String>()
        val contentList = mutableListOf<String>()
        for (bannerBean in bannerData) {
            urlList.add(bannerBean.imagePath)
            contentList.add(bannerBean.title)
        }
        banner_project?.run {
            setData(R.layout.layout_banner_imageview, urlList, contentList)
            viewPager.pageMargin = 20
            loadImage { banner, model, view, position ->
                val iv = view.findViewById<SWImageView>(R.id.iv_banner_item)
                GlideUtils.loadImageView(context, model as String?, iv)
            }
            setOnItemClickListener { banner, model, position -> context?.let { CommonWebActivity.loadUrl(it, bannerData[position].url, bannerData[position].title) } }
        }
        xrv_hot.addHeaderView(layoutBanner)
    }

    fun setAdapter(data: ArticleBean) {
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
        getPresenter().queryKey(mPage, hotBean.name)
    }

    override fun setData(data: ArticleBean) {
        setAdapter(data)
    }

    override fun onRefresh() {
        xrv_hot.reset()
        refresh_hot.isRefreshing = true
        mPage = 0
        getPresenter().queryKey(mPage, hotBean.name)
    }


    override fun onEvent(event: EventMap.BaseEvent) {
        if (event is EventMap.WifiImgEvent) {
            mAdapter.notifyDataSetChanged()
        }
    }


    override fun onResume() {
        super.onResume()
        banner_project?.run { startAutoPlay() }
    }

    override fun onStop() {
        super.onStop()
        banner_project?.run { stopAutoPlay() }
    }
}