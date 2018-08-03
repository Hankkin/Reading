package com.hankkin.reading.ui.person

import android.app.Activity
import android.app.ActivityManager
import android.os.Build
import android.os.Bundle
import android.view.View
import com.bilibili.magicasakura.utils.ThemeUtils
import com.cocosw.bottomsheet.BottomSheet
import com.hankkin.library.utils.StatusBarUtil
import com.hankkin.reading.R
import com.hankkin.reading.base.BaseActivity
import com.hankkin.reading.event.EventMap
import com.hankkin.reading.utils.RxBusTools
import com.hankkin.reading.utils.ThemeHelper
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : BaseActivity() {


    private var mCurrentTheme: Int = 0
    private lateinit var mThemeBuilder: BottomSheet.Builder

    override fun getLayoutId(): Int {
        return R.layout.activity_setting
    }

    override fun initData() {
    }

    override fun initViews(savedInstanceState: Bundle?) {

        StatusBarUtil.setColor(this, resources.getColor(R.color.white), 0)

        mCurrentTheme = ThemeHelper.getTheme(this)
        tv_setting_theme_value.text = ThemeHelper.getName(this,mCurrentTheme)
        initThemeBuilder()
        rl_setting_theme.setOnClickListener { mThemeBuilder.show() }
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
        tv_setting_theme_value.text = ThemeHelper.getName(this,mCurrentTheme)
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
