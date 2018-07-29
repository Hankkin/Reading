package com.hankkin.reading.ui.tools

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.hankkin.reading.R
import com.hankkin.reading.adapter.ToolsAdapter
import com.hankkin.reading.base.BaseMvpFragment
import com.hankkin.reading.common.Constant
import com.hankkin.reading.domain.ToolsBean
import com.hankkin.reading.domain.Weatherbean
import com.hankkin.reading.ui.home.articledetail.CommonWebActivity
import com.hankkin.reading.utils.LoadingUtils
import com.hankkin.reading.utils.ViewHelper
import com.hankkin.reading.utils.WeatherUtils
import kotlinx.android.synthetic.main.fragment_word.*

/**
 * Created by huanghaijie on 2018/5/15.
 */
class ToolsFragment : BaseMvpFragment<ToolsContract.IPresenter>(), ToolsContract.IView {

    private lateinit var mData: MutableList<ToolsBean>

    private lateinit var mToolsAdapter: ToolsAdapter


    override fun registerPresenter() = ToolsPresenter::class.java


    public fun newInstance(index: Int): ToolsFragment {
        var fragment = ToolsFragment()
        var args = Bundle()
        args.putInt("index", index)
        return fragment
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_word
    }

    override fun initData() {
        addData()
        rv_tools.layoutManager = GridLayoutManager(context, 4)
        mToolsAdapter = ToolsAdapter()
        mToolsAdapter.addAll(mData)
        rv_tools.adapter = mToolsAdapter
        mToolsAdapter.setOnItemClickListener { t, position ->
            when (t.id) {
                Constant.TOOLS.ID_ABOUT -> context?.let { ViewHelper.showAboutDialog(it) }
                Constant.TOOLS.ID_JUEJIN -> context?.let { CommonWebActivity.loadUrl(it,Constant.AboutUrl.JUEJIN,Constant.AboutUrl.JUEJIN_TITLE) }
            }
        }
        getPresenter().getWeather("beijing")
    }

    private fun addData() {
        mData = mutableListOf<ToolsBean>(
                ToolsBean(Constant.TOOLS.ID_KUAIDI, activity!!.resources.getString(R.string.work_kuaidi), R.mipmap.icon_kuaidi),
                ToolsBean(Constant.TOOLS.ID_SAOYISAO, activity!!.resources.getString(R.string.work_sao), R.mipmap.icon_saoyisao),
                ToolsBean(Constant.TOOLS.ID_WORD, activity!!.resources.getString(R.string.work_word), R.mipmap.icon_word),
                ToolsBean(Constant.TOOLS.ID_WORD_NOTE, activity!!.resources.getString(R.string.work_word_note), R.mipmap.icon_wrod_note),
                ToolsBean(Constant.TOOLS.ID_MOVIE, activity!!.resources.getString(R.string.work_movie), R.mipmap.icon_dianying),
                ToolsBean(Constant.TOOLS.ID_MUSIC, activity!!.resources.getString(R.string.work_music), R.mipmap.icon_music),
                ToolsBean(Constant.TOOLS.ID_WEATHER, activity!!.resources.getString(R.string.work_weather), R.mipmap.icon_weather),
                ToolsBean(Constant.TOOLS.ID_PWD_NOTE, activity!!.resources.getString(R.string.work_pwd_note), R.mipmap.icon_pwd_tools),
                ToolsBean(Constant.TOOLS.ID_NEWS, activity!!.resources.getString(R.string.work_news), R.mipmap.icon_computer),
                ToolsBean(Constant.TOOLS.ID_JUEJIN, activity!!.resources.getString(R.string.work_juejin), R.mipmap.icon_juejin),
                ToolsBean(Constant.TOOLS.ID_ABOUT, activity!!.resources.getString(R.string.work_about), R.mipmap.icon_about)
        )
    }

    override fun initView() {
        tv_translate_weather.text = "正在获取天气..."
        et_translate_search.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                when (actionId) {
                    EditorInfo.IME_ACTION_SEARCH -> {
                    }
                }
                return false
            }
        })
    }


    override fun setWeather(weatherbean: Weatherbean) {
        val now = weatherbean.results[0].now
        val format = resources.getString(R.string.format_weather)
        tv_translate_weather.text = String.format(format, now.text, now.temperature)
        iv_translate_weather.setImageResource(WeatherUtils.getWeatherImg(now.code, context))
    }

    override fun setWeatherError() {
        tv_translate_weather.text = "获取天气失败"
    }

    override fun hideLoading() {
        LoadingUtils.hideLoading()
    }

    override fun showLoading() {
        LoadingUtils.showLoading(context)
    }
}

