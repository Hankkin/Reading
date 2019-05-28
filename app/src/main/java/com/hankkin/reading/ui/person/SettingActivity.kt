package com.hankkin.reading.ui.person

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.hankkin.library.utils.*
import com.hankkin.reading.R
import com.hankkin.reading.base.BaseActivity
import com.hankkin.reading.common.Constant
import com.hankkin.reading.event.EventMap
import com.hankkin.reading.ui.home.articledetail.CommonWebActivity
import com.hankkin.reading.utils.DBUtils
import com.hankkin.reading.utils.LoadingUtils
import com.hankkin.reading.utils.ThemeHelper
import com.hankkin.reading.utils.ViewHelper
import com.tencent.bugly.beta.Beta
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : BaseActivity() {


    private var mCurrentTheme: Int = 0
//    private lateinit var mThemeBuilder: BottomSheet.Builder


    override fun isHasBus(): Boolean {
        return true
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_setting
    }

    override fun initViews(savedInstanceState: Bundle?) {

        setMiuiStatusBar()

        mCurrentTheme = ThemeHelper.getTheme(this)
        tv_setting_theme_value.text = ThemeHelper.getName(this, mCurrentTheme)
//        initThemeBuilder()
        rl_setting_theme.setOnClickListener { startActivity(Intent(this,ThemeActivity::class.java)) }

        //账号锁 账号备份 加载图片 单词备份
        switch_lock.apply {
            isChecked = SPUtils.getInt(Constant.SP_KEY.LOCK_OPEN) != 0
            isChecked = SPUtils.getInt(Constant.SP_KEY.LOCK_BACKUP_OPEN) != 0
        }

        ll_setting_backup.visibility = if (switch_lock_backup.isChecked) View.VISIBLE else View.GONE
        switch_img.isChecked = SPUtils.getInt(Constant.SP_KEY.WIFI_IMG) != 0
        switch_logo.isChecked = SPUtils.getInt(Constant.SP_KEY.LOGO) != 0
        switch_slide.isChecked = SPUtils.getBoolean(com.hankkin.library.fuct.common.Constant.SLIDE_LEFT)

        //开启账号锁
        switch_lock.setOnCheckedChangeListener { buttonView, isChecked ->
            SPUtils.put(Constant.SP_KEY.LOCK_OPEN, if (isChecked) 1 else 0)
        }
        //数据备份
        switch_lock_backup.setOnCheckedChangeListener { buttonView, isChecked ->
            SPUtils.put(Constant.SP_KEY.LOCK_BACKUP_OPEN, if (isChecked) 1 else 0)
            ll_setting_backup.visibility = if (isChecked) View.VISIBLE else View.GONE
        }
        //左侧返回
        switch_slide.setOnCheckedChangeListener { compoundButton, isChecked ->
            SPUtils.put(com.hankkin.library.fuct.common.Constant.SLIDE_LEFT, isChecked)
            activity?.let { ToastUtils.showInfo(it,"您已设置左侧边缘的滑动,下次启动生效") }
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

        //数据清空
        rl_setting_data_clear.setOnClickListener {
            ViewHelper.showConfirmDialog(this, resources.getString(R.string.setting_db_delete_hint),
                    MaterialDialog.SingleButtonCallback { dialog, which ->
                        DBUtils.deleteData(this)
                        ToastUtils.showSuccess(this, resources.getString(R.string.setting_db_delete_success))
                    })
        }

        //默认加载图片
        switch_img.setOnCheckedChangeListener { buttonView, isChecked ->
            SPUtils.put(Constant.SP_KEY.WIFI_IMG, if (isChecked) 1 else 0)
            RxBusTools.getDefault().post(EventMap.WifiImgEvent())
        }
        //百变logo
        switch_logo.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                ViewHelper.showConfirmDialog(this, resources.getString(R.string.setting_logo_hint),
                        MaterialDialog.SingleButtonCallback { dialog, which ->
                            SPUtils.put(Constant.SP_KEY.LOGO, 1)
                            ToastUtils.showInfo(this, resources.getString(R.string.setting_logo_success))
                        }, MaterialDialog.SingleButtonCallback { dialog, which ->
                    switch_logo.isChecked = false
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
                        CacheUtils.clearCacte(this)
                        ToastUtils.showInfo(this, resources.getString(R.string.setting_clear_cache_success))
                        tv_setting_cache_size.text = "0KB"
                    })
        }
        rl_setting_api.setOnClickListener { CommonWebActivity.loadUrl(this,"http://hankkin.cn/threeapi/",resources.getString(R.string.setting_api)) }
        tv_setting_version.text = "V "+ AppUtils.getVersionName(this)
        rl_setting_update.setOnClickListener { Beta.checkUpgrade(true,false) }
    }
//
    override fun initData() {
        tv_setting_cache_size.text = CacheUtils.getCachesSize(this)
    }

    override fun onEvent(event: EventMap.BaseEvent) {
        if (event is EventMap.ChangeThemeEvent){
            mCurrentTheme = ThemeHelper.getTheme(this)
            tv_setting_theme_value.text = ThemeHelper.getName(this, mCurrentTheme)
        }
    }

}
