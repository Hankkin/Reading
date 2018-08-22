package com.hankkin.library.widget.webview

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.ViewGroup
import android.webkit.*
import com.hankkin.library.BuildConfig
import com.hankkin.library.utils.PhoneUtils
import com.hankkin.library.widget.dialog.HLoading
import java.util.*

class WebViewProxy(private val mAct: Activity, private val mWebView: WebView,
                   private val mLoading: HLoading? = null, private val mWebActionCallBack: WebActionCallBack? = null) {

    private val TAG = "WebViewProxy"

    //    private var isError = false
    private var redirect = false
    private var mUrl = ""
    private val loadedUrl = ArrayList<String>()
    private val needReloadUrl = ArrayList<String>()
    private var mHandler: Handler? = Handler()

    init {
        initBaseSetting()
        registerJSInterface()
        initDownListener()
        initWebChromeClient()
        initWebViewClient()
    }



    private fun initWebViewClient() {
        mWebView.webViewClient = object : WebViewClient() {

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
//                isError = true
//                mPageLayout?.setPage(PageStateLayout.PageState.STATE_ERROR)
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                if (mWebActionCallBack != null && mWebActionCallBack.shouldOverrideUrlLoading(view, url)) {
                    return true
                } else if (url.startsWith("http")) {
                    val hit = view.hitTestResult
                    if (hit != null) {
                        return false
                    }
                    redirect = true
                    loadUrl(url)
                    return true
                } else if (url.startsWith("tel:")) {
                    PhoneUtils.callPhone(mAct, url)
                    return true
                }
                try {
                    mAct.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                } catch (e: Exception) {
                    Log.e(TAG, "shouldOverrideUrlLoading", e)
                }
                return false
            }

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                view.settings.blockNetworkImage = true
                redirect = false
                loadedUrl.add(url)
            }

            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler, error: SslError?) {
                handler.proceed()
            }

            override fun onLoadResource(view: WebView?, url: String?) {
                Log.d(TAG, "res -> $url")
                super.onLoadResource(view, url)
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                view.settings.blockNetworkImage = false
                if (needReloadUrl.contains(url)) {
                    needReloadUrl.remove(url)
                    mWebView.reload()
                    return
                }
                if (mLoading != null && mLoading.isShowing) {
                    mWebActionCallBack?.onPageFinished(view, url)
                    if (!redirect) {
                        mHandler?.postDelayed({
                            mLoading.dismiss()
                        }, 100)
                    }
                }
                redirect = false
            }

        }
    }

    private fun initWebChromeClient() {
        mWebView.webChromeClient = object : WebChromeClient() {

//            override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
//                //拦截Alert
//                result?.cancel()
//                return true
//            }

            //配置权限（同样在WebChromeClient中实现）
            override fun onGeolocationPermissionsShowPrompt(origin: String, callback: GeolocationPermissions.Callback) {
                callback.invoke(origin, true, true)
                super.onGeolocationPermissionsShowPrompt(origin, callback)
            }


            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                Log.i(TAG, "progress -> $newProgress")
            }

            override fun onReceivedTitle(view: WebView, title: String) {
                mWebActionCallBack?.onReceivedTitle(view, title)
            }
        }
    }

    private fun initDownListener() {
        mWebView.setDownloadListener { url, _, _, _, _ ->
            val uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            mAct.startActivity(intent)
        }
    }

    @SuppressLint("AddJavascriptInterface")
    private fun registerJSInterface() {
//        mWebView.addJavascriptInterface(JsObject(), "ercarjs")
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initBaseSetting() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG)
        }
        val setting = mWebView.settings
        setting.javaScriptEnabled = true
        setting.javaScriptCanOpenWindowsAutomatically = true
        setting.loadsImagesAutomatically = true
        setting.pluginState = WebSettings.PluginState.ON
        setting.cacheMode = WebSettings.LOAD_DEFAULT
        // 启用DOM存储API
        setting.domStorageEnabled = true // 允许html localStorage
        setting.databaseEnabled = true
        setting.setGeolocationEnabled(true) // 设置定位的数据库路径
        setting.useWideViewPort = true
        setting.setSupportMultipleWindows(true)
        setting.loadWithOverviewMode = true
        setting.setSupportZoom(false)
        setting.setRenderPriority(WebSettings.RenderPriority.HIGH)
        setting.setAppCacheEnabled(true)//缓存
        setting.saveFormData = true


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setting.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
    }

    fun loadUrl(url: String) {
        Log.i(TAG, url)
        this.loadUrl(url, true)
    }

    /**
     * 加载url
     *
     * @param url
     * @param showLoading  是否展示loading
     */
    fun loadUrl(url: String, showLoading: Boolean) {
        if (TextUtils.isEmpty(url)) {
            return
        }
//        isError = false
        mUrl = url
        if (showLoading) {
            mLoading?.show()
        }
        mWebView.loadUrl(mUrl)
    }

    fun callJs(js: String) {
        mWebView.loadUrl(js)
    }

    fun onWebViewPause() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mWebView.onPause()
        }
        mWebView.pauseTimers()
    }

    fun onWebViewResume() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mWebView.onResume()
        }
        mWebView.resumeTimers()
    }

    fun canGoBack(): Boolean {
        return mWebView.canGoBack()
    }

    fun goBack() {
        loadedUrl.remove(mUrl)
        mWebView.goBack()
    }

    fun onDestroy() {
        mHandler = null
        mWebView.stopLoading()
        mWebView.webChromeClient = null
        mWebView.webViewClient = null
        mWebView.settings.javaScriptEnabled = false
        // 清除webview绑定的一切缓存,4.4版本以上会崩溃
        if (Build.VERSION.SDK_INT < 19) {
            mWebView.clearCache(true)
        }

        val group = mWebView.parent as ViewGroup
        group.removeView(mWebView)

        try {
            mWebView.removeAllViews()
            mWebView.destroy()
        } catch (t: Throwable) {
            Log.e(TAG, "destroy", t)
        }

    }

    inner class JsObject {

        @JavascriptInterface
        fun jsCallMethod(json: String) {
            mWebActionCallBack?.jsActionCallBack(json)
        }
    }
}