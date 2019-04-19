package com.hankkin.reading.ui.tools

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.hankkin.library.fuct.android.CaptureActivity
import com.hankkin.library.fuct.bean.ZxingConfig
import com.hankkin.library.mvp.presenter.RxLifePresenter
import com.hankkin.library.utils.AppUtils
import com.hankkin.library.utils.SPUtils
import com.hankkin.library.utils.ToastUtils
import com.hankkin.reading.R
import com.hankkin.reading.SplashActivity
import com.hankkin.reading.adapter.PersonListAdapter
import com.hankkin.reading.base.BaseMvpFragment
import com.hankkin.reading.common.Constant
import com.hankkin.reading.control.UserControl
import com.hankkin.reading.dao.DaoFactory
import com.hankkin.reading.domain.PersonListBean
import com.hankkin.reading.domain.Weatherbean
import com.hankkin.reading.domain.WordNoteBean
import com.hankkin.reading.event.EventMap
import com.hankkin.reading.http.HttpClientUtils
import com.hankkin.reading.ui.home.articledetail.CommonWebActivity
import com.hankkin.reading.ui.login.LoginActivity
import com.hankkin.reading.ui.person.*
import com.hankkin.reading.ui.tools.acount.AccountListActivity
import com.hankkin.reading.ui.tools.acount.LockSetActivity
import com.hankkin.reading.ui.tools.translate.TranslateActivity
import com.hankkin.reading.ui.tools.wordnote.WordNoteActivity
import com.hankkin.reading.ui.tools.wordnote.WordNoteDaoContract
import com.hankkin.reading.utils.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.util.BackpressureHelper.add
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_word.*
import kotlinx.android.synthetic.main.layout_card_word.*
import kotlinx.android.synthetic.main.layout_tools.*
import kotlinx.android.synthetic.main.layout_word_every.*
import kotlinx.android.synthetic.main.layout_word_no_data.*
import java.util.*


/**
 * Created by huanghaijie on 2018/5/15.
 */
class ToolsFragment : BasePersonFragment() {

    override fun isHasBus(): Boolean {
        return true
    }

    override fun getChildLayoutId(): Int {
        return R.layout.fragment_word
    }

    override fun initViews() {
        super.initViews()
        tv_translate_weather.text = "正在获取天气..."
        tv_tools_version.text = getVersion()
        tv_word_go.setOnClickListener { goTranslate() }
        tv_word_note.setOnClickListener { goWordNote() }
        iv_person_change.setOnClickListener { changeTheme(0) }
        tv_word_next.setOnClickListener {
            ViewHelper.startShakeAnim(card_word_every)
            setEveryWord()
        }
        tv_tools_title.setOnClickListener { goLogin() }
        ll_tools_scan.setOnClickListener { goScan() }
        ll_tools_note.setOnClickListener { goWordNote() }
        ll_tools_word.setOnClickListener { goTranslate() }
        ll_tools_pwd.setOnClickListener { goAccount() }
    }


    override fun initData() {
        setEveryWord()
        setSetting()
        getWeather()
    }

    @SuppressLint("CheckResult")
    private fun getWeather() {
        val map = HashMap<String, Any>()
        map["key"] = Constant.COMMON.WEATHER_KEY
        map["location"] = "beijing"
        HttpClientUtils.Builder.getToolsHttp().getWeather(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val now = it.results[0].now
                    val format = resources.getString(R.string.format_weather)
                    tv_translate_weather.text = String.format(format, now.text, now.temperature)
                    iv_translate_weather.setImageResource(WeatherUtils.getWeatherImg(now.code, context))
                }, {
                    tv_translate_weather.text = "获取天气失败"
                })
    }


    private fun setSetting() {
        tv_tools_title.text = if (UserControl.isLogin()) "Hi,${UserControl.getCurrentUser()?.username}" else resources.getString(R.string.hi_login)
        xrv_tools_lisy.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SCAN) {
            if (resultCode == Activity.RESULT_OK) {
                val url = data!!.getStringExtra(com.hankkin.library.fuct.common.Constant.CODED_CONTENT)
                context?.let { CommonWebActivity.loadUrl(it, url, "扫描结果") }
            }
        }
    }

    override fun onEvent(event: EventMap.BaseEvent) {
        super.onEvent(event)
        when (event) {
            is EventMap.LoginEvent -> mAdapter.apply {
                add(PersonListBean(R.mipmap.icon_person_set_exit, resources.getString(R.string.person_info_logout)))
                mAdapter.notifyDataSetChanged()
                tv_tools_title.text = if (UserControl.isLogin()) "Hi,${UserControl.getCurrentUser()?.username}" else resources.getString(R.string.hi_login)
            }
            is EventMap.PersonClickEvent -> when (event.index) {
                4 -> {
                    context?.let {
                        ViewHelper.showConfirmDialog(it,
                                resources.getString(R.string.person_info_logout_hint),
                                MaterialDialog.SingleButtonCallback { dialog, which ->
                                    UserControl.logout()
                                    mAdapter.apply {
                                        remove(data.size - 1)
                                        notifyDataSetChanged()
                                    }
                                    ToastUtils.showInfo(context!!, "已注销登录!")
                                    tv_tools_title.text = resources.getString(R.string.hi_login)
                                })
                    }
                }
            }
        }
    }
}


