package com.hankkin.library.widget.webview

import android.webkit.WebResourceResponse
import android.webkit.WebView

interface WebActionCallBack {
    fun jsActionCallBack(json: String): Boolean
    fun shouldInterceptRequest(view: WebView?, url: String?): WebResourceResponse?
    fun onPageFinished(view: WebView?, url: String?)
    fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean
    fun onReceivedTitle(view: WebView?, title: String?)
}