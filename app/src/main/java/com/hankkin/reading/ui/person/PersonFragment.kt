package com.hankkin.reading.ui.person

import com.hankkin.reading.R
import com.hankkin.reading.base.BaseFragment

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