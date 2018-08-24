package com.hankkin.reading.ui.home.search

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import com.bilibili.magicasakura.widgets.TintTextView
import com.hankkin.library.utils.LogUtils
import com.hankkin.reading.R
import com.hankkin.reading.adapter.SearchHistoryAdapter
import com.hankkin.reading.base.BaseMvpActivity
import com.hankkin.reading.domain.HotBean
import com.hankkin.reading.event.EventMap
import com.hankkin.reading.ui.home.search.searchresult.SearchResultActivity
import com.hankkin.reading.utils.LoadingUtils
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : BaseMvpActivity<SearchPresenter>(),SearchContract.IView {

    private lateinit var mHistoryAdapter: SearchHistoryAdapter

    override fun getLayoutId(): Int {
        return R.layout.activity_search
    }

    override fun isHasBus(): Boolean {
        return true
    }

    override fun initView() {
        setStatusBarColor()
        iv_search_back.setOnClickListener { finish() }
        et_search.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH){
                val hotBean = HotBean()
                hotBean.name = v.text.toString()
                hotBean.id = Math.random().toLong()
                getPresenter().insertDao(hotBean)
                goSearch(v.text.toString())
            }
            false
        }
    }

    override fun initData() {
        getPresenter().getHotHttp()
        getPresenter().queryDao()
    }

    override fun registerPresenter() = SearchPresenter::class.java


    override fun getHotResult(data: MutableList<HotBean>) {
        for (hot in data){
            val item = LayoutInflater.from(this).inflate(R.layout.layout_hot_item,null) as TintTextView
            item.text = hot.name
            item.setOnClickListener {
                getPresenter().insertDao(hot)
                goSearch(hot.name)
            }
            flex.addView(item)
        }
    }

    fun goSearch(key: String){
        val intent = Intent(this@SearchActivity,SearchResultActivity::class.java)
        intent.putExtra("key",key)
        startActivity(intent)
        getPresenter().queryDao()
    }


    override fun insertDao(id: Long) {
        LogUtils.e(">>>>DB"+"插入数据库成功")
    }

    override fun queryResult(hotBean: MutableList<HotBean>) {
        rv_search_history.layoutManager = LinearLayoutManager(this)
        mHistoryAdapter = SearchHistoryAdapter()
        mHistoryAdapter.addAll(hotBean)
        rv_search_history.adapter = mHistoryAdapter
    }


    override fun deleteResult() {
        LogUtils.e(">>>>DB"+"删除数据库成功")
        getPresenter().queryDao()
    }

    override fun showLoading() {
        LoadingUtils.showLoading(this)
    }

    override fun hideLoading() {
        LoadingUtils.hideLoading()
    }

    override fun onEvent(event: EventMap.BaseEvent) {
        val hotBean = mHistoryAdapter.data[(event as EventMap.SearchHistoryDeleteEvent).position]
        getPresenter().delete(hotBean.id)
    }
}
