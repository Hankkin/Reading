package com.hankkin.reading.ui.todo

import com.hankkin.reading.R
import com.hankkin.reading.base.BaseMvpFragment

/**
 * @author Hankkin
 * @date 2018/8/26
 */
class ToDoFragment : BaseMvpFragment<ToDoContract.IPresenter>(),ToDoContract.IView{

    override fun getLayoutId(): Int {
        return R.layout.fragment_todo
    }

    override fun registerPresenter() = ToDoPresenter::class.java

    override fun initView() {
    }

    override fun initData() {

    }

}