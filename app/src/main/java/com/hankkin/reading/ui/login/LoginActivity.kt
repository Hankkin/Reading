package com.hankkin.reading.ui.login

import android.os.Bundle
import android.support.v4.app.Fragment
import com.hankkin.reading.R
import com.hankkin.reading.adapter.MainFragmentAdapter
import com.hankkin.reading.base.BaseActivity
import com.hankkin.reading.event.EventMap
import com.hankkin.reading.ui.login.register.RegFragment
import com.hankkin.reading.utils.RxBus
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
        RxBus.getDefault().toObservable(EventMap.LoginSetTabEvent::class.java)
                .subscribe { vp_login.currentItem = it.index }
    }

    override fun initViews(savedInstanceState: Bundle?) {
        vp_login.adapter = adapter
    }

}
