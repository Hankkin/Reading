package com.hankkin.reading.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.Intent
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
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.bilibili.magicasakura.utils.ThemeUtils
import com.bilibili.magicasakura.widgets.TintLinearLayout
import com.hankkin.library.utils.StatusBarUtil
import com.hankkin.reading.R
import com.hankkin.reading.adapter.MainFragmentAdapter
import com.hankkin.reading.base.BaseActivity
import com.hankkin.reading.control.UserControl
import com.hankkin.reading.event.EventMap
import com.hankkin.reading.ui.home.HomeFragment
import com.hankkin.reading.ui.login.LoginActivity
import com.hankkin.reading.ui.person.PersonFragment
import com.hankkin.reading.ui.person.SettingActivity
import com.hankkin.reading.ui.translate.TranslateFragment
import com.hankkin.reading.ui.user.collect.MyCollectActivity
import com.hankkin.reading.utils.*
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.layout_drawer_header.*

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

        if (UserControl.isLogin()) {
            setNavHeader()
        }

    }

    override fun initViews(savedInstanceState: Bundle?) {

        setStatuBar()

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        val mainAdapter = MainFragmentAdapter(supportFragmentManager, fgList)
        vp_main.adapter = mainAdapter
        vp_main.offscreenPageLimit = DEFAULT_FG_SIZE
        setTabColor(0)
        toggle = ActionBarDrawerToggle(this, drawer_layout, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        toggle!!.syncState()
        drawer_layout.addDrawerListener(toggle!!)
        initNavView()
    }

    fun initNavView() {
        nav_view.inflateHeaderView(R.layout.layout_drawer_header)
        val navView = nav_view.getHeaderView(0)
        navView.findViewById<LinearLayout>(R.id.ll_nav_theme).setOnClickListener(doubleClick)
        navView.findViewById<LinearLayout>(R.id.ll_nav_setting).setOnClickListener(doubleClick)
        navView.findViewById<LinearLayout>(R.id.ll_nav_exit).setOnClickListener(doubleClick)
        navView.findViewById<LinearLayout>(R.id.ll_nav_about).setOnClickListener(doubleClick)
        navView.findViewById<LinearLayout>(R.id.ll_nav_collect).setOnClickListener(doubleClick)
    }

    fun openDrawer() {
        drawer_layout.openDrawer(Gravity.LEFT)
    }

    fun setStatuBar() {
        StatusBarUtil.setColorNoTranslucentForDrawerLayout(this@MainActivity, drawer_layout, resources.getColor(ThemeHelper.getCurrentColor(this)))
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        clearTabColor()
        when (item.itemId) {
            R.id.navigation_dic -> {
                vp_main.setCurrentItem(DICTIONARY_INDEX, false)
                setTabColor(0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_tran -> {
                vp_main.setCurrentItem(TRANSLATE_INDEX, false)
                setTabColor(1)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_person -> {
                vp_main.setCurrentItem(PERSON_INDEX, false)
                setTabColor(2)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    fun setTabColor(index: Int) {
        ((navigation.getChildAt(0) as BottomNavigationMenuView)
                .getChildAt(index) as BottomNavigationItemView).setTextColor(getColorStateList(ThemeHelper.getCurrentColor(this@MainActivity)))

        ((navigation.getChildAt(0) as BottomNavigationMenuView)
                .getChildAt(index) as BottomNavigationItemView).setIconTintList(getColorStateList(ThemeHelper.getCurrentColor(this@MainActivity)))
    }

    fun clearTabColor() {
        for (i in 0..2) {
            ((navigation.getChildAt(0) as BottomNavigationMenuView)
                    .getChildAt(i) as BottomNavigationItemView).setTextColor(getColorStateList(R.color.grey))

            ((navigation.getChildAt(0) as BottomNavigationMenuView)
                    .getChildAt(i) as BottomNavigationItemView).setIconTintList(getColorStateList(R.color.grey))
        }
    }

    fun operateBus() {
        RxBus.getDefault().toObservable(EventMap.BaseEvent::class.java)
                .subscribe({
                    if (it is EventMap.ChangeThemeEvent) {
                        changeTheme()
                    } else if (it is EventMap.LoginEvent) {
                        setNavHeader()
                    }
                })
    }


    fun setNavHeader() {
        val navView = nav_view.getHeaderView(0)
        val tvName = navView.findViewById<TextView>(R.id.tv_username)
        tvName.text = UserControl.getCurrentUser()!!.username
    }

    fun changeTheme() {
        setStatuBar()
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
        setTabColor(vp_main.currentItem)
        RxBus.getDefault().post(EventMap.ChangeFabEvent())
    }


    private val doubleClick = object : DoubleClickListener() {
        override fun onNoDoubleClick(v: View) {
            drawer_layout.closeDrawer(Gravity.START)
            drawer_layout.postDelayed({
                when (v.id) {
                    R.id.ll_nav_theme -> startActivity(Intent(this@MainActivity, SettingActivity::class.java))
                    R.id.ll_nav_setting -> startActivity(Intent(this@MainActivity, SettingActivity::class.java))
                    R.id.ll_nav_about -> ViewHelper.showAboutDialog(this@MainActivity)
                    R.id.ll_nav_collect -> {
                        if (!UserControl.isLogin()) {
                            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                        } else {
                            startActivity(Intent(this@MainActivity, MyCollectActivity::class.java))
                        }
                    }
                    R.id.ll_nav_exit -> finish()
                }
            }, 200)
        }

    }
}
