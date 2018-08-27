package com.hankkin.reading.ui.todo

import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.hankkin.reading.R
import com.hankkin.reading.adapter.ToDoAdapter
import com.hankkin.reading.base.BaseMvpFragment
import com.hankkin.reading.control.UserControl
import com.hankkin.reading.domain.ToDoBean
import com.hankkin.reading.ui.login.LoginActivity
import com.hankkin.reading.utils.ViewHelper
import kotlinx.android.synthetic.main.fragment_todo_list.*

/**
 * @author Hankkin
 * @date 2018/8/26
 */
class ToDoListFragment : BaseMvpFragment<ToDoContract.IPresenter>(), ToDoContract.IView,SwipeRefreshLayout.OnRefreshListener {


    private lateinit var mAdapter: ToDoAdapter
    private var mPage: Int = 0
    private var mIndex: Int = 0
    private var mStyle: Int = 0

    override fun getLayoutId(): Int {
        return R.layout.fragment_todo_list
    }

    override fun registerPresenter() = ToDoPresenter::class.java

    override fun initView() {
        if (UserControl.isLogin()){
            ll_todo_login.visibility = View.GONE
            refresh_todo_list.visibility = View.VISIBLE
            mIndex = arguments!!.getInt("index")
        }
        else{
            ll_todo_login.visibility = View.VISIBLE
            refresh_todo_list.visibility = View.GONE
        }
        iv_todo_login.setOnClickListener { startActivity(Intent(context,LoginActivity::class.java)) }
        ViewHelper.setRefreshLayout(context,true,refresh_todo_list,this)
    }

    override fun initData() {
        mAdapter = ToDoAdapter()
        rv_todo_list.layoutManager = if (mStyle == 0) LinearLayoutManager(context) else GridLayoutManager(context,2)
        rv_todo_list.adapter = mAdapter
        getPresenter().getListDone(mIndex,mPage)
    }

    override fun setListDone(data: ToDoBean) {
        mAdapter.addAll(data.data.todoList)
    }

    override fun onRefresh() {

    }

}