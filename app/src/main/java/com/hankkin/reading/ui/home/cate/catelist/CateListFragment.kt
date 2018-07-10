package com.hankkin.reading.ui.home.project.projectlist

import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.hankkin.reading.R
import com.hankkin.reading.adapter.AndroidAdapter
import com.hankkin.reading.adapter.ProjectListAdapter
import com.hankkin.reading.adapter.base.XRecyclerView
import com.hankkin.reading.base.BaseMvpFragment
import com.hankkin.reading.domain.ArticleBean
import com.hankkin.reading.domain.BannerBean
import com.hankkin.reading.domain.CateBean
import com.hankkin.reading.ui.home.cate.catelist.CateListContact
import com.hankkin.reading.ui.home.cate.catelist.CateListPresenter
import com.hankkin.reading.utils.GlideUtils
import com.hankkin.reading.utils.ViewHelper
import com.hankkin.reading.view.widget.SWImageView
import com.stx.xhb.xbanner.XBanner
import kotlinx.android.synthetic.main.fragment_cate_list.*

/**
 * Created by huanghaijie on 2018/5/15.
 */
class CateListFragment : BaseMvpFragment<CateListPresenter>(), CateListContact.IView,SwipeRefreshLayout.OnRefreshListener {

    private var bannerData = mutableListOf<BannerBean>()
    private lateinit var mAdapter: AndroidAdapter
    private var banner_project: XBanner? = null

    private lateinit var cateBean: CateBean
    private var mPage: Int = 0


    override fun getLayoutId(): Int {
        return R.layout.fragment_cate_list
    }


    override fun initView() {
        ViewHelper.setRefreshLayout(context,true,refresh_cate,this)
        initXrv()
    }
    
    override fun initData() {
        cateBean = (arguments!!.getSerializable("bean")) as CateBean
        getPresenter().getBannerHttp()
    }
    
    fun initXrv(){
        ViewHelper.setRefreshLayout(context,true,refresh_cate,this)
        mAdapter = AndroidAdapter()
        val linearLayoutManager = LinearLayoutManager(context)
        xrv_cate.layoutManager = linearLayoutManager
        xrv_cate.setPullRefreshEnabled(false)
        xrv_cate.clearHeader()
        xrv_cate.adapter = mAdapter
        xrv_cate.setLoadingListener(object : XRecyclerView.LoadingListener {
            override fun onLoadMore() {
                getPresenter().getCateList(mPage,cateBean.id)
            }

            override fun onRefresh() {
            }

        })
    }

    fun initBannerHeader(){
        val layoutBanner = layoutInflater.inflate(R.layout.layout_project_banner_header,null)
        banner_project = layoutBanner.findViewById<XBanner>(R.id.banner_project)
        val urlList = mutableListOf<String>()
        val contentList = mutableListOf<String>()
        for (bannerBean in bannerData){
            urlList.add(bannerBean.imagePath)
            contentList.add(bannerBean.title)
        }
        banner_project!!.setData(R.layout.layout_banner_imageview,urlList,contentList)
        banner_project!!.viewPager.pageMargin = 20
        banner_project!!.loadImage(object : XBanner.XBannerAdapter{
            override fun loadBanner(banner: XBanner?, model: Any?, view: View, position: Int) {
                val iv = view.findViewById<SWImageView>(R.id.iv_banner_item)
                GlideUtils.loadImageView(context, model as String?, iv)
            }
        })
        xrv_cate.addHeaderView(layoutBanner)
    }

    fun setAdapter(data: ArticleBean){
        mPage = data.curPage
        if (mPage < 2) {
            xrv_cate.scrollToPosition(0)
            mAdapter.clear()
        }
        mAdapter.addAll(data.datas)
        mAdapter.notifyDataSetChanged()
        if (data.datas.size < 20) {
            xrv_cate.noMoreLoading()
        }
        xrv_cate.refreshComplete()
        if (refresh_cate.isRefreshing) {
            refresh_cate.isRefreshing = false
        }
    }

    override fun setBanner(banner: MutableList<BannerBean>) {
        bannerData.addAll(banner)
        val index = arguments!!.get("index")
        if (index == 0){
            initBannerHeader()
        }
        getPresenter().getCateList(mPage,cateBean.id)
    }

    override fun setCateList(data: ArticleBean) {
        setAdapter(data)
    }

    override fun onRefresh() {

    }

    override fun registerPresenter() = CateListPresenter::class.java



    override fun onResume() {
        super.onResume()
        if(banner_project != null){
            banner_project!!.startAutoPlay()
        }
    }

    override fun onStop() {
        super.onStop()
        if(banner_project != null) {
            banner_project!!.stopAutoPlay()
        }
    }
}