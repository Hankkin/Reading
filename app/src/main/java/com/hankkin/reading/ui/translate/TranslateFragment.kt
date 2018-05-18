package com.hankkin.reading.ui.translate

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.BindView
import com.hankkin.reading.R
import com.hankkin.reading.base.BaseFragment
import com.hankkin.reading.domain.*
import com.hankkin.reading.utils.LogUtils
import com.hankkin.reading.utils.WeatherUtils

/**
 * Created by huanghaijie on 2018/5/15.
 */
class TranslateFragment : BaseFragment<TranslateContract.IPresenter>(), TranslateContract.IView {

    override fun createmPresenter() = TranslatePresenter(this)


    @BindView(R.id.et_translate_search) lateinit var etSearch: EditText
    @BindView(R.id.tv_translate_weather) lateinit var tvWeather: TextView
    @BindView(R.id.iv_translate_weather) lateinit var ivWeather: ImageView
    @BindView(R.id.ll_translate_speak) lateinit var llSpeak: LinearLayout
    @BindView(R.id.tv_translate_word) lateinit var tvWord: TextView
    @BindView(R.id.tv_translate_ranks) lateinit var tvRank: TextView
    @BindView(R.id.ll_translate_para) lateinit var llPara: LinearLayout

    lateinit var word: WordBean

    public fun newInstance(index: Int): TranslateFragment {
        var fragment = TranslateFragment()
        var args = Bundle()
        args.putInt("index", index)
        return fragment
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_translate
    }

    override fun initData() {
        getmPresenter().getWeather("beijing")
    }

    override fun initViews() {
        tvWeather.text = "正在获取天气..."
        etSearch.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                when (actionId) {
                    EditorInfo.IME_ACTION_SEARCH -> {
                        getmPresenter().getWrod(etSearch.text.toString().trim())
                    }
                }
                return false
            }

        })
    }


    override fun setWeather(weatherbean: Weatherbean) {
        val now = weatherbean.results[0].now
        val format = resources.getString(R.string.format_weather)
        tvWeather.text = String.format(format, now.text, now.temperature)
        ivWeather.setImageResource(WeatherUtils.getWeatherImg(now.code, context))
    }

    override fun setWeatherError() {
        tvWeather.text = "获取天气失败"
    }

    override fun searchWordResult(wordBean: WordBean) {
        if (wordBean.data == null) return
        this.word = wordBean
        val data = wordBean.data
        tvWord.text = data.title
        llSpeak.removeAllViews()
        llPara.removeAllViews()
        for (phonetics in data.phonetics) {
            val tv: TextView = LayoutInflater.from(context).inflate(R.layout.layout_translate_speak, null) as TextView
            tv.text = if (phonetics.type == 1) "英  ${phonetics.content}" else "美  ${phonetics.content}"
            llSpeak.addView(tv)
        }
        getmPresenter().downRank(data.title + data.phonetics[0].name, data.phonetics[0].url)
        data.ranks?.let {
            tvRank.visibility = View.VISIBLE
        }

        var rank = ""
        for (s in data.ranks!!) {
            rank = rank + "/" + s
        }
        tvRank.text = rank

        for (item in data.paraphrases) {
            llPara.addView(addParaItemView(item.key))
        }

    }

    fun addParaItemView(key: String): View {
        val data = word.data.paraphrases
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_translate_paraphrases_item, null)
        val tv = view.findViewById<TextView>(R.id.tv_translate_para_key)
        val value = view.findViewById<TextView>(R.id.tv_translate_para_value)
        tv.text = key
        value.text = data.get(key).toString()
        return view
    }

    override fun searchFail() {
    }


    override fun downRankSuc(path: String) {
        LogUtils.e(path)
    }


}

