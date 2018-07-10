package com.hankkin.reading.ui.home.project.projectlist

import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import com.hankkin.reading.R
import com.hankkin.reading.adapter.ProjectListAdapter
import com.hankkin.reading.adapter.base.XRecyclerView
import com.hankkin.reading.base.BaseMvpFragment
import com.hankkin.reading.domain.BannerBean
import com.stx.xhb.xbanner.XBanner
import kotlinx.android.synthetic.main.fragment_project_list.*

/**
 * Created by huanghaijie on 2018/5/15.
 */
class ProjectListFragment : BaseMvpFragment<ProjectListPresenter>(),ProjectListContact.IView,SwipeRefreshLayout.OnRefreshListener {

    private var bannerData = mutableListOf<BannerBean>()
    private lateinit var mAdapter: ProjectListAdapter
    private var banner_project: XBanner? = null


    override fun getLayoutId(): Int {
        return R.layout.fragment_project_list
    }

    override fun initData() {
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