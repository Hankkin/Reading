package com.hankkin.reading.ui.todo

import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.hankkin.library.utils.ToastUtils
import com.hankkin.reading.R
import com.hankkin.reading.adapter.ToDoAdapter
import com.hankkin.reading.base.BaseMvpFragment
import com.hankkin.reading.control.UserControl
import com.hankkin.reading.domain.ToDoBean
import com.hankkin.reading.event.EventMap
import com.hankkin.reading.ui.login.LoginActivity
import com.hankkin.reading.utils.ViewHelper
import kotlinx.android.synthetic.main.fragment_todo_list.*

/**
 * @author Hankkin
 * @date 2018/8/26
 */
class ToDoListFragment : BaseMvpFragment<ToDoContract.IPresenter>(), ToDoContract.IView, SwipeRefreshLayout.OnRefreshListener {
    private lateinit var mAdapter: ToDoAdapter
    private var mIndex: Int = 0
    private var mStyle: Int = 0

    override fun isHasBus(): Boolean {
        return true
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_todo_list
    }

    override fun registerPresenter() = ToDoPresenter::class.java

    override fun initView() {
        initLogin()
        iv_todo_login.setOnClickListener { startActivity(Intent(context, LoginActivity::class.java)) }
        ViewHelper.setRefreshLayout(context, true, refresh_todo_list, this)
    }

    private fun initLogin() {
        if (UserControl.isLogin()) {
            ll_todo_login.visibility = View.GONE
            refresh_todo_list.visibility = View.VISIBLE
            mIndex = arguments!!.getInt("index")
        } else {
            ll_todo_login.visibility = View.VISIBLE
            refresh_todo_list.visibility = View.GONE
        }
    }

    override fun initData() {
        mAdapter = ToDoAdapter()
        rv_todo_list.setLoadingMoreEnabled(false)
        rv_todo_list.setPullRefreshEnabled(false)
        rv_todo_list.layoutManager = if (mStyle == 0) LinearLayoutManager(context) else GridLayoutManager(context, 2)
        rv_todo_list.adapter = mAdapter
        getPresenter().getListDone(mIndex)
    }

    override fun setListDone(data: ToDoBean) {
        val headerTodo = layoutInflater.inflate(R.layout.layout_header_todo, null)
        val headerDone = layoutInflater.inflate(R.layout.layout_header_done, null)
        headerDone.findViewById<TextView>(R.id.tv_done_more).setOnClickListener { context?.let { it1 -> ToastUtils.showInfo(it1, "待开发") } }
        val llDone = headerDone.findViewById<LinearLayout>(R.id.ll_done_container)
        if (data.doneList.size > 0){
            if (data.doneList.size > 3){
                for (i in 0..2){

                }
            }
        }
        rv_todo_list.addHeaderView(headerDone)
        rv_todo_list.addHeaderView(headerTodo)
        mAdapter.clear()
        mAdapter.addAll(data.todoList)
        mAdapter.notifyDataSetChanged()
        refresh_todo_list.isRefreshing = false
    }

    override fun setFail() {
        refresh_todo_list.isRefreshing = false
    }

    override fun onRefresh() {
        refresh_todo_list.isRefreshing = true
        getPresenter().getListDone(mIndex)
    }

    override fun onEvent(event: EventMap.BaseEvent) {
        if (event is EventMap.LoginEvent) {
            initLogin()
            refresh_todo_list.isRefreshing = true
            getPresenter().getListDone(mIndex)
        }
    }

}