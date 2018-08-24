package com.hankkin.reading.ui.home.articledetail

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.hankkin.reading.R
import com.hankkin.reading.base.BaseMvpActivity
import com.hankkin.reading.utils.CommonUtils
import com.hankkin.reading.utils.LoadingUtils
import com.hankkin.reading.utils.ThemeHelper
import com.jaeger.library.StatusBarUtil
import kotlinx.android.synthetic.main.activity_article_detail.*

class CommonWebActivity : BaseMvpActivity<ArticleDetailPresenter>(), ArticleDetailContract.IView {

    private lateinit var mUrl: String
    private lateinit var mTitle: String
    private lateinit var ws: WebSettings


    companion object {
        fun loadUrl(context: Context,url: String, title: String) {
            val intent = Intent(context, CommonWebActivity::class.java)
            intent.putExtra("url", url)
            intent.putExtra("title", title)
            context.startActivity(intent)
        }
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_article_detail
    }

    override fun registerPresenter() = ArticleDetailPresenter::class.java

    override fun collectResult() {
    }

    override fun initView() {
        StatusBarUtil.setColor(this, resources.getColor(ThemeHelper.getCurrentColor(this)), 0)
        getIntentData()
        initWebView()
        initToolBar()
        menuClick()
        web_article.loadUrl(mUrl)
    }


    private fun getIntentData() {
        if (intent != null) {
            mTitle = intent.getStringExtra("title")
            mUrl = intent.getStringExtra("url")
        }
    }

    @SuppressLint("RestrictedApi")
    private fun initToolBar() {
        setSupportActionBar(toobar_article_detail)
        val actionBar = getSupportActionBar()
        if (actionBar != null) {
            //去除默认Title显示
            actionBar!!.setDisplayShowTitleEnabled(false)
        }

        tv_article_detail_title.text = mTitle
        tv_article_detail_title.postDelayed({ tv_article_detail_title.isSelected = true }, 2000)
        supportActionBar!!.openOptionsMenu()
    }

    private fun initWebView() {
        ws = web_article.settings
        // 网页内容的宽度是否可大于WebView控件的宽度
        ws.loadWithOverviewMode = false
        // 保存表单数据
        ws.saveFormData = true
        // 是否应该支持使用其屏幕缩放控件和手势缩放
        ws.setSupportZoom(true)
        ws.builtInZoomControls = true
        ws.displayZoomControls = false
        // 启动应用缓存
        ws.setAppCacheEnabled(true)
        // 设置缓存模式
        ws.cacheMode = WebSettings.LOAD_DEFAULT
        // setDefaultZoom  api19被弃用
        // 设置此属性，可任意比例缩放。
        ws.useWideViewPort = true
        // 不缩放
        web_article.setInitialScale(100)
        // 告诉WebView启用JavaScript执行。默认的是false。
        ws.javaScriptEnabled = true
        //  页面加载好以后，再放开图片
        ws.blockNetworkImage = false
        // 使用localStorage则必须打开
        ws.domStorageEnabled = true
        // 排版适应屏幕
        ws.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
        // WebView是否新窗口打开(加了后可能打不开网页)
        //        ws.setSupportMultipleWindows(true);

        // webview从5.0开始默认不允许混合模式,https中不能加载http资源,需要设置开启。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ws.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        /** 设置字体默认缩放大小(改变网页字体大小,setTextSize  api14被弃用) */
        ws.textZoom = 100

        web_article.setDownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
        web_article.webViewClient = object : WebViewClient(){
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                LoadingUtils.showLoading(this@CommonWebActivity)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                LoadingUtils.hideLoading()
            }
        }
    }


    fun menuClick() {
        toobar_article_detail.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_share -> CommonUtils.share(this@CommonWebActivity, mUrl)
                R.id.menu_open -> CommonUtils.openBroswer(this@CommonWebActivity, mUrl)
                else -> {
                    false
                }
            }
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.article_detail_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }


}
