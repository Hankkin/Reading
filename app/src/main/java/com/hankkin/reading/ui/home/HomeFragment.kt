package com.hankkin.reading.ui.home

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.View
import com.hankkin.reading.R
import com.hankkin.reading.adapter.HomeFragmentPagerAdapter
import com.hankkin.reading.base.BaseFragment
import com.hankkin.reading.event.EventMap
import com.hankkin.reading.ui.home.android.AndroidFragment
import com.hankkin.reading.ui.home.cate.CateFragment
import com.hankkin.reading.ui.home.project.ProjectFragment
import com.hankkin.reading.ui.main.MainActivity
import com.hankkin.reading.utils.RxBus
import com.hankkin.reading.utils.ThemeHelper
import com.hankkin.reading.utils.ToastUtils
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * Created by huanghaijie on 2018/5/15.
 */
class HomeFragment : BaseFragment() {

    private val mFgList = listOf<Fragment>(
            AndroidFragment(),
            CateFragment(),
            ProjectFragment()
    )

    @RequiresApi(Build.VERSION_CODES.M)
    override fun initViews() {
        iv_title_menu.setOnClickListener { (activity as MainActivity).openDrawer() }
        iv_title_one.setOnClickListener { setCurrent(0) }
        iv_title_two.setOnClickListener { setCurrent(1) }
        iv_title_three.setOnClickListener { setCurrent(2) }
    }

    public fun newInstance(index: Int) {
        val fragment = HomeFragment()
        val args = Bundle()
        args.putInt("index", index)
        fragment.arguments = args
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun initData() {
        val adapter = HomeFragmentPagerAdapter(childFragmentManager,mFgList )
        vp_home.adapter = adapter
        vp_home.offscreenPageLimit = 2
        vp_home.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(p0: Int) {
                setCurrent(p0)
            }

        })
        setCurrent(0)
        setFabColor()
        setFabClickListener()

        RxBus.getDefault().toObservable(EventMap.ChangeFabEvent::class.java)
                .subscribe({
                    setFabColor()
                })
    }

    fun setFabClickListener(){
        fab_menu_add.setClosedOnTouchOutside(true)
        fab_up.setOnClickListener {
            RxBus.getDefault().post(EventMap.ToUpEvent())
            fab_menu_add.close(true)
        }
        fab_refresh.setOnClickListener {
            RxBus.getDefault().post(EventMap.HomeRefreshEvent())
            fab_menu_add.close(true)
        }
        fab_write.setOnClickListener {
            ToastUtils.showToast(context,"敬请期待...")
            fab_menu_add.close(true)
        }
    }

    fun setFabColor(){
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
                fab_menu_add.visibility = View.VISIBLE
                appbar.elevation = 1f
            }
            1 -> {
                isTwo = true
                fab_menu_add.visibility = View.GONE
                appbar.elevation = 0f
            }
            2 -> {
                isThree = true
                fab_menu_add.visibility = View.GONE
                appbar.elevation = 1f
            }
        }
        vp_home.currentItem = index
        iv_title_one.isSelected = isOne
        iv_title_two.isSelected = isTwo
        iv_title_three.isSelected = isThree
    }

}