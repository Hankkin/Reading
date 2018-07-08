package com.hankkin.reading.ui.home.project.projectlist

import android.view.View
import android.widget.ImageView
import com.hankkin.reading.R
import com.hankkin.reading.base.BaseMvpFragment
import com.hankkin.reading.domain.BannerBean
import com.hankkin.reading.utils.GlideUtils
import com.stx.xhb.xbanner.XBanner
import kotlinx.android.synthetic.main.fragment_post_list.*

/**
 * Created by huanghaijie on 2018/5/15.
 */
class ProjectListFragment : BaseMvpFragment<ProjectListPresenter>(),ProjectListContact.IView {

    private var bannerData = mutableListOf<BannerBean>()

    override fun setBanner(banner: MutableList<BannerBean>) {
        bannerData.addAll(banner)
        val index = arguments!!.get("index")
        if (index == 0){
            initBanner()
        }
    }


     override fun registerPresenter() = ProjectListPresenter::class.java


    override fun initView() {
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_post_list
    }

    override fun initData() {
        getPresenter().getBannerHttp()
    }

    fun initBanner(){
        rl_project_banner.visibility = View.VISIBLE
        val urlList = mutableListOf<String>()
        val contentList = mutableListOf<String>()
        for (bannerBean in bannerData){
            urlList.add(bannerBean.imagePath)
            contentList.add(bannerBean.title)
        }
        banner_project.setData(R.layout.layout_banner_imageview,urlList,contentList)
        banner_project.viewPager.pageMargin = 20
        banner_project.loadImage(object : XBanner.XBannerAdapter{
            override fun loadBanner(banner: XBanner?, model: Any?, view: View?, position: Int) {
                GlideUtils.loadImageView(context, model as String?, view as ImageView?)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        banner_project.startAutoPlay()
    }

    override fun onStop() {
        super.onStop()
        banner_project.stopAutoPlay()
    }
}