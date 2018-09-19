package com.hankkin.reading.ui.todo

import android.content.Intent
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.hankkin.library.utils.ToastUtils
import com.hankkin.reading.R
import com.hankkin.reading.adapter.DoneAdapter
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

    private lateinit var mTodoAdapter: ToDoAdapter
    private lateinit var mDoneAdapter: DoneAdapter
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
        val doneLongClickItems = mutableListOf<String>(context!!.resources.getString(R.string.todo_delete))
        mTodoAdapter = ToDoAdapter()
        rv_todo_list.apply {
            setLoadingMoreEnabled(false)
            setPullRefreshEnabled(false)
            layoutManager = if (mStyle == 0) LinearLayoutManager(context) else GridLayoutManager(context, 2)
            adapter = mTodoAdapter
        }
        mDoneAdapter = DoneAdapter()
        mDoneAdapter.setOnItemLongClickListener { t, position ->
            context?.let {
                ViewHelper.showListTitleDialog(it, "操作",
                        doneLongClickItems, MaterialDialog.ListCallback { dialog, itemView, which, text ->
                    when (which) {
                        0 -> {
                            ViewHelper.showConfirmDialog(context!!,
                                    context!!.resources.getString(R.string.todo_delete_hint),
                                    MaterialDialog.SingleButtonCallback { dialog, which ->
                                        getPresenter().deleteTodo(t.id)
                                    })
                        }
                    }
                })
            }

        }
        rv_done_list.apply {
            setLoadingMoreEnabled(false)
            setPullRefreshEnabled(false)
            layoutManager = if (mStyle == 0) LinearLayoutManager(context) else GridLayoutManager(context, 2)
            adapter = mDoneAdapter
        }
        getPresenter().getListDone(mIndex)
    }

    override fun setListDone(data: ToDoBean) {
        rv_todo_list.apply {
            clearHeader()
            if (data.todoList.size > 0) {
                val headerTodo = layoutInflater.inflate(R.layout.layout_header_todo, null)
                addHeaderView(headerTodo)
            }
        }
        mTodoAdapter.apply {
            clear()
            addAll(data.todoList)
            notifyDataSetChanged()
        }
        rv_done_list.apply {
            clearHeader()
            if (data.doneList.size > 0) {
                val headerDone = layoutInflater.inflate(R.layout.layout_header_done, null)
                        .apply {
                            findViewById<TextView>(R.id.tv_done_more).setOnClickListener {
                                Intent(activity,MoreToDoActivity::class.java).apply {
                                    putExtra("cate",mIndex)
                                    startActivity(this)
                                }
                            }
                        }
                addHeaderView(headerDone)
            }
            mDoneAdapter.apply {
                clear()
                addAll(data.doneList[data.doneList.size-1].todoList)
                notifyDataSetChanged()
            }
        }
        refresh_todo_list.isRefreshing = false
    }

    override fun deleteTodoSuccess() {
        context?.let {
            ToastUtils.showSuccess(it, resources.getString(R.string.account_detail_delete_success_hint))
            onRefresh()
        }
    }

    override fun completeTodo() {
        context?.let {
            ToastUtils.showSuccess(it, resources.getString(R.string.account_detail_complete_success_hint))
            onRefresh()
        }
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
        } else if (event is EventMap.ToDoRefreshEvent) {
            onRefresh()
        } else if (event is EventMap.DeleteToDoEvent) {
            getPresenter().deleteTodo(event.id)
        } else if (event is EventMap.CompleteToDoEvent) {
            getPresenter().completeTo(event.bean.id)
        }else if (event is EventMap.ChangeThemeEvent){
            ViewHelper.changeRefreshColor(refresh_todo_list,context)
        }
    }


}