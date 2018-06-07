package com.hankkin.reading.ui.translate

import android.graphics.drawable.AnimationDrawable
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.BindView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.hankkin.reading.R
import com.hankkin.reading.adapter.PhoneticsAdapter
import com.hankkin.reading.base.BaseFragment
import com.hankkin.reading.domain.BaseResponse
import com.hankkin.reading.domain.Weatherbean
import com.hankkin.reading.domain.WordBean
import com.hankkin.reading.utils.DownUtils
import com.hankkin.reading.utils.FileUtils
import com.hankkin.reading.utils.LoadingUtils
import com.hankkin.reading.utils.WeatherUtils
import com.hankkin.reading.view.GridSpacingItemDecoration
import okhttp3.ResponseBody
import java.io.File

/**
 * Created by huanghaijie on 2018/5/15.
 */
class TranslateFragment : BaseFragment<TranslateContract.IPresenter>(), TranslateContract.IView {


    override fun createmPresenter() = TranslatePresenter(this)


    @BindView(R.id.et_translate_search) lateinit var etSearch: EditText
    @BindView(R.id.tv_translate_weather) lateinit var tvWeather: TextView
    @BindView(R.id.iv_translate_weather) lateinit var ivWeather: ImageView
    @BindView(R.id.rv_translate_speak) lateinit var rv: RecyclerView
    @BindView(R.id.tv_translate_word) lateinit var tvWord: TextView
    @BindView(R.id.tv_translate_ranks) lateinit var tvRank: TextView
    @BindView(R.id.ll_translate_para) lateinit var llPara: LinearLayout



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

    override fun searchWordResult(reponse: WordBean) {
        if (reponse == null) return
        this.word = reponse
        tvWord.text = word.title
        llPara.removeAllViews()

        phoneticsAdapter = PhoneticsAdapter()
        rv.layoutManager = GridLayoutManager(context,word.paraphrases.size)
        rv.addItemDecoration( GridSpacingItemDecoration(word.paraphrases.size, 30, false))
        phoneticsAdapter.setNewData(word.phonetics)
        rv.adapter = phoneticsAdapter


        phoneticsAdapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            val phonetics = word.phonetics[position]
            checkPhonetics(word.title, phonetics.name, phonetics.url,position)
        }

        if(word.ranks!!.size>0) {
            tvRank.visibility = View.VISIBLE
        }

        var rank = ""
        for (s in word.ranks!!) {
            rank = rank + "/" + s
        }
        tvRank.text = rank

        for (item in word.paraphrases) {
            llPara.addView(addParaItemView(item.key))
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
            getmPresenter().downRank(name, url, type,index)
        }
    }

    fun playPhonetice(name: String, type: String,index: Int) {
        val path = FileUtils.RANK_PATH + type + File.separator + name + ".mp3"
        mediaPlayer.setDataSource(path)
        mediaPlayer.prepare()
        mediaPlayer.start()
        val iv = phoneticsAdapter.getViewByPosition(rv,index,R.id.iv_translate_play) as ImageView
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

