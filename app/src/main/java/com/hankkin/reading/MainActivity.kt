package com.hankkin.reading

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
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
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.bilibili.magicasakura.utils.ThemeUtils
import com.bumptech.glide.Glide
import com.hankkin.library.fuct.decode.ImageUtil
import com.hankkin.library.utils.LogUtils
import com.hankkin.library.utils.RxBusTools
import com.hankkin.library.utils.SPUtils
import com.hankkin.library.utils.ToastUtils
import com.hankkin.library.widget.toasty.Toasty
import com.hankkin.reading.adapter.MainFragmentAdapter
import com.hankkin.reading.base.BaseActivity
import com.hankkin.reading.common.Constant
import com.hankkin.reading.control.UserControl
import com.hankkin.reading.event.EventMap
import com.hankkin.reading.ui.gank.GankFragment
import com.hankkin.reading.ui.home.HomeFragment
import com.hankkin.reading.ui.login.LoginActivity
import com.hankkin.reading.ui.person.MyCollectActivity
import com.hankkin.reading.ui.person.PersonFragment
import com.hankkin.reading.ui.person.SettingActivity
import com.hankkin.reading.ui.person.ThemeActivity
import com.hankkin.reading.ui.todo.ToDoActivity
import com.hankkin.reading.ui.tools.ToolsFragment
import com.hankkin.reading.ui.tools.acount.AccountListActivity
import com.hankkin.reading.ui.tools.wordnote.WordNoteActivity
import com.hankkin.reading.ui.wxarticle.WxArticleFragment
import com.hankkin.reading.utils.*
import com.hankkin.reading.view.BottomNavigationViewHelper
import com.hankkin.reading.view.widget.SWImageView
import com.tbruyelle.rxpermissions2.RxPermissions
import com.zhihu.matisse.Matisse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_drawer_header.*

@SuppressLint("RestrictedApi")
@RequiresApi(Build.VERSION_CODES.M)
class MainActivity : BaseActivity() {

    private var toggle: ActionBarDrawerToggle? = null

    companion object {
        private const val DEFAULT_FG_SIZE = 3
        private const val DICTIONARY_INDEX = 0
        private const val TODO_INDEX = 2
        private const val TRANSLATE_INDEX = 3
        private const val GANK_INDEX = 1
    }


    private val fgList = mutableListOf<Fragment>(
            HomeFragment(),
            GankFragment(),
            WxArticleFragment(),
            PersonFragment()
    )

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun isHasBus(): Boolean {
        return true
    }

    override fun isSupportSwipeBack() = true


    @SuppressLint("CheckResult")
    override fun initData() {
        SPUtils.put(Constant.SP_KEY.WIFI_IMG, 1)//默认加载图片
        RxPermissions(this).requestEach(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                Manifest.permission.READ_CALENDAR,
//                Manifest.permission.READ_CALL_LOG,
//                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_PHONE_STATE,
//                Manifest.permission.READ_SMS,
//                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA,
//                Manifest.permission.CALL_PHONE,
//                Manifest.permission.SEND_SMS,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe { p0 ->
            when {
                p0.granted -> LogUtils.d(p0.name + " is granted")
                p0.shouldShowRequestPermissionRationale -> Toast.makeText(activity, "请在设置-应用-权限管理中开启权限", Toast.LENGTH_SHORT).show()
                else -> Toast.makeText(activity, "权限被拒绝，无法启用存储功能", Toast.LENGTH_SHORT).show()
            }
        }

        if (UserControl.isLogin()) {
            setNavHeader()
        }
        checkSync()

    }

    override fun initViews(savedInstanceState: Bundle?) {

        setStatuBar()
        if (SPUtils.getInt(Constant.SP_KEY.PERSON_THEME) == 1) {
            fgList.removeAt(3)
            fgList.add(ToolsFragment())
        }

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

    private fun initNavView() {
        nav_view.inflateHeaderView(R.layout.layout_drawer_header)
        nav_view.getHeaderView(0)?.run {
            findViewById<LinearLayout>(R.id.ll_nav_theme).setOnClickListener(click)
            findViewById<LinearLayout>(R.id.ll_nav_setting).setOnClickListener(click)
            findViewById<LinearLayout>(R.id.ll_nav_exit).setOnClickListener(click)
            findViewById<LinearLayout>(R.id.ll_nav_about).setOnClickListener(click)
            findViewById<LinearLayout>(R.id.ll_nav_collect).setOnClickListener(click)
            findViewById<LinearLayout>(R.id.ll_nav_wordnote).setOnClickListener(click)
            findViewById<LinearLayout>(R.id.ll_nav_account).setOnClickListener(click)
            findViewById<LinearLayout>(R.id.ll_nav_todo).setOnClickListener(click)
            findViewById<SWImageView>(R.id.iv_avatar).setOnClickListener(click)

            findViewById<ImageView>(R.id.iv_drawer_header_bg).apply {
                setOnClickListener { changeHeader() }
                if (SPUtils.getString(Constant.COMMON.HEADER_BG).isNotEmpty()) {
                    Glide.with(this@MainActivity).load(SPUtils.getString(Constant.COMMON.HEADER_BG)).into(this)
                }
            }

        }
        BottomNavigationViewHelper.disableShiftMode(navigation)

    }

    fun openDrawer() {
        drawer_layout.openDrawer(Gravity.LEFT)
    }

    private fun setStatuBar() {
        MyStatusBarUtil.setColorForSwipeBackDrawerLayout(this, resources.getColor(ThemeHelper.getCurrentColor(this)), 0)
        MyStatusBarUtil.setColorNoTranslucentForDrawerLayout(this@MainActivity, drawer_layout, resources.getColor(ThemeHelper.getCurrentColor(this)))
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_dic -> {
                setTabColor(DICTIONARY_INDEX)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_todo -> {
                setTabColor(TODO_INDEX)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_tran -> {
                setTabColor(TRANSLATE_INDEX)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_gank -> {
                setTabColor(GANK_INDEX)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    fun setTabColor(index: Int) {
        clearTabColor()
        vp_main.setCurrentItem(index, false)
        ((navigation.getChildAt(0) as BottomNavigationMenuView)
                .getChildAt(index) as BottomNavigationItemView).setTextColor(resources.getColorStateList(ThemeHelper.getCurrentColor(this@MainActivity)))

        ((navigation.getChildAt(0) as BottomNavigationMenuView)
                .getChildAt(index) as BottomNavigationItemView).setIconTintList(resources.getColorStateList(ThemeHelper.getCurrentColor(this@MainActivity)))
    }

    private fun clearTabColor() {
        for (i in 0 until fgList.size
        ) {
            ((navigation.getChildAt(0) as BottomNavigationMenuView)
                    .getChildAt(i) as BottomNavigationItemView).setTextColor(resources.getColorStateList(R.color.grey))

            ((navigation.getChildAt(0) as BottomNavigationMenuView)
                    .getChildAt(i) as BottomNavigationItemView).setIconTintList(resources.getColorStateList(R.color.grey))
        }
    }


    private fun setNavHeader() {
        val navView = nav_view.getHeaderView(0)
        val tvName = navView.findViewById<TextView>(R.id.tv_username)
        tvName.text = UserControl.getCurrentUser()!!.username
    }

    private fun changeTheme() {
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

        Toasty.Config.getInstance()
                .setInfoColor(resources.getColor(ThemeHelper.getCurrentColor(this)))
                .apply(application)
        changeLogo()
        setTabColor(vp_main.currentItem)
        RxBusTools.getDefault().post(EventMap.ChangeFabEvent())
    }


    private val click = View.OnClickListener { v ->
        drawer_layout.closeDrawer(Gravity.START)
        drawer_layout.postDelayed({
            when (v.id) {
                R.id.ll_nav_theme -> startActivity(Intent(this@MainActivity, ThemeActivity::class.java))
                R.id.ll_nav_setting -> startActivity(Intent(this@MainActivity, SettingActivity::class.java))
                R.id.ll_nav_about -> ViewHelper.showAboutDialog(this@MainActivity)
                R.id.ll_nav_collect -> startActivity(Intent(this@MainActivity, if (!UserControl.isLogin()) LoginActivity::class.java else MyCollectActivity::class.java))
                R.id.ll_nav_exit -> finish()
                R.id.ll_nav_wordnote -> startActivity(Intent(this@MainActivity, WordNoteActivity::class.java))
                R.id.ll_nav_account -> startActivity(Intent(this@MainActivity, AccountListActivity::class.java))
                R.id.ll_nav_todo -> startActivity(Intent(this@MainActivity, ToDoActivity::class.java))
                R.id.iv_avatar -> startActivity(Intent(this@MainActivity, if (!UserControl.isLogin()) LoginActivity::class.java else LoginActivity::class.java))
            }
        }, 200)
    }

    private fun changeHeader() {
        ViewHelper.selectImg(this)
    }

    private fun changeLogo() {
        if (SPUtils.getInt(Constant.SP_KEY.LOGO) == 0) return
        enableCompont(ThemeHelper.getNameStr(this))
        disableComponent("SplashActivity")
        for (str in ThemeHelper.themeList) {
            if (str != ThemeHelper.getNameStr(this)) {
                disableComponent(str)
            }
        }
    }

    private fun checkSync() {
        if (DBUtils.isAutoSync(this)) {
            SPUtils.put(Constant.SP_KEY.LOCK_BACKUP_OPEN, 1)
            ViewHelper.showConfirmDialog(this, resources.getString(R.string.setting_lock_data_restore_hint),
                    MaterialDialog.SingleButtonCallback { dialog, which ->
                        val disposable = Observable.create<Boolean> {
                            try {
                                DBUtils.loadDBData(this)
                                it.onNext(true)
                                it.onComplete()
                            } catch (e: Exception) {
                                it.onError(e)
                            }
                        }
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({
                                    LoadingUtils.hideLoading()
                                    ToastUtils.showInfo(this, resources.getString(R.string.setting_lock_data_restore_success))
                                }, {
                                    LoadingUtils.hideLoading()
                                    ToastUtils.showError(this, resources.getString(R.string.setting_lock_data_restore_fail))
                                })
                        LoadingUtils.showLoading(this)
                        disposables.add(disposable)
                    })
        }
    }

    private fun enableCompont(compontName: String) {
        packageManager.setComponentEnabledSetting(ComponentName(baseContext, packageName + "." + compontName),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP)
    }

    private fun disableComponent(compontName: String) {
        packageManager.setComponentEnabledSetting(ComponentName(baseContext, packageName + "." + compontName),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP)
    }

    override fun onEvent(event: EventMap.BaseEvent) {
        if (event is EventMap.ChangeThemeEvent) {
            changeTheme()
        } else if (event is EventMap.LoginEvent) {
            setNavHeader()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.COMMON.REQUEST_CODE_CHOOSE && resultCode == Activity.RESULT_OK) {
            val uris = Matisse.obtainResult(data)
            val path = ImageUtil.getImageAbsolutePath(this, uris[0])
            SPUtils.put(Constant.COMMON.HEADER_BG, path)
            Glide.with(this).load(path).into(iv_drawer_header_bg)

        }
    }
}
