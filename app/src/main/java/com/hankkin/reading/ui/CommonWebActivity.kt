package com.hankkin.reading.ui

import android.annotation.SuppressLint
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.webkit.*
import com.hankkin.reading.R
import com.hankkin.reading.common.Constant
import com.hankkin.reading.utils.Key4Intent
import com.hankkin.reading.utils.LogUtils
import kotlinx.android.synthetic.main.activity_common_web.*
import kotlinx.android.synthetic.main.layout_title_bar_back.*

class CommonWebActivity : AppCompatActivity(),View.OnClickListener{


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common_web)

        iv_back_icon.setOnClickListener(this)
        initSettings()
        loadUrl()
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun initSettings(){
        val setting = wb_common.settings
        setting.javaScriptEnabled = true
        setting.useWideViewPort = true
        setting.loadWithOverviewMode = true
        setting.domStorageEnabled = true
        setting.setAppCacheEnabled(true)
        setting.javaScriptCanOpenWindowsAutomatically = true
        setting.pluginState = WebSettings.PluginState.ON
        setting.cacheMode = WebSettings.LOAD_NO_CACHE
        setting.setSupportZoom(false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            setting.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        wb_common.webViewClient = object : WebViewClient(){
            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                handler!!.proceed()
            }


            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }
        }

    }

    fun loadUrl(){
        var url = intent.getStringExtra(Key4Intent.KEY_WEB_URL)
        var title = intent.getStringExtra(Key4Intent.KEY_WEB_TITLE)
        if (!url.isNullOrEmpty()){
            url = url+"?response_type=code&client_id=${Constant.WEATHER_CLIENT_ID}&redirect_uri=${Constant.WEATHER_REDURI}"
            wb_common.loadUrl(url)
        }
        if (!title.isNullOrEmpty()){
            tv_normal_title.text = title
        }
    }


    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.iv_back_icon -> finish()
        }
    }
}
