package com.hankkin.reading.ui.person

import android.app.Activity
import android.app.ActivityManager
import android.content.Intent
import android.os.Build
import android.support.design.widget.AppBarLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.bilibili.magicasakura.utils.ThemeUtils
import com.cocosw.bottomsheet.BottomSheet
import com.hankkin.library.utils.AppUtils
import com.hankkin.library.utils.LogUtils
import com.hankkin.library.utils.SPUtils
import com.hankkin.library.utils.ToastUtils
import com.hankkin.reading.R
import com.hankkin.reading.adapter.PersonListAdapter
import com.hankkin.reading.base.BaseMvpFragment
import com.hankkin.reading.common.Constant
import com.hankkin.reading.control.UserControl
import com.hankkin.reading.domain.PersonListBean
import com.hankkin.reading.event.EventMap
import com.hankkin.reading.ui.login.LoginActivity
import com.hankkin.reading.ui.user.collect.MyCollectActivity
import com.hankkin.reading.utils.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_person.*

/**
 * Created by huanghaijie on 2018/5/15.
 */
class PersonFragment : BaseMvpFragment<PersonContract.IPresenter>(), PersonContract.IView {

    private lateinit var mAdapter: PersonListAdapter
    private var mCurrentTheme: Int = 0
    private lateinit var mThemeBuilder: BottomSheet.Builder

    override fun registerPresenter() = PersonPresenter::class.java

    override fun getLayoutId(): Int {
        return R.layout.fragment_person
    }

    override fun isHasBus(): Boolean {
        return true
    }

    override fun initData() {
        mAdapter = PersonListAdapter()
        mAdapter.data.add(PersonListBean(R.mipmap.icon_person_star, resources.getString(R.string.person_follow)))
        mAdapter.data.add(PersonListBean(R.mipmap.icon_person_list_theme, resources.getString(R.string.person_theme)))
        mAdapter.data.add(PersonListBean(R.mipmap.icon_person_db, resources.getString(R.string.setting_db)))
        mAdapter.data.add(PersonListBean(R.mipmap.icon_person_set_list, resources.getString(R.string.setting)))
        xrv_person_lisy.layoutManager = LinearLayoutManager(context)
        xrv_person_lisy.adapter = mAdapter
        mCurrentTheme = ThemeHelper.getTheme(context)
        tv_person_version.text = context?.let { "当前版本："+AppUtils.getVersionName(it) }
        initThemeBuilder()
    }

    override fun initView() {
        initHeaderAnim()
        iv_person_avatar.setOnClickListener { llAvatarClick() }
        iv_person_set.setOnClickListener { startActivity(Intent(context, SettingActivity::class.java)) }
        ll_person_new.setOnClickListener { context?.let { it1 -> ToastUtils.showInfo(it1,"待开发") } }
        ll_person_todo.setOnClickListener { context?.let { it1 -> ToastUtils.showInfo(it1,"待开发") } }
        ll_person_done.setOnClickListener { context?.let { it1 -> ToastUtils.showInfo(it1,"待开发") } }
        iv_person_feedback.setOnClickListener {
            activity?.let { it1 -> CommonUtils.feedBack(it1) }
        }
    }

    fun initHeaderAnim() {
        val name = if (!UserControl.isLogin()) {
            "未登录"
        } else UserControl.getCurrentUser()!!.username
        tv_person_name.text = name
        ll_person_header.viewTreeObserver.addOnGlobalLayoutListener {
            val height = ll_person_header.height
            app_bar.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
                override fun onStateChanged(appBarLayout: AppBarLayout?, state: State?) {
                }

                override fun onAppBarOffsetChanged(state: State?, appBarLayout: AppBarLayout?, i: Int) {
                    when (state) {
                        AppBarStateChangeListener.State.COLLAPSED -> {
                            tv_person_title.visibility = View.VISIBLE
                            ll_person_header.visibility = View.GONE
                        }
                        else -> {
                            tv_person_title.visibility = View.GONE
                            ll_person_header.visibility = View.VISIBLE
                        }
                    }
                    LogUtils.e(">>>>>alpha" + i )
                    if (i == 0) {
                        ll_person_header.alpha = 1F

                    } else if (i in 1..(height - 30)) {
                        ll_person_header.alpha = ((height-i).toFloat() / height)
                    }else if (i>height - 30){
                        ll_person_header.alpha = 0F
                    }
                }

            })
        }

    }

    fun llAvatarClick() {
        if (!UserControl.isLogin()) {
            startActivity(Intent(context, LoginActivity::class.java))
        } else {
            startActivity(Intent(context, PersonInfoActivity::class.java))
        }
    }


    fun setUserHeader() {
        if (UserControl.isLogin()) {
            val user = UserControl.getCurrentUser()
            tv_person_name.text = user!!.username
        } else {
            tv_person_name.text = resources.getString(R.string.person_no_login)
        }
    }



    override fun onEvent(event: EventMap.BaseEvent) {
        if (event is EventMap.LoginEvent) {
            setUserHeader()
        } else if (event is EventMap.PersonClickEvent) {
            when (event.index) {
                0 -> startActivity(Intent(context,MyCollectActivity::class.java))
                1 -> mThemeBuilder.show()
                2 -> syncData()
                3 -> context!!.startActivity(Intent(context, SettingActivity::class.java))
            }
        }
    }

    /**
     * 数据还原
     */
    private fun syncData() {
        if (context != null) {
            if (SPUtils.getInt(Constant.SP_KEY.LOCK_BACKUP_OPEN) == 1) {
                if (DBUtils.isNeedSync(context!!)) {
                    LoadingUtils.showLoading(context)
                    val disposable = Observable.create<Boolean> {
                        try {
                            DBUtils.loadDBData(context!!)
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
                                ToastUtils.showInfo(context!!, resources.getString(R.string.setting_lock_data_restore_success))
                            }, {
                                LoadingUtils.hideLoading()
                                ToastUtils.showError(context!!, resources.getString(R.string.setting_lock_data_restore_fail))
                            })
                    disposables.add(disposable)
                } else {
                    ToastUtils.showInfo(context!!, resources.getString(R.string.setting_lock_backup_new))
                }
            } else {
                ViewHelper.showConfirmDialog(context!!, context!!.resources.getString(R.string.setting_db_hint),
                        MaterialDialog.SingleButtonCallback { dialog, which ->
                            context!!.startActivity(Intent(context, SettingActivity::class.java))
                        })
            }
        }
    }

    fun initThemeBuilder() {
        mThemeBuilder = BottomSheet.Builder(context, R.style.BottomSheet_StyleDialog)
                .title(R.string.setting_theme)
                .sheet(R.menu.theme_bottomsheet)
                .listener { dialog, which ->
                    changeTheme(which)
                }
    }

    /**
     * 修改主题颜色
     */
    fun changeTheme(themeValue: Int) {
        mCurrentTheme = when (themeValue) {
            R.id.yima -> ThemeHelper.COLOR_YIMA
            R.id.kuan -> ThemeHelper.COLOR_KUAN
            R.id.bili -> ThemeHelper.COLOR_BILI
            R.id.yidi -> ThemeHelper.COLOR_YIDI
            R.id.shuiya -> ThemeHelper.COLOR_SHUIYA
            R.id.yiteng -> ThemeHelper.COLOR_YITENG
            R.id.jilao -> ThemeHelper.COLOR_JILAO
            R.id.zhihu -> ThemeHelper.COLOR_ZHIHU
            R.id.gutong -> ThemeHelper.COLOR_GUTONG
            R.id.didiao -> ThemeHelper.COLOR_DIDIAO
            R.id.gaoduan -> ThemeHelper.COLOR_GAODUAN
            R.id.aping -> ThemeHelper.COLOR_APING
            R.id.liangbai -> ThemeHelper.COLOR_LIANGBAI
            R.id.anluolan -> ThemeHelper.COLOR_ANLUOLAN
            R.id.xinghong -> ThemeHelper.COLOR_XINGHONG
            else -> {
                ThemeHelper.COLOR_YIMA
            }
        }
        if (ThemeHelper.getTheme(context) != mCurrentTheme) {
            ThemeHelper.setTheme(context, mCurrentTheme)
            ThemeUtils.refreshUI(context, object : ThemeUtils.ExtraRefreshable {
                override fun refreshSpecificView(view: View?) {
                }

                override fun refreshGlobal(activity: Activity?) {
                    if (Build.VERSION.SDK_INT >= 21) {
                        val taskDescription = ActivityManager.TaskDescription(null, null,
                                ThemeUtils.getThemeAttrColor(context, android.R.attr.colorPrimary))
                        activity!!.setTaskDescription(taskDescription)
                        activity.window.statusBarColor = ThemeUtils.getColorById(context, R.color.theme_color_primary_dark)
                    }
                }

            })
            RxBusTools.getDefault().post(EventMap.ChangeThemeEvent())
        }
    }


}