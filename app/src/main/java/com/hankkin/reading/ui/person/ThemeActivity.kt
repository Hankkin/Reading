package com.hankkin.reading.ui.person

import android.app.Activity
import android.app.ActivityManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.bilibili.magicasakura.utils.ThemeUtils
import com.hankkin.library.utils.RxBusTools
import com.hankkin.reading.R
import com.hankkin.reading.adapter.ThemePicAdapter
import com.hankkin.reading.base.BaseActivity
import com.hankkin.reading.domain.ThemeItemBean
import com.hankkin.reading.event.EventMap
import com.hankkin.reading.utils.ThemeHelper
import kotlinx.android.synthetic.main.activity_theme.*

class ThemeActivity : BaseActivity() {

    private lateinit var mAdapter: ThemePicAdapter
    private lateinit var data: MutableList<ThemeItemBean>
    private var mCurrentTheme: Int = 0

    override fun getLayoutId(): Int {
        return R.layout.activity_theme
    }

    override fun initViews(savedInstanceState: Bundle?) {
        setStatusBarColor()
        iv_back_icon.setOnClickListener { finish() }
        tv_theme_ok.setOnClickListener {
            changeTheme()
        }
        tv_normal_title.text = resources.getString(R.string.setting_theme)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        rv_theme.layoutManager = layoutManager
        mAdapter = ThemePicAdapter()
    }

    override fun initData() {
        data = mutableListOf(ThemeItemBean(ThemeHelper.COLOR_YIMA, false, R.mipmap.color_yima, R.mipmap.pic_yima),
                ThemeItemBean(ThemeHelper.COLOR_BILI, false, R.mipmap.color_bili, R.mipmap.pic_bili),
                ThemeItemBean(ThemeHelper.COLOR_YITENG, false, R.mipmap.color_yiteng, R.mipmap.pic_yiteng),
                ThemeItemBean(ThemeHelper.COLOR_XINGHONG, false, R.mipmap.color_xinghong, R.mipmap.pic_xinghong),
                ThemeItemBean(ThemeHelper.COLOR_ANLUOLAN, false, R.mipmap.color_anluolan, R.mipmap.pic_anluolan),
                ThemeItemBean(ThemeHelper.COLOR_KUAN, false, R.mipmap.color_kuan, R.mipmap.pic_kuan),
                ThemeItemBean(ThemeHelper.COLOR_JILAO, false, R.mipmap.color_jilao, R.mipmap.pic_jilao),
                ThemeItemBean(ThemeHelper.COLOR_YIDI, false, R.mipmap.color_yidi, R.mipmap.pic_yidi),
                ThemeItemBean(ThemeHelper.COLOR_ZHIHU, false, R.mipmap.color_zhihu, R.mipmap.pic_zhihu),
                ThemeItemBean(ThemeHelper.COLOR_SHUIYA, false, R.mipmap.color_shuiya, R.mipmap.pic_shuiya),
                ThemeItemBean(ThemeHelper.COLOR_DIDIAO, false, R.mipmap.color_didiao, R.mipmap.pic_didiao),
                ThemeItemBean(ThemeHelper.COLOR_GUTONG, false, R.mipmap.color_gutong, R.mipmap.pic_gutong),
                ThemeItemBean(ThemeHelper.COLOR_APING, false, R.mipmap.color_aping, R.mipmap.pic_aping),
                ThemeItemBean(ThemeHelper.COLOR_GAODUAN, false, R.mipmap.color_gaoduan, R.mipmap.pic_gaoduan),
                ThemeItemBean(ThemeHelper.COLOR_LIANGBAI, false, R.mipmap.color_liangbai, R.mipmap.pic_liangbai)
        )
        for (d in data) {
            if (d.id == ThemeHelper.getTheme(this)) {
                d.isSelected = true
                iv_theme_expamle.setImageResource(d.pic)
            }
        }
        mAdapter.addAll(data)
        rv_theme.adapter = mAdapter

        mAdapter.setOnItemClickListener { t, position ->
            mCurrentTheme = t.id
            iv_theme_expamle.setImageResource(t.pic)
            for (d in data) {
                d.isSelected = d.id == t.id
            }
            mAdapter.notifyDataSetChanged()
        }
    }

    /**
     * 修改主题颜色
     */
    fun changeTheme() {
        if (ThemeHelper.getTheme(this) != mCurrentTheme) {
            ThemeHelper.setTheme(this, mCurrentTheme)
            ThemeUtils.refreshUI(this, object : ThemeUtils.ExtraRefreshable {
                override fun refreshSpecificView(view: View?) {
                }

                override fun refreshGlobal(activity: Activity?) {
                    if (Build.VERSION.SDK_INT >= 21) {
                        val taskDescription = ActivityManager.TaskDescription(null, null,
                                ThemeUtils.getThemeAttrColor(this@ThemeActivity, android.R.attr.colorPrimary))
                        setTaskDescription(taskDescription)
                        window.statusBarColor = ThemeUtils.getColorById(this@ThemeActivity, R.color.theme_color_primary_dark)
                    }
                }

            })
            RxBusTools.getDefault().post(EventMap.ChangeThemeEvent())
            finish()
        }
    }

}
