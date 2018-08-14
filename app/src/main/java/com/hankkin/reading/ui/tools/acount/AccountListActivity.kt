package com.hankkin.reading.ui.tools.acount

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import com.hankkin.library.utils.ToastUtils
import com.hankkin.reading.R
import com.hankkin.reading.adapter.AccountAdapter
import com.hankkin.reading.base.BaseActivity
import com.hankkin.reading.domain.AccountBean
import com.hankkin.reading.mvp.model.DaoFactory
import com.hankkin.reading.utils.ThemeHelper
import kotlinx.android.synthetic.main.activity_account_list.*
import kotlinx.android.synthetic.main.layout_title_bar_back.*

class AccountListActivity : BaseActivity() {

    private lateinit var mAdapter: AccountAdapter
    private var mData: MutableList<AccountBean>? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_account_list
    }

    override fun initViews(savedInstanceState: Bundle?) {
        tv_normal_title.text = resources.getString(R.string.account_title)
        rv_account.layoutManager = GridLayoutManager(this,2)
        mAdapter = AccountAdapter()
        rv_account.adapter = mAdapter
        iv_back_icon.setOnClickListener { finish() }
        fab_menu_add.setMenuButtonColorNormalResId(ThemeHelper.getCurrentColor(this))
        fab_menu_add.setClosedOnTouchOutside(false)
        fab_menu_add.setOnMenuButtonClickListener { startActivity(Intent(this,AddAcountActivity::class.java)) }
    }

    override fun initData() {
        mData = getData()
        mAdapter.addAll(mData)
        mAdapter.notifyDataSetChanged()
        if (mData == null || mData!!.size == 0){
            ToastUtils.showInfo(this,resources.getString(R.string.account_no_data_hint))
        }
    }

    private fun getData() = DaoFactory.getProtocol(AccountDaoContract::class.java).queryAllAccount()

}
