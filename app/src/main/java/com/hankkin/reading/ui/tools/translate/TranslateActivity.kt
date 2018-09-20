package com.hankkin.reading.ui.tools.translate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.hankkin.library.utils.LogUtils
import com.hankkin.reading.R
import com.hankkin.reading.adapter.TranHistoryAdapter
import com.hankkin.reading.base.BaseActivity
import com.hankkin.reading.common.Constant
import com.hankkin.reading.domain.TranslateBean
import com.hankkin.reading.domain.WordNoteBean
import com.hankkin.reading.dao.DaoFactory
import com.hankkin.reading.ui.home.articledetail.CommonWebActivity
import com.hankkin.reading.ui.tools.wordnote.WordNoteDaoContract
import com.hankkin.reading.utils.JsonUtils
import com.hankkin.reading.utils.LoadingUtils
import com.hankkin.reading.utils.ViewHelper
import com.youdao.sdk.app.Language
import com.youdao.sdk.app.LanguageUtils
import com.youdao.sdk.ydonlinetranslate.Translator
import com.youdao.sdk.ydtranslate.Translate
import com.youdao.sdk.ydtranslate.TranslateErrorCode
import com.youdao.sdk.ydtranslate.TranslateListener
import com.youdao.sdk.ydtranslate.TranslateParameters
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_translate.*
import kotlinx.android.synthetic.main.layout_translate_history.*
import kotlinx.android.synthetic.main.layout_translate_top.*
import org.greenrobot.greendao.DaoException

class TranslateActivity : BaseActivity() {

    private lateinit var langFrom: Language
    private lateinit var langTo: Language
    private lateinit var tps: TranslateParameters
    private lateinit var translator: Translator
    private var translate: Translate? = null
    private var translateBean: TranslateBean? = null
    private lateinit var mSubscribe: Disposable

    companion object {
        fun intentTo(context: Context?, key: String) {
            val intent = Intent(context, TranslateActivity::class.java)
            intent.putExtra(Constant.CONSTANT_KEY.TRANSLATE, key)
            context?.startActivity(intent)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_translate
    }

    override fun initViews(savedInstanceState: Bundle?) {
        setMiuiStatusBar()
        iv_translate_back.setOnClickListener { finish() }
        iv_translate_search.setOnClickListener { searchWord(et_translate_search.text.toString()) }
        et_translate_search.setOnEditorActionListener { v, actionId, event ->
            searchWord(et_translate_search.text.toString())
            false
        }
        et_translate_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (et_translate_search.text.isEmpty()) {
                    iv_translate_clear.visibility = View.GONE
                    line_translate.visibility = View.GONE
                    setHistoryAdapter()
                } else {
                    iv_translate_clear.visibility = View.VISIBLE
                    line_translate.visibility = View.VISIBLE
                }
            }
        })
        iv_translate_clear.setOnClickListener {
            et_translate_search.setText("")
            setHistoryAdapter()
        }
        tv_translate_more.setOnClickListener {
            if (translate != null) {
                if (!translate!!.openDict(this)) {
                    if (translate!!.deeplink.isNotEmpty()) {
                        CommonWebActivity.loadUrl(this, translate!!.dictWebUrl, translate!!.query)
                    }
                } else {
                    translate!!.openMore(this)
                }
            }
        }
        iv_translate_star.setOnClickListener {
            if (translateBean == null) return@setOnClickListener
            var wordNoteBean = WordNoteBean(translateBean!!.hashCode().toLong(), false)
            wordNoteBean.translateBean = translateBean
            DaoFactory.getProtocol(WordNoteDaoContract::class.java).addWordToNote(wordNoteBean)
            iv_translate_stared.visibility = View.VISIBLE
            iv_translate_star.visibility = View.GONE
        }
    }

    override fun initData() {
        //查词对象初始化，请设置source参数为app对应的名称（英文字符串）
        langFrom = LanguageUtils.getLangByName("中文")
        //若设置为自动，则查询自动识别源语言，自动识别不能保证完全正确，最好传源语言类型
        //Language langFrom = LanguageUtils.getLangByName("自动");
        langTo = LanguageUtils.getLangByName("英文")

        tps = TranslateParameters.Builder()
                .source("ydtranslate-demo")
                .from(langFrom).to(langTo).build()
        translator = Translator.getInstance(tps)

        val key = intent.getStringExtra(Constant.CONSTANT_KEY.TRANSLATE)
        if (key != null && key.isNotEmpty()) {
            et_translate_search.setText(key)
            et_translate_search.setSelection(key.length)
            searchWord(key)
        } else {
            setHistoryAdapter()
        }
    }


    fun inflateSearch() {
        ll_search_result.visibility = View.VISIBLE
        ll_search_history.visibility = View.GONE
    }

    fun inflateHistory() {
        ll_search_result.visibility = View.GONE
        ll_search_history.visibility = View.VISIBLE
    }

    fun setHistoryAdapter() {
        inflateHistory()
        val history = getHistory()
        if (history != null && history.size > 0) {
            val adapter = TranHistoryAdapter()
            rv_translate_history.layoutManager = LinearLayoutManager(this)
            adapter.addAll(history)
            rv_translate_history.adapter = adapter
            adapter.setOnItemLongClickListener { t, position ->
                ViewHelper.showConfirmDialog(this, resources.getString(R.string.translate_delete_hint), MaterialDialog.SingleButtonCallback { dialog, which ->
                    DaoFactory.getProtocol(TranslateDaoContract::class.java).deleteTranslateHistory(t.id)
                    if (getHistory() != null && getHistory()!!.size > 0) {
                        adapter.clear()
                        adapter.addAll(getHistory())
                        adapter.notifyDataSetChanged()
                    }
                })
            }
            adapter.setOnItemClickListener { t, position ->
                et_translate_search.setText(t.query)
                et_translate_search.setSelection(t.query.length)
                searchWord(t.query)
            }
        }
    }

    fun getHistory(): MutableList<TranslateBean>? {
        return DaoFactory.getProtocol(TranslateDaoContract::class.java).queryTranslateHistoty()!!
    }

    fun searchWord(key: String) {
        if (key.isEmpty()) return
        //查询，返回两种情况，一种是成功，相关结果存储在result参数中，
        // 另外一种是失败，失败信息放在TranslateErrorCode中，TranslateErrorCode是一个枚举类，整个查询是异步的，为了简化操作，回调都是在主线程发生。
        LoadingUtils.showLoading(this)
        translator.lookup(key, key.hashCode().toString(), object : TranslateListener {
            override fun onResult(p0: Translate?, p1: String?, p2: String?) {
                Handler(Looper.getMainLooper()).post({
                    translate = p0
                    var translate = JsonUtils.jsonToObject(p0?.let { JsonUtils.objToJson(it) }!!, TranslateBean::class.java) as TranslateBean
                    translate.id = p2!!.toLong()
                    tv_translate_content.text = key
                    setWordLayout(translate)
                    saveHistory(translate)
                    LoadingUtils.hideLoading()
                })
            }

            override fun onResult(p0: MutableList<Translate>?, p1: MutableList<String>?, p2: MutableList<TranslateErrorCode>?, p3: String?) {
                LogUtils.d(p1)
                LoadingUtils.hideLoading()
            }

            override fun onError(p0: TranslateErrorCode?, p1: String?) {
                LogUtils.d(p1)
                LoadingUtils.hideLoading()
            }
        })
    }


    private fun setWordLayout(translate: TranslateBean?) {
        if (translate == null) return
        inflateSearch()
        translate.ukPhonetic?.let {
            if (translate.ukPhonetic.isEmpty()) {
                ll_translate_uk.visibility = View.GONE
            } else {
                ll_translate_uk.visibility = View.VISIBLE
                tv_translate_phonrtic_uk.text = "英/ ${translate.ukPhonetic}  /"
            }
        }

        translate.usPhonetic?.let {
            if (translate.usPhonetic.isEmpty()) {
                ll_translate_us.visibility = View.GONE
            } else {
                ll_translate_us.visibility = View.VISIBLE
                tv_translate_phonrtic_us.text = "美/  ${translate.usPhonetic} /"
            }
        }

        ll_translate_explains.removeAllViews()
        if (translate.explains != null){
            for (explain in translate.explains) {
                val tv = layoutInflater.inflate(R.layout.adapter_translate_paraphrases_item, null) as TextView
                tv.text = explain
                ll_translate_explains.addView(tv)
            }
        }

        try {
            if (translate.webExplains != null) {
                val webEx = translate.webExplains.get(0)
                tv_translate_web.text = webEx.means.toString()
            }
        }catch (e: DaoException){
            LogUtils.e(e.message)
        }

        val wordNotes = DaoFactory.getProtocol(WordNoteDaoContract::class.java).queryWordNotes()
        if (wordNotes != null && wordNotes.size > 0) {
            var ids = mutableListOf<Long>()
            for (word in wordNotes) {
                ids.add(word.id)
            }
            if (ids.contains(translate.id)) {
                iv_translate_stared.visibility = View.VISIBLE
                iv_translate_star.visibility = View.GONE
            } else {
                iv_translate_stared.visibility = View.GONE
                iv_translate_star.visibility = View.VISIBLE
            }
        }
    }


    private fun saveHistory(translate: TranslateBean?) {
        if (translate == null) return
        translateBean = translate
        DaoFactory.getProtocol(TranslateDaoContract::class.java).insertTranslateHistory(translate)
    }

}
