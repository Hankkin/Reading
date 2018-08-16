package com.hankkin.reading.ui.tools.acount

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.hankkin.library.utils.ToastUtils
import com.hankkin.reading.R
import com.hankkin.reading.adapter.AccountAdapter
import com.hankkin.reading.base.BaseActivity
import com.hankkin.reading.domain.AccountBean
import com.hankkin.reading.event.EventMap
import com.hankkin.reading.mvp.model.DaoFactory
import com.hankkin.reading.utils.ThemeHelper
import com.hankkin.reading.utils.ViewHelper
import kotlinx.android.synthetic.main.activity_account_list.*
import kotlinx.android.synthetic.main.layout_white_title_bar_back.*

class AccountListActivity : BaseActivity() {
    val REQUEST_CODE: Int = 0x1

    private lateinit var mAdapter: AccountAdapter
    private var mData: MutableList<AccountBean>? = null


    override fun isHasBus(): Boolean {
        return true
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_account_list
    }

    override fun initViews(savedInstanceState: Bundle?) {
        setMiuiStatusBar()
        tv_normal_title_white.text = resources.getString(R.string.account_title)
        rv_account.layoutManager = LinearLayoutManager(this)
        mAdapter = AccountAdapter()
        rv_account.adapter = mAdapter
        iv_back_icon_white.setOnClickListener { finish() }
        fab_menu_add.setMenuButtonColorNormalResId(ThemeHelper.getCurrentColor(this))
        fab_menu_add.setClosedOnTouchOutside(false)
        fab_menu_add.setOnMenuButtonClickListener { startActivityForResult(Intent(this, AddAcountActivity::class.java), REQUEST_CODE) }
        val longClickItems = mutableListOf<String>(resources.getString(R.string.account_look), resources.getString(R.string.account_edit), resources.getString(R.string.account_delete))
        mAdapter.setOnItemLongClickListener { t, position ->
            ViewHelper.showListNoTitleDialog(this, longClickItems, MaterialDialog.ListCallback { dialog, itemView, which, text ->
                when (which) {
                    0 -> {
                        AccountDetailActivity.intentTo(this, t.id)
                    }
                    1 -> {
                        AddAcountActivity.intentTo(this, t.id)
                    }
                    2 -> {
                        DaoFactory.getProtocol(AccountDaoContract::class.java).deleteAccountById(t.id)
                        initData()
                    }
                }
            })
        }
        mAdapter.setOnItemClickListener { t, position ->
            AccountDetailActivity.intentTo(this, t.id)
        }
    }

    override fun initData() {
        mData = getData()
        mAdapter.clear()
        mAdapter.addAll(mData)
        mAdapter.notifyDataSetChanged()
        if (mData == null || mData!!.size == 0) {
            ToastUtils.showInfo(this, resources.getString(R.string.account_no_data_hint))
        }
    }

    private fun getData() = DaoFactory.getProtocol(AccountDaoContract::class.java).queryAllAccount()


    override fun onEvent(event: EventMap.BaseEvent) {
        if (event is EventMap.UpdateAccountListEvent){
            initData()
        }
    }

}
