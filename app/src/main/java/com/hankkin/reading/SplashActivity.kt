package com.hankkin.reading

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.hankkin.library.utils.AppUtils
import com.hankkin.reading.base.BaseActivity
import com.hankkin.reading.utils.ThemeHelper
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }

    @SuppressLint("SetTextI18n")
    override fun initViews(savedInstanceState: Bundle?) {
        setMiuiStatusBar()
        iv_logo.setImageResource(ThemeHelper.getCurrentLogo(this))
        tv_splash_version.text = "V"+AppUtils.getVersionName(this)
    }

    override fun initData() {
        Handler().postDelayed({
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        },1500)
    }

}
