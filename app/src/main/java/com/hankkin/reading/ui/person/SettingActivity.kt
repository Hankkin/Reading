package com.hankkin.reading.ui.person

import android.app.Activity
import android.app.ActivityManager
import android.os.Build
import android.os.Bundle
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.bilibili.magicasakura.utils.ThemeUtils
import com.cocosw.bottomsheet.BottomSheet
import com.hankkin.library.utils.CacheUtils
import com.hankkin.library.utils.SPUtils
import com.hankkin.library.utils.ToastUtils
import com.hankkin.reading.R
import com.hankkin.reading.base.BaseActivity
import com.hankkin.reading.common.Constant
import com.hankkin.reading.event.EventMap
import com.hankkin.reading.utils.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : BaseActivity() {


    private var mCurrentTheme: Int = 0
    private lateinit var mThemeBuilder: BottomSheet.Builder

    override fun getLayoutId(): Int {
        return R.layout.activity_setting
    }

    override fun initViews(savedInstanceState: Bundle?) {

        setMiuiStatusBar()

        mCurrentTheme = ThemeHelper.getTheme(this)
        tv_setting_theme_value.text = ThemeHelper.getName(this, mCurrentTheme)
        initThemeBuilder()
        rl_setting_theme.setOnClickListener { mThemeBuilder.show() }

        //账号锁 账号备份 加载图片 单词备份
        switch_lock.isChecked = SPUtils.getInt(Constant.SP_KEY.LOCK_OPEN) != 0
        switch_lock_backup.isChecked = SPUtils.getInt(Constant.SP_KEY.LOCK_BACKUP_OPEN) != 0
        ll_setting_backup.visibility = if (switch_lock_backup.isChecked) View.VISIBLE else View.GONE
        switch_img.isChecked = SPUtils.getInt(Constant.SP_KEY.WIFI_IMG) != 0
        switch_logo.isChecked = SPUtils.getInt(Constant.SP_KEY.LOGO) != 0

        //开启账号锁
        switch_lock.setOnCheckedChangeListener { buttonView, isChecked ->
            SPUtils.put(Constant.SP_KEY.LOCK_OPEN, if (isChecked) 1 else 0)
        }
        //数据备份
        switch_lock_backup.setOnCheckedChangeListener { buttonView, isChecked ->
            SPUtils.put(Constant.SP_KEY.LOCK_BACKUP_OPEN, if (isChecked) 1 else 0)
            ll_setting_backup.visibility = if (isChecked) View.VISIBLE else View.GONE
        }
        //数据备份
        rl_setting_data_backup.setOnClickListener {
            if (DBUtils.isNeedBackup(this)) {
                LoadingUtils.showLoading(this)
                val disposable = Observable.create<Boolean> {
                    try {
                        DBUtils.saveDBData(this)
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
                            ToastUtils.showSuccess(this, resources.getString(R.string.setting_lock_backup_success))
                        }, {
                            LoadingUtils.hideLoading()
                            ToastUtils.showError(this, resources.getString(R.string.setting_lock_backup_fail))
                        })
                disposables.add(disposable)
            } else {
                ToastUtils.showInfo(this, resources.getString(R.string.setting_lock_backup_new))
            }

        }

        //数据还原
        rl_setting_data_restore.setOnClickListener {
            LoadingUtils.showLoading(this)
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
            disposables.add(disposable)
        }

        //默认加载图片
        switch_img.setOnCheckedChangeListener { buttonView, isChecked ->
            SPUtils.put(Constant.SP_KEY.WIFI_IMG, if (isChecked) 1 else 0)
            RxBusTools.getDefault().post(EventMap.WifiImgEvent())
        }
        //百变logo
        switch_logo.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                ViewHelper.showConfirmDialog(this, resources.getString(R.string.setting_logo_hint), MaterialDialog.SingleButtonCallback { dialog, which ->
                    SPUtils.put(Constant.SP_KEY.LOGO, 1)
                    ToastUtils.showInfo(this, resources.getString(R.string.setting_logo_success))
                })
            } else {
                SPUtils.put(Constant.SP_KEY.LOGO, 0)
            }
        }

        rl_setting_about.setOnClickListener { ViewHelper.showAboutDialog(this) }
        rl_setting_clear_cache.setOnClickListener {
            ViewHelper.showConfirmDialog(this,
                    resources.getString(R.string.setting_clear_cache_hint),
                    MaterialDialog.SingleButtonCallback { dialog, which ->
                        CacheUtils.clearGlideImg(this)
                        ToastUtils.showInfo(this, resources.getString(R.string.setting_clear_cache_success))
                        tv_setting_cache_size.setText("0KB")
                    })
        }
    }

    override fun initData() {
        tv_setting_cache_size.setText(CacheUtils.getCachesSize(this, Constant.COMMON.DB_NAME))
    }


    fun initThemeBuilder() {
        mThemeBuilder = BottomSheet.Builder(this, R.style.BottomSheet_StyleDialog)
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
        tv_setting_theme_value.text = ThemeHelper.getName(this, mCurrentTheme)

        if (ThemeHelper.getTheme(this) != mCurrentTheme) {
            ThemeHelper.setTheme(this, mCurrentTheme)
            ThemeUtils.refreshUI(this, object : ThemeUtils.ExtraRefreshable {
                override fun refreshSpecificView(view: View?) {
                }

                override fun refreshGlobal(activity: Activity?) {
                    if (Build.VERSION.SDK_INT >= 21) {
                        val context = this@SettingActivity
                        val taskDescription = ActivityManager.TaskDescription(null, null,
                                ThemeUtils.getThemeAttrColor(context, android.R.attr.colorPrimary))
                        setTaskDescription(taskDescription)
                        window.statusBarColor = ThemeUtils.getColorById(context, R.color.theme_color_primary_dark)
                    }
                }

            })
            RxBusTools.getDefault().post(EventMap.ChangeThemeEvent())
        }
    }

}
