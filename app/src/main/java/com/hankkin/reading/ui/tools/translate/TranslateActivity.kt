package com.hankkin.reading.ui.tools.translate

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.hankkin.library.fuct.RxLogTool
import com.hankkin.library.utils.StatusBarUtil
import com.hankkin.reading.R
import com.hankkin.reading.base.BaseActivity
import com.youdao.sdk.app.Language
import com.youdao.sdk.app.LanguageUtils
import com.youdao.sdk.ydonlinetranslate.Translator
import com.youdao.sdk.ydtranslate.Translate
import com.youdao.sdk.ydtranslate.TranslateErrorCode
import com.youdao.sdk.ydtranslate.TranslateListener
import com.youdao.sdk.ydtranslate.TranslateParameters
import kotlinx.android.synthetic.main.activity_translate.*

class TranslateActivity : BaseActivity() {

    private lateinit var langFrom: Language
    private lateinit var langTo: Language
    private lateinit var tps: TranslateParameters
    private lateinit var translator: Translator

    override fun getLayoutId(): Int {
        return R.layout.activity_translate
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

        et_translate_search.setOnEditorActionListener { v, actionId, event ->
            searchWord(et_translate_search.text.toString())
            false
        }

    }

    override fun initViews(savedInstanceState: Bundle?) {
        StatusBarUtil.setColor(this,resources.getColor(R.color.white))
        iv_translate_phonetics1.setOnClickListener {
            val animation = iv_translate_phonetics1.drawable as AnimationDrawable
            animation.start()
        }
    }

    fun searchWord(key: String){
        //查询，返回两种情况，一种是成功，相关结果存储在result参数中，
        // 另外一种是失败，失败信息放在TranslateErrorCode中，TranslateErrorCode是一个枚举类，整个查询是异步的，为了简化操作，回调都是在主线程发生。
        translator.lookup(key, "requestId", object : TranslateListener {
            override fun onResult(p0: Translate?, p1: String?, p2: String?) {
                Handler(Looper.getMainLooper()).post({
                    tv_translate_content.text = key
                    RxLogTool.d(p0.toString())
                })
            }

            override fun onResult(p0: MutableList<Translate>?, p1: MutableList<String>?, p2: MutableList<TranslateErrorCode>?, p3: String?) {
                RxLogTool.d(p1)
            }

            override fun onError(p0: TranslateErrorCode?, p1: String?) {
                RxLogTool.d(p1)
            }
        })
    }

}
