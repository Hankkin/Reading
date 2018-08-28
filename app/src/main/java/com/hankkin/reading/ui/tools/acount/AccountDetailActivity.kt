package com.hankkin.reading.ui.tools.acount

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.afollestad.materialdialogs.MaterialDialog
import com.hankkin.library.utils.EncryptUtils
import com.hankkin.library.utils.RxBusTools
import com.hankkin.library.utils.ToastUtils
import com.hankkin.reading.R
import com.hankkin.reading.base.BaseActivity
import com.hankkin.reading.common.Constant
import com.hankkin.reading.domain.AccountBean
import com.hankkin.reading.event.EventMap
import com.hankkin.reading.dao.DaoFactory
import com.hankkin.reading.utils.*
import kotlinx.android.synthetic.main.activity_account_detail.*

class AccountDetailActivity : BaseActivity() {

    private var accountBean: AccountBean? = null
    private var id: Long? = 0

    companion object {
        fun intentTo(context: Context, id: Long) {
            val intent = Intent(context, AccountDetailActivity::class.java)
            intent.putExtra("id", id)
            context.startActivity(intent)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_account_detail
    }

    override fun initViews(savedInstanceState: Bundle?) {
        fab_edit_acount.setColorPressedResId(ThemeHelper.getCurrentColor(this))
        fab_edit_acount.setColorNormalResId(ThemeHelper.getCurrentColor(this))
        iv_account_back.setOnClickListener { finish() }
        iv_account_delete.setOnClickListener {
            ViewHelper.showConfirmDialog(this,
                    resources.getString(R.string.account_detail_delete_hint),
                    MaterialDialog.SingleButtonCallback { dialog, which ->
                id?.let { it1 -> DaoFactory.getProtocol(AccountDaoContract::class.java).deleteAccountById(it1) }
                ToastUtils.showInfo(this, resources.getString(R.string.account_detail_delete_success_hint))
                RxBusTools.getDefault().post(EventMap.UpdateAccountListEvent())
                dialog.dismiss()
                finish()
            })
        }
        fab_edit_acount.setOnClickListener {
            id?.let { it1 ->
                AddAcountActivity.intentTo(this, it1)
                finish()
            }
        }

    }

    override fun initData() {
        id = intent.getLongExtra("id", 0)
        accountBean = DaoFactory.getProtocol(AccountDaoContract::class.java).queryAccountById(id!!)
        setAccountLayout()
    }

    private fun setAccountLayout() {
        if (accountBean != null) {
            tv_account_detail_name.text = accountBean!!.name
            tv_account_detail_number.text = accountBean!!.number
            tv_account_detail_cate.text = accountBean!!.cate
            tv_account_detail_bz.text = accountBean!!.beizhu
            tv_account_detail_pwd.text = EncryptUtils.HloveyRC4(accountBean!!.password.toString(), Constant.COMMON.DEFAULT_LOCK_KEY)
        }
    }

}
