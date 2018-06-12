package com.hankkin.reading.ui.translate

import android.graphics.drawable.AnimationDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.hankkin.reading.R
import com.hankkin.reading.adapter.PhoneticsAdapter
import com.hankkin.reading.base.BaseMvpFragment
import com.hankkin.reading.domain.Weatherbean
import com.hankkin.reading.domain.WordBean
import com.hankkin.reading.utils.DownUtils
import com.hankkin.reading.utils.FileUtils
import com.hankkin.reading.utils.LoadingUtils
import com.hankkin.reading.utils.WeatherUtils
import com.hankkin.reading.view.GridSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_word.*
import kotlinx.android.synthetic.main.layout_translate_content.*
import okhttp3.ResponseBody
import java.io.File

/**
 * Created by huanghaijie on 2018/5/15.
 */
class TranslateFragment : BaseMvpFragment<TranslateContract.IPresenter>(), TranslateContract.IView {


    override fun registerPresenter() = TranslatePresenter::class.java


    lateinit var word: WordBean
    lateinit var phoneticsAdapter: PhoneticsAdapter
    var mediaPlayer: MediaPlayer = MediaPlayer()


    public fun newInstance(index: Int): TranslateFragment {
        var fragment = TranslateFragment()
        var args = Bundle()
        args.putInt("index", index)
        return fragment
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_word
    }

    override fun initData() {
        getPresenter().getWeather("beijing")
    }

    override fun initView() {
        tv_translate_weather.text = "正在获取天气..."
        et_translate_search.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                when (actionId) {
                    EditorInfo.IME_ACTION_SEARCH -> {
                        getPresenter().getWrod(et_translate_search.text.toString().trim())
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

    override fun searchWordResult(reponse: WordBean) {
        if (reponse == null) return
        this.word = reponse
        tv_translate_word.text = word.title
        ll_translate_para.removeAllViews()

        phoneticsAdapter = PhoneticsAdapter()
        rv_translate_speak.layoutManager = GridLayoutManager(context,word.paraphrases.size)
        rv_translate_speak.addItemDecoration( GridSpacingItemDecoration(word.paraphrases.size, 30, false))
        phoneticsAdapter.setNewData(word.phonetics)
        rv_translate_speak.adapter = phoneticsAdapter


        phoneticsAdapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            val phonetics = word.phonetics[position]
            checkPhonetics(word.title, phonetics.name, phonetics.url,position)
        }

        if(word.ranks!!.size>0) {
            tv_translate_ranks.visibility = View.VISIBLE
        }

        var rank = ""
        for (s in word.ranks!!) {
            rank = rank + "/" + s
        }
        tv_translate_ranks.text = rank

        for (item in word.paraphrases) {
            ll_translate_para.addView(addParaItemView(item.key))
        }
    }

    fun addParaItemView(key: String): View {
        val data = word.paraphrases
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_translate_paraphrases_item, null)
        val tv = view.findViewById<TextView>(R.id.tv_translate_para_key)
        val value = view.findViewById<TextView>(R.id.tv_translate_para_value)
        tv.text = key
        value.text = data.get(key).toString()
        return view
    }

    override fun searchFail() {
        hideLoading()
    }


    override fun downRankSuc(body: ResponseBody, name: String, type: String,index: Int) {
        DownUtils.saveRank(name, type, body)
        playPhonetice(name, type,index)
    }

    override fun hideLoading() {
        LoadingUtils.hideLoading()
    }

    override fun showLoading() {
        LoadingUtils.showLoading(context)
    }


    fun checkPhonetics(name: String, type: String, url: String,index: Int) {
        if (FileUtils.isDownloadedRank(name, type)) {
            if (mediaPlayer.isPlaying) return
            playPhonetice(name, type,index)
        } else {
            getPresenter().downRank(name, url, type,index)
        }
    }

    fun playPhonetice(name: String, type: String,index: Int) {
        val path = FileUtils.RANK_PATH + type + File.separator + name + ".mp3"
        mediaPlayer.setDataSource(path)
        mediaPlayer.prepare()
        mediaPlayer.start()
        val iv = phoneticsAdapter.getViewByPosition(rv_translate_speak,index,R.id.iv_translate_play) as ImageView
        mediaPlayer.setOnCompletionListener{
            mediaPlayer.reset()
            ((iv.drawable)as AnimationDrawable).stop()
            iv.setImageResource(R.mipmap.icon_translate_play2)
        }
        iv.setImageResource(R.drawable.phonetics_play_anim)
        ((iv.drawable)as AnimationDrawable).start()
    }


    override fun onStop() {
        super.onStop()
        mediaPlayer.stop()
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.pause()
    }


}

