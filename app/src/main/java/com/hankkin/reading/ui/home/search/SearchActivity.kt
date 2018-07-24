package com.hankkin.reading.ui.home.search

import android.view.LayoutInflater
import com.bilibili.magicasakura.widgets.TintTextView
import com.hankkin.reading.R
import com.hankkin.reading.base.BaseMvpActivity
import com.hankkin.reading.domain.HotBean
import com.hankkin.reading.utils.LoadingUtils
import com.hankkin.reading.utils.LogUtils
import com.hankkin.reading.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : BaseMvpActivity<SearchPresenter>(),SearchContract.IView {

    override fun getLayoutId(): Int {
        return R.layout.activity_search
    }


    override fun initView() {
        iv_search_back.setOnClickListener { finish() }
    }

    override fun initData() {
        getPresenter().getHotHttp()
    }

    override fun registerPresenter() = SearchPresenter::class.java

    override fun getHotResult(data: MutableList<HotBean>) {
        for (hot in data){
            val item = LayoutInflater.from(this).inflate(R.layout.layout_hot_item,null) as TintTextView
            item.text = hot.name
            item.setOnClickListener { getPresenter().insertDao(hot) }
            flex.addView(item)
        }
    }


    override fun insertDao(id: Long) {
        LogUtils.e(">>>>DB"+"插入数据库成功")
        getPresenter().queryDao(id)
    }

    override fun queryResult(hotBean: MutableList<HotBean>) {
        LogUtils.e(">>>>DB"+hotBean[0].name)
    }

    override fun showLoading() {
        LoadingUtils.showLoading(this)
    }

    override fun hideLoading() {
        LoadingUtils.hideLoading()
    }
}
