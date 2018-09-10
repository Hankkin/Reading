package com.hankkin.reading.ui.home.articledetail

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import com.hankkin.library.widget.view.CoolIndicator
import com.hankkin.library.widget.view.CoolIndicatorLayout
import com.hankkin.reading.R
import com.hankkin.reading.base.BaseMvpActivity
import com.hankkin.reading.utils.CommonUtils
import com.hankkin.reading.utils.LoadingUtils
import com.hankkin.reading.utils.ThemeHelper
import com.jaeger.library.StatusBarUtil
import com.just.agentweb.AgentWeb
import com.just.agentweb.AgentWebSettingsImpl
import com.just.agentweb.DefaultWebClient
import kotlinx.android.synthetic.main.activity_article_detail.*

class CommonWebActivity : BaseMvpActivity<ArticleDetailPresenter>(), ArticleDetailContract.IView {

    private lateinit var mUrl: String
    private lateinit var mTitle: String
    private lateinit var mAgentWeb: AgentWeb


    companion object {
        fun loadUrl(context: Context, url: String, title: String) {
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
        setStatusBarColor()
        getIntentData()
        initWebView()
        initToolBar()
        menuClick()
    }


    private fun getIntentData() {
        if (intent != null) {
            mTitle = intent.getStringExtra("title")
            mUrl = intent.getStringExtra("url")
        }
    }

    @SuppressLint("RestrictedApi")
    private fun initToolBar() {
        toobar_article_detail.apply {
            setSupportActionBar(this)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            supportActionBar?.openOptionsMenu()
        }

        tv_article_detail_title.apply {
            text = mTitle
            postDelayed({ tv_article_detail_title.isSelected = true }, 2000)
        }
    }

    private fun initWebView() {
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(ll_common_web, LinearLayout.LayoutParams(-1, -1))
                .setCustomIndicator(CoolIndicatorLayout(this))
                .setAgentWebWebSettings(AgentWebSettingsImpl.getInstance())
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
                .interceptUnkownUrl()
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)
                .createAgentWeb()
                .go(mUrl)
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
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            return false
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onPause() {
        mAgentWeb.webLifeCycle.onPause()
        super.onPause()
    }

    override fun onResume() {
        mAgentWeb.webLifeCycle.onResume()
        super.onResume()
    }

    override fun onDestroy() {
        mAgentWeb.webLifeCycle.onDestroy()
        super.onDestroy()
    }

}
