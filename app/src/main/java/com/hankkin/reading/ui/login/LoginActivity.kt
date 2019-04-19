package com.hankkin.reading.ui.login

import android.os.Bundle
import android.support.v4.app.Fragment
import com.hankkin.reading.R
import com.hankkin.reading.adapter.MainFragmentAdapter
import com.hankkin.reading.base.BaseActivity
import com.hankkin.reading.event.EventMap
import com.hankkin.reading.ui.login.register.RegFragment
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {

    private val fragments = listOf<Fragment>(
            LoginFragment(),
            RegFragment()
    )
    val adapter = MainFragmentAdapter(supportFragmentManager, fragments)


    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun initData() {
    }

    override fun initViews(savedInstanceState: Bundle?) {
        setStatusBarColor()
        vp_login.adapter = adapter
    }

    override fun isHasBus() = true

    override fun onEvent(event: EventMap.BaseEvent) {
        if (event is EventMap.LoginSetTabEvent)
            vp_login.currentItem = event.index
    }
}
