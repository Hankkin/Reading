package com.hankkin.reading.ui.todo

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.hankkin.library.mvp.presenter.RxLifePresenter
import com.hankkin.library.utils.ToastUtils
import com.hankkin.reading.R
import com.hankkin.reading.adapter.DoneAdapter
import com.hankkin.reading.adapter.base.XRecyclerView
import com.hankkin.reading.base.BaseActivity
import com.hankkin.reading.domain.DoneBean
import com.hankkin.reading.http.HttpClientUtils
import com.hankkin.reading.utils.ViewHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_more_to_do.*
import kotlinx.android.synthetic.main.layout_title_bar_back.*

class MoreToDoActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener {

    private var mPage: Int = 0
    private lateinit var mAdapter: DoneAdapter
    private var mCate: Int = 0

    override fun getLayoutId() = R.layout.activity_more_to_do

    override fun initViews(savedInstanceState: Bundle?) {
        intent?.let {
            mCate = it.getIntExtra("cate", 0)
        }
        tv_normal_title.text = resources.getString(R.string.home_search_hot_more)
        setStatusBarColor()
        ViewHelper.setRefreshLayout(this, true, refresh_mor_todo, this)
        initRV()
    }

    private fun initRV() {
        rv_more_todo.run {
            mAdapter = DoneAdapter()
            layoutManager = LinearLayoutManager(context)
            setPullRefreshEnabled(false)
            clearHeader()
            adapter = mAdapter
            setLoadingListener(object : XRecyclerView.LoadingListener {
                override fun onLoadMore() {
                    loadData()
                }

                override fun onRefresh() {
                }

            })
        }
        mAdapter.setOnItemLongClickListener { t, _ ->
            ViewHelper.showListTitleDialog(this, "操作",
                    mutableListOf(resources.getString(R.string.todo_delete)), MaterialDialog.ListCallback { dialog, itemView, which, text ->
                when (which) {
                    0 -> {
                        ViewHelper.showConfirmDialog(this,
                                resources.getString(R.string.todo_delete_hint),
                                MaterialDialog.SingleButtonCallback { _, _ ->
                                    disposables.add(HttpClientUtils.Builder.getCommonHttp()
                                            .deleteTodo(t.id)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe({
                                                ToastUtils.showSuccess(this, resources.getString(R.string.account_detail_delete_success_hint))
                                                onRefresh()
                                            }, {
                                                ToastUtils.showError(this, resources.getString(R.string.pagelayout_error))
                                            }))
                                })
                    }
                }
            })

        }
    }

    override fun initData() {
        loadData()
    }

    private fun loadData() {
        disposables.add(HttpClientUtils.Builder.getCommonHttp()
                .listDone(mCate, mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    setAdapter(it.data)
                }, {
                    ToastUtils.showError(this@MoreToDoActivity, resources.getString(R.string.pagelayout_error))
                    refresh_mor_todo.isRefreshing = false
                })
        )
    }

    private fun setAdapter(data: DoneBean) {
        mPage = data.curPage
        if (mPage < 2) {
            rv_more_todo.scrollToPosition(0)
            mAdapter.clear()
        }
        mAdapter.addAll(data.datas)
        mAdapter.notifyDataSetChanged()
        if (data.size < 20) {
            rv_more_todo.noMoreLoading()
        }
        rv_more_todo.refreshComplete()
        if (refresh_mor_todo.isRefreshing) {
            refresh_mor_todo.isRefreshing = false
        }
    }

    override fun onRefresh() {
        rv_more_todo.apply {
            reset()
            refresh_mor_todo.isRefreshing = true
            mPage = 0
            loadData()
        }
    }

}
