package com.hankkin.reading.ui.tools.acount

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import com.hankkin.library.utils.RxBusTools
import com.hankkin.reading.R
import com.hankkin.reading.adapter.SelectAppAdapter
import com.hankkin.reading.base.BaseActivity
import com.hankkin.reading.domain.AppInfo
import com.hankkin.reading.event.EventMap
import com.hankkin.reading.utils.CommonUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_select_app.*

class SelectAppActivity : BaseActivity() {

    private lateinit var mAdapter: SelectAppAdapter

    private lateinit var mData: MutableList<AppInfo>


    override fun getLayoutId(): Int {
        return R.layout.activity_select_app
    }

    override fun initViews(savedInstanceState: Bundle?) {
        setMiuiStatusBar()
        initPageLayout(rv_app_list)
        mPageLayout?.showLoading()
    }

    override fun initData() {
        getApps()
        mAdapter = SelectAppAdapter()
        rv_app_list.layoutManager = LinearLayoutManager(this)
        rv_app_list.adapter = mAdapter
        mAdapter.setOnItemClickListener { t, position ->
            RxBusTools.getDefault().post(EventMap.SelectAppEvent(t))
            finish()
        }

        et_add_account_search.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                search(s.toString())
            }

        })

        iv_back_icon_white.setOnClickListener { finish() }
    }

    private fun search(key: String){
        if (key.isEmpty()){
            mAdapter.run {
                clear()
                addAll(mData)
                notifyDataSetChanged()
            }
        }else{
            val newData = mutableListOf<AppInfo>()
            mData.forEach {
                if (it.name.contains(key)){
                    newData.add(it)
                }
            }
            mAdapter.run {
                clear()
                addAll(newData)
                notifyDataSetChanged()
            }
        }

    }

    private fun getApps() {
        Observable.create<MutableList<AppInfo>> {
            mData = CommonUtils.getApps(this)
            it.onNext(mData)
            it.onComplete()
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    mAdapter.addAll(it)
                    mPageLayout?.hide()
                }
    }


}
