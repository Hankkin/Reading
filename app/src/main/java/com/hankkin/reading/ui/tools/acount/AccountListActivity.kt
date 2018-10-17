package com.hankkin.reading.ui.tools.acount

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.hankkin.library.utils.ToastUtils
import com.hankkin.reading.R
import com.hankkin.reading.adapter.AccountAdapter
import com.hankkin.reading.base.BaseActivity
import com.hankkin.reading.domain.AccountBean
import com.hankkin.reading.event.EventMap
import com.hankkin.reading.dao.DaoFactory
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
        initPageLayout(rv_account)
        setMiuiStatusBar()
        tv_normal_title_white.text = resources.getString(R.string.account_title)
        iv_back_icon_white.setOnClickListener { finish() }
        rv_account.apply {
            layoutManager = LinearLayoutManager(context)
            mAdapter = AccountAdapter()
            adapter = mAdapter
        }
        fab_menu_add.apply {
            setMenuButtonColorNormalResId(ThemeHelper.getCurrentColor(context))
            setClosedOnTouchOutside(false)
            setOnMenuButtonClickListener { startActivityForResult(Intent(context, AddAcountActivity::class.java), REQUEST_CODE) }
        }
        val longClickItems = mutableListOf<String>(resources.getString(R.string.account_look), resources.getString(R.string.account_edit), resources.getString(R.string.account_delete))
        mAdapter.setOnItemLongClickListener { t, position ->
            ViewHelper.showListTitleDialog(this, "操作", longClickItems, MaterialDialog.ListCallback { dialog, itemView, which, text ->
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
        mPageLayout?.showLoading()
        mAdapter.clear()
        mAdapter.apply {
            mData = DaoFactory.getProtocol(AccountDaoContract::class.java).queryAllAccount()
            addAll(mData)
            notifyDataSetChanged()
        }
        if (mData == null || mData!!.size == 0) {
            ToastUtils.showInfo(this, resources.getString(R.string.account_no_data_hint))
            mPageLayout?.showEmpty()
        }
        else{
            mPageLayout?.hide()
        }
    }


    override fun onEvent(event: EventMap.BaseEvent) {
        if (event is EventMap.UpdateAccountListEvent) {
            initData()
        }
    }

}
