package com.hankkin.reading.ui.home.project.projectlist

import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.hankkin.reading.R
import com.hankkin.reading.adapter.ProjectListAdapter
import com.hankkin.reading.adapter.base.XRecyclerView
import com.hankkin.reading.base.BaseMvpFragment
import com.hankkin.reading.domain.BannerBean
import com.hankkin.reading.utils.GlideUtils
import com.hankkin.reading.view.widget.SWImageView
import com.stx.xhb.xbanner.XBanner
import kotlinx.android.synthetic.main.fragment_post_list.*

/**
 * Created by huanghaijie on 2018/5/15.
 */
class ProjectListFragment : BaseMvpFragment<ProjectListPresenter>(),ProjectListContact.IView,SwipeRefreshLayout.OnRefreshListener {

    private var bannerData = mutableListOf<BannerBean>()
    private lateinit var mAdapter: ProjectListAdapter
    private var banner_project: XBanner? = null

    override fun initView() {
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_post_list
    }

    override fun initData() {
        getPresenter().getBannerHttp()
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
        xrv_project.addHeaderView(layoutBanner)
    }

    fun setAdapter(){
        val data = mutableListOf<String>()
        for (a in 0..20){
            data.add("item"+a)
        }
        mAdapter = ProjectListAdapter()
        mAdapter.addAll(data)
        xrv_project.layoutManager = LinearLayoutManager(context)
        xrv_project.setPullRefreshEnabled(false)
        xrv_project.clearHeader()
        xrv_project.adapter = mAdapter
        refresh_project.setOnRefreshListener(this)
        refresh_project.isRefreshing = true
        xrv_project.setLoadingListener(object : XRecyclerView.LoadingListener {
            override fun onLoadMore() {
                android.os.Handler().postDelayed({
                    xrv_project.reset()
                    mAdapter.add("Item"+mAdapter.data.size+1)
                    mAdapter.notifyDataSetChanged()
                    xrv_project.refreshComplete()
                },100)

            }

            override fun onRefresh() {
            }

        })
        refresh_project.isRefreshing = false
    }

    override fun setBanner(banner: MutableList<BannerBean>) {
        setAdapter()
        bannerData.addAll(banner)
        val index = arguments!!.get("index")
        if (index == 0){
            initBannerHeader()
        }
    }

    override fun onRefresh() {

    }

    override fun registerPresenter() = ProjectListPresenter::class.java



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