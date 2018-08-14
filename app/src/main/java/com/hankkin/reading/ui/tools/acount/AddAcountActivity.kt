package com.hankkin.reading.ui.tools.acount

import android.os.Bundle
import com.hankkin.reading.R
import com.hankkin.reading.base.BaseActivity

class AddAcountActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_add_acount
    }

    override fun initViews(savedInstanceState: Bundle?) {
        setMiuiStatusBar()
    }

    override fun initData() {
    }


}
