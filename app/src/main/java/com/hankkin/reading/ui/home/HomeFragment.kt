package com.hankkin.reading.ui.home

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.View
import com.hankkin.library.utils.ToastUtils
import com.hankkin.reading.R
import com.hankkin.reading.adapter.HomeFragmentPagerAdapter
import com.hankkin.reading.base.BaseFragment
import com.hankkin.reading.event.EventMap
import com.hankkin.reading.ui.home.android.AndroidFragment
import com.hankkin.reading.ui.home.hot.HotFragment
import com.hankkin.reading.ui.home.project.ProjectFragment
import com.hankkin.reading.MainActivity
import com.hankkin.reading.ui.home.search.SearchActivity
import com.hankkin.library.utils.RxBusTools
import com.hankkin.reading.utils.ThemeHelper
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * Created by huanghaijie on 2018/5/15.
 */
class HomeFragment : BaseFragment() {

    private val mFgList = listOf<Fragment>(
            AndroidFragment(),
            HotFragment(),
            ProjectFragment()
    )

    @RequiresApi(Build.VERSION_CODES.M)
    override fun initViews() {
        iv_title_menu.setOnClickListener { (activity as MainActivity).openDrawer() }
        iv_title_one.setOnClickListener { setCurrent(0) }
        iv_title_two.setOnClickListener { setCurrent(1) }
        iv_title_three.setOnClickListener { setCurrent(2) }
        iv_search.setOnClickListener { startActivity(Intent(activity,SearchActivity::class.java)) }
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun isHasBus(): Boolean {
        return true
    }

    override fun initData() {
        val mAdapter = HomeFragmentPagerAdapter(childFragmentManager,mFgList )
        vp_home.run {
            adapter = mAdapter
            offscreenPageLimit = 3
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(p0: Int) {
                }

                override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
                }

                override fun onPageSelected(p0: Int) {
                    setCurrent(p0)
                }

            })
        }
        setCurrent(1)
        setFabColor()
        setFabClickListener()
    }

    private fun setFabClickListener(){
        fab_menu_add.setClosedOnTouchOutside(true)
        fab_up.setOnClickListener {
            RxBusTools.getDefault().post(EventMap.ToUpEvent())
            fab_menu_add.close(true)
        }
        fab_refresh.setOnClickListener {
            RxBusTools.getDefault().post(EventMap.HomeRefreshEvent())
            fab_menu_add.close(true)
        }
        fab_write.setOnClickListener {
            context?.let { it1 -> ToastUtils.showInfo(it1,"敬请期待...") }
            fab_menu_add.close(true)
        }
    }

    private fun setFabColor(){
        fab_menu_add.setMenuButtonColorNormalResId(ThemeHelper.getCurrentColor(context))
        fab_up.setColorNormalResId(ThemeHelper.getCurrentColor(context))
        fab_refresh.setColorNormalResId(ThemeHelper.getCurrentColor(context))
        fab_write.setColorNormalResId(ThemeHelper.getCurrentColor(context))

        fab_menu_add.setMenuButtonColorPressedResId(ThemeHelper.getCurrentColor(context))
        fab_up.setColorPressedResId(ThemeHelper.getCurrentColor(context))
        fab_refresh.setColorPressedResId(ThemeHelper.getCurrentColor(context))
        fab_write.setColorPressedResId(ThemeHelper.getCurrentColor(context))

        fab_menu_add.setMenuButtonColorRippleResId(ThemeHelper.getCurrentColor(context))
        fab_up.setColorRippleResId(ThemeHelper.getCurrentColor(context))
        fab_refresh.setColorRippleResId(ThemeHelper.getCurrentColor(context))
        fab_write.setColorRippleResId(ThemeHelper.getCurrentColor(context))
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun setCurrent(index: Int) {
        var isOne = false
        var isTwo = false
        var isThree = false
        when (index) {
            0 -> {
                isOne = true
                appbar.elevation = 1f
            }
            1 -> {
                isTwo = true
                appbar.elevation = 0f
            }
            2 -> {
                isThree = true
                appbar.elevation = 0f
            }
        }
        vp_home.currentItem = index
        iv_title_one.isSelected = isOne
        iv_title_two.isSelected = isTwo
        iv_title_three.isSelected = isThree
    }

    override fun onEvent(event: EventMap.BaseEvent) {
        if (event is EventMap.ChangeFabEvent){
            setFabColor()
        }
    }

}