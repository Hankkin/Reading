package com.hankkin.reading.ui.tools

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.hankkin.library.fuct.RxLogTool
import com.hankkin.library.fuct.android.CaptureActivity
import com.hankkin.library.fuct.bean.ZxingConfig
import com.hankkin.reading.R
import com.hankkin.reading.adapter.ToolsAdapter
import com.hankkin.reading.base.BaseMvpFragment
import com.hankkin.reading.common.Constant
import com.hankkin.reading.domain.ToolsBean
import com.hankkin.reading.domain.Weatherbean
import com.hankkin.reading.ui.home.articledetail.CommonWebActivity
import com.hankkin.reading.utils.LoadingUtils
import com.hankkin.reading.utils.ThemeHelper
import com.hankkin.reading.utils.ViewHelper
import com.hankkin.reading.utils.WeatherUtils
import kotlinx.android.synthetic.main.fragment_word.*
import com.youdao.sdk.ydtranslate.TranslateErrorCode
import com.youdao.sdk.ydtranslate.Translate
import com.youdao.sdk.ydtranslate.TranslateListener
import com.youdao.sdk.ydtranslate.TranslateParameters
import com.youdao.sdk.app.LanguageUtils
import com.youdao.sdk.ydonlinetranslate.Translator


/**
 * Created by huanghaijie on 2018/5/15.
 */
class ToolsFragment : BaseMvpFragment<ToolsContract.IPresenter>(), ToolsContract.IView {
    val REQUEST_CODE_SCAN = 0x1

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
                Constant.TOOLS.ID_KUAIDI -> startActivity(Intent(context, KuaiDiActivity::class.java))
                Constant.TOOLS.ID_ABOUT -> context?.let { ViewHelper.showAboutDialog(it) }
                Constant.TOOLS.ID_JUEJIN -> context?.let { CommonWebActivity.loadUrl(it, Constant.AboutUrl.JUEJIN, Constant.AboutUrl.JUEJIN_TITLE) }
                Constant.TOOLS.ID_SAOYISAO -> {
                    val intent = Intent(context, CaptureActivity::class.java)
                    val bundle = Bundle()
                    val config = ZxingConfig()
                    config.reactColor = ThemeHelper.getCurrentColor(context)
                    bundle.putSerializable(com.hankkin.library.fuct.common.Constant.INTENT_ZXING_CONFIG, config)
                    intent.putExtras(bundle)
                    startActivityForResult(intent, REQUEST_CODE_SCAN)
                }
            }
        }
        getPresenter().getWeather("beijing")
        et_translate_search.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                //查词对象初始化，请设置source参数为app对应的名称（英文字符串）
                val langFrom = LanguageUtils.getLangByName("中文")
//若设置为自动，则查询自动识别源语言，自动识别不能保证完全正确，最好传源语言类型
//Language langFrom = LanguageUtils.getLangByName("自动");
                val langTo = LanguageUtils.getLangByName("英文")

                val tps = TranslateParameters.Builder()
                        .source("ydtranslate-demo")
                        .from(langFrom).to(langTo).build()

                val translator = Translator.getInstance(tps)


                //查询，返回两种情况，一种是成功，相关结果存储在result参数中，
                // 另外一种是失败，失败信息放在TranslateErrorCode中，TranslateErrorCode是一个枚举类，整个查询是异步的，为了简化操作，回调都是在主线程发生。

                translator.lookup(et_translate_search.text.toString(), "requestId", object : TranslateListener {
                    override fun onResult(p0: Translate?, p1: String?, p2: String?) {
                        RxLogTool.d(p1)
                    }

                    override fun onResult(p0: MutableList<Translate>?, p1: MutableList<String>?, p2: MutableList<TranslateErrorCode>?, p3: String?) {
                        RxLogTool.d(p1)
                    }

                    override fun onError(p0: TranslateErrorCode?, p1: String?) {
                        RxLogTool.d(p1)
                    }
                })
            }
            false
        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SCAN) {
            if (resultCode == Activity.RESULT_OK) {
                val url = data!!.getStringExtra(com.hankkin.library.fuct.common.Constant.CODED_CONTENT)
                context?.let { CommonWebActivity.loadUrl(it, url, "扫描结果") }
            }
        }
    }
}

