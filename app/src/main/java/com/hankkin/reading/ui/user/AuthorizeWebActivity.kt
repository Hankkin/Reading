package com.hankkin.reading.ui.user

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.hankkin.reading.R
import com.hankkin.reading.base.BaseMvpActivity
import com.hankkin.reading.common.Constant
import com.hankkin.reading.domain.UserBean
import com.hankkin.reading.utils.Key4Intent
import com.hankkin.reading.utils.LogUtils
import com.hankkin.reading.utils.SPUtils
import kotlinx.android.synthetic.main.activity_common_web.*
import kotlinx.android.synthetic.main.layout_title_bar_back.*

open class AuthorizeWebActivity : BaseMvpActivity<AuthorizePresenter>(), View.OnClickListener,AuthorizeContract.IView {
    override fun showLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun saveUserInfo(userBean: UserBean) {
        LogUtils.d(userBean.first_name)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_common_web
    }

    override fun registerPresenter() = AuthorizePresenter::class.java


    override fun initData() {
        super.initData()
        iv_back_icon.setOnClickListener(this)
        initSettings()
        loadUrl()
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun initSettings() {
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setting.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        wb_common.webViewClient = object : WebViewClient() {
            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                handler!!.proceed()
            }


            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                handleUrlAndCode(url!!)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }
        }

    }

    fun loadUrl() {
        var url = intent.getStringExtra(Key4Intent.KEY_WEB_URL)
        var title = intent.getStringExtra(Key4Intent.KEY_WEB_TITLE)
        if (!url.isNullOrEmpty()) {
            wb_common.loadUrl(url)
        }
        if (!title.isNullOrEmpty()) {
            tv_normal_title.text = title
        }
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back_icon -> finish()
        }
    }

    fun handleUrlAndCode(url: String){
        if (url.isNotEmpty()){
            if (url.contains("code=")){
                val code = url.substring(url.indexOf("?code=")+6,url.indexOf("&state"))
                if (code.isNotEmpty()){
                    SPUtils.saveObject(Constant.CONSTANT_KEY.CODE,code)
                    getPresenter().getToken(code)
                }
            }
        }
    }

}
