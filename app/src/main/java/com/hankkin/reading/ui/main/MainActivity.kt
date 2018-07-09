package com.hankkin.reading.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.design.internal.BottomNavigationItemView
import android.support.design.internal.BottomNavigationMenuView
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Gravity
import android.view.View
import android.widget.Toast
import com.bilibili.magicasakura.utils.ThemeUtils
import com.hankkin.library.utils.StatusBarUtil
import com.hankkin.reading.R
import com.hankkin.reading.adapter.MainFragmentAdapter
import com.hankkin.reading.base.BaseActivity
import com.hankkin.reading.event.EventMap
import com.hankkin.reading.ui.home.HomeFragment
import com.hankkin.reading.ui.person.PersonFragment
import com.hankkin.reading.ui.translate.TranslateFragment
import com.hankkin.reading.utils.LogUtils
import com.hankkin.reading.utils.RxBus
import com.hankkin.reading.utils.ThemeHelper
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_main.*

@SuppressLint("RestrictedApi")
@RequiresApi(Build.VERSION_CODES.M)
class MainActivity : BaseActivity() {

    private var toggle: ActionBarDrawerToggle? = null

    companion object {
        private const val DEFAULT_FG_SIZE = 3
        private const val DICTIONARY_INDEX = 0
        private const val TRANSLATE_INDEX = 1
        private const val PERSON_INDEX = 2
    }


    private val fgList = listOf<Fragment>(
            HomeFragment(),
            TranslateFragment(),
            PersonFragment()
    )

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initData() {
        operateBus()
        RxPermissions(this).requestEach(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_CALENDAR,
                Manifest.permission.READ_CALL_LOG,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_SMS,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.SEND_SMS,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe { p0 ->
            if (p0.granted) {
                LogUtils.d(p0.name + " is granted")
            } else if (p0.shouldShowRequestPermissionRationale) {
                Toast.makeText(activity, "请在设置-应用-权限管理中开启权限", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, "权限被拒绝，无法启用存储功能", Toast.LENGTH_SHORT).show()
            }
        }


    }

    override fun initViews(savedInstanceState: Bundle?) {

        StatusBarUtil.setColorNoTranslucentForDrawerLayout(this@MainActivity, drawer_layout,
                resources.getColor(ThemeHelper.getCurrentColor(this)))

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        val mainAdapter = MainFragmentAdapter(supportFragmentManager, fgList)
        vp_home.adapter = mainAdapter
        vp_home.offscreenPageLimit = DEFAULT_FG_SIZE
        setTabColor(0)
        toggle = ActionBarDrawerToggle(this,drawer_layout,R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        toggle!!.syncState()
        drawer_layout.addDrawerListener(toggle!!)

        nav_view.inflateHeaderView(R.layout.layout_drawer_header)
    }


    fun openDrawer(){
        drawer_layout.openDrawer(Gravity.LEFT)
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        clearTabColor()
        when (item.itemId) {
            R.id.navigation_dic -> {
                vp_home.setCurrentItem(DICTIONARY_INDEX, false)
                setTabColor(0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_tran -> {
                vp_home.setCurrentItem(TRANSLATE_INDEX, false)
                setTabColor(1)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_person -> {
                vp_home.setCurrentItem(PERSON_INDEX, false)
                setTabColor(2)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    fun setTabColor(index: Int){
        ((navigation.getChildAt(0) as BottomNavigationMenuView)
                .getChildAt(index) as BottomNavigationItemView).
                setTextColor(getColorStateList(ThemeHelper.getCurrentColor(this@MainActivity)))

        ((navigation.getChildAt(0) as BottomNavigationMenuView)
                .getChildAt(index) as BottomNavigationItemView).
                setIconTintList(getColorStateList(ThemeHelper.getCurrentColor(this@MainActivity)))
    }
    fun clearTabColor(){
        for (i in 0..2){
            ((navigation.getChildAt(0) as BottomNavigationMenuView)
                    .getChildAt(i) as BottomNavigationItemView).
                    setTextColor(getColorStateList(R.color.grey))

            ((navigation.getChildAt(0) as BottomNavigationMenuView)
                    .getChildAt(i) as BottomNavigationItemView).
                    setIconTintList(getColorStateList(R.color.grey))
        }
    }

    fun operateBus() {
        RxBus.getDefault().toObservable(EventMap.BaseEvent::class.java)
                .subscribe({
                    if (it is EventMap.ChangeThemeEvent) {
                        changeTheme()
                    }
                })
    }

    fun changeTheme() {
        ThemeUtils.refreshUI(this, object : ThemeUtils.ExtraRefreshable {
            override fun refreshSpecificView(view: View?) {
            }
            override fun refreshGlobal(activity: Activity?) {
                if (Build.VERSION.SDK_INT >= 21) {
                    val context = this@MainActivity
                    val taskDescription = ActivityManager.TaskDescription(null, null,
                            ThemeUtils.getThemeAttrColor(context, android.R.attr.colorPrimary))
                    setTaskDescription(taskDescription)
                    window.statusBarColor = ThemeUtils.getColorById(context, R.color.theme_color_primary_dark)
                }
            }

        })
        setTabColor(vp_home.currentItem)
        RxBus.getDefault().post(EventMap.ChangeFabEvent())
    }
}
