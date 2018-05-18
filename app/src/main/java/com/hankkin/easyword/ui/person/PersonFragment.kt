package com.hankkin.easyword.ui.person

import com.hankkin.easyword.R
import com.hankkin.easyword.base.BaseFragment

/**
 * Created by huanghaijie on 2018/5/15.
 */
class PersonFragment : BaseFragment<PersonContract.IPresenter>(), PersonContract.IView {

    override fun createmPresenter() = PersonPresenter(this)


    override fun getLayoutId(): Int {
        return R.layout.fragment_person
    }

    override fun initData() {
    }

    override fun initViews() {
    }

}