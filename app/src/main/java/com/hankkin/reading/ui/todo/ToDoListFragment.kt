package com.hankkin.reading.ui.todo

import android.content.Intent
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.hankkin.library.utils.ToastUtils
import com.hankkin.reading.R
import com.hankkin.reading.adapter.ToDoAdapter
import com.hankkin.reading.adapter.ToDoAdapter.Companion.TYPE_LIFE
import com.hankkin.reading.adapter.ToDoAdapter.Companion.TYPE_ONLY
import com.hankkin.reading.adapter.ToDoAdapter.Companion.TYPE_STUDY
import com.hankkin.reading.adapter.ToDoAdapter.Companion.TYPE_WORK
import com.hankkin.reading.base.BaseMvpFragment
import com.hankkin.reading.control.UserControl
import com.hankkin.reading.domain.ToDoBean
import com.hankkin.reading.domain.ToDoListBean
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
            arguments?.let { mIndex = it.getInt("index") }
        } else {
            ll_todo_login.visibility = View.VISIBLE
            refresh_todo_list.visibility = View.GONE
        }
    }

    override fun initData() {
        mAdapter = ToDoAdapter()
        rv_todo_list.apply {
            setLoadingMoreEnabled(false)
            setPullRefreshEnabled(false)
            layoutManager = if (mStyle == 0) LinearLayoutManager(context) else GridLayoutManager(context, 2)
            adapter = mAdapter
        }
        mAdapter.apply {
            val longClickItems = mutableListOf<String>(resources.getString(R.string.word_note_detail), resources.getString(R.string.word_note_remove), resources.getString(R.string.word_note_emphasis))
            setOnItemClickListener { t, position ->
            }
        }
        getPresenter().getListDone(mIndex)
    }

    override fun setListDone(data: ToDoBean) {
        rv_todo_list.clearHeader()
        val headerTodo = layoutInflater.inflate(R.layout.layout_header_todo, null)
        val headerDone = layoutInflater.inflate(R.layout.layout_header_done, null)
        headerDone.findViewById<TextView>(R.id.tv_done_more).setOnClickListener { context?.let { it1 -> ToastUtils.showInfo(it1, "待开发") } }
        val llDone = headerDone.findViewById<LinearLayout>(R.id.ll_done_container)
        llDone.apply {
            removeAllViews()
            if (data.doneList.size > 0) {
                val dones = data.doneList[0]
                for (d in dones.todoList) {
                    addView(setDoneView(d))
                }
            }
        }

        rv_todo_list.apply {
            clearHeader()
            if (data.doneList.size > 0) {
                addHeaderView(headerDone)
            }
            if (data.todoList.size > 0) {
                addHeaderView(headerTodo)
            }
        }
        mAdapter.apply {
            clear()
            addAll(data.todoList)
            notifyDataSetChanged()
        }

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
        } else if (event is EventMap.LogOutEvent) {
            initLogin()
        }
    }

    private fun setDoneView(toDoListBean: ToDoListBean): View {
        val view = layoutInflater.inflate(R.layout.layout_done_item, null, false)
        val tvTitle = view.findViewById<TextView>(R.id.tv_adapter_todo_title)
        tvTitle.text = toDoListBean.title
        val tvContent = view.findViewById<TextView>(R.id.tv_adapter_todo_content)
        tvContent.text = toDoListBean.content
        val tvTime = view.findViewById<TextView>(R.id.tv_adapter_todo_complete_time)
        tvTime.text = "完成：" + toDoListBean.dateStr
        val tvType = view.findViewById<TextView>(R.id.tv_adapter_todo_type)
        tvType.text = when (toDoListBean.type) {
            TYPE_WORK -> "WORK"
            TYPE_ONLY -> "ONLY"
            TYPE_LIFE -> "LIFE"
            TYPE_STUDY -> "STUDY"
            else -> {
                ""
            }
        }
        return view
    }

}