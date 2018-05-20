package com.hankkin.reading.ui.login

import com.hankkin.reading.R
import com.hankkin.reading.base.BaseFragment
import com.hankkin.reading.ui.person.PersonContract
import com.hankkin.reading.ui.person.PersonPresenter

/**
 * Created by huanghaijie on 2018/5/15.
 */
class LoginFragment : BaseFragment<PersonContract.IPresenter>(), PersonContract.IView {

    override fun createmPresenter() = PersonPresenter(this)


    override fun getLayoutId(): Int {
        return R.layout.fragment_login
    }

    override fun initData() {
    }

    override fun initViews() {
    }

}