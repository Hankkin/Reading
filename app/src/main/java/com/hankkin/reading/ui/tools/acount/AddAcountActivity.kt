package com.hankkin.reading.ui.tools.acount

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.hankkin.library.utils.ToastUtils
import com.hankkin.reading.R
import com.hankkin.reading.base.BaseActivity
import com.hankkin.reading.common.Constant
import com.hankkin.reading.domain.AccountBean
import com.hankkin.reading.mvp.model.DaoFactory
import com.hankkin.reading.utils.ThemeHelper
import com.hankkin.reading.utils.ViewHelper
import kotlinx.android.synthetic.main.activity_add_acount.*

class AddAcountActivity : BaseActivity() {

    private val CATE by lazy {
        mutableListOf<String>(Constant.ACCOUNT_CATE.SOCIAL,
                Constant.ACCOUNT_CATE.BANK, Constant.ACCOUNT_CATE.CODE,
                Constant.ACCOUNT_CATE.WORK, Constant.ACCOUNT_CATE.SHOP,
                Constant.ACCOUNT_CATE.EMAIL, Constant.ACCOUNT_CATE.OTHER)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_add_acount
    }

    override fun initViews(savedInstanceState: Bundle?) {
        setMiuiStatusBar()
        fab_add_acount.setColorPressedResId(ThemeHelper.getCurrentColor(this))
        iv_add_acount_back.setOnClickListener { finish() }
        tv_add_account_cate.setOnClickListener {
            ViewHelper.showListTitleDialog(this, resources.getString(R.string.account_add_cate_hint), CATE,
                    MaterialDialog.ListCallback { dialog, itemView, which, text ->
                        tv_add_account_cate.text = text
                        iv_add_acount_cate.visibility = View.VISIBLE
                        iv_add_acount_cate.setImageResource(when (text) {
                            Constant.ACCOUNT_CATE.BANK -> R.mipmap.icon_account_bank
                            Constant.ACCOUNT_CATE.SHOP -> R.mipmap.icon_account_shop
                            Constant.ACCOUNT_CATE.SOCIAL -> R.mipmap.icon_account_social
                            Constant.ACCOUNT_CATE.EMAIL -> R.mipmap.icon_account_email
                            Constant.ACCOUNT_CATE.CODE -> R.mipmap.icon_account_code
                            Constant.ACCOUNT_CATE.WORK -> R.mipmap.icon_account_work
                            Constant.ACCOUNT_CATE.OTHER -> R.mipmap.icon_account_other
                            else -> {
                                R.mipmap.icon_account_other
                            }
                        })
                        dialog.dismiss()
                    })
        }
        fab_add_acount.setOnClickListener {
            checkMsg()
            saveAccount()
        }
    }

    override fun initData() {
    }

    private fun checkMsg() {
        if (et_add_account_name.text.toString().isEmpty()) {
            ToastUtils.showError(this, resources.getString(R.string.account_add_name_input_hint))
            return
        }
        if (et_add_account_number.text.toString().isEmpty()) {
            ToastUtils.showError(this, resources.getString(R.string.account_add_number_input_hint))
            return
        }
        if (et_add_account_password.text.toString().isEmpty()) {
            ToastUtils.showError(this, resources.getString(R.string.account_add_password_input_hint))
            return
        }
        if (tv_add_account_cate.text.toString().isEmpty()) {
            ToastUtils.showError(this, resources.getString(R.string.account_add_cate_select_hint))
            return
        }
    }

    private fun saveAccount() {
        var accountBean = AccountBean()
        accountBean.cate = tv_add_account_cate.text.toString()
        accountBean.id = accountBean.hashCode().toLong()
        accountBean.name = et_add_account_name.text.toString()
        accountBean.number = et_add_account_number.text.toString()
        accountBean.password = et_add_account_password.text.toString()
        accountBean.createAt = System.currentTimeMillis()
        DaoFactory.getProtocol(AccountDaoContract::class.java).saveAccount(accountBean)
        ToastUtils.showSuccess(this, resources.getString(R.string.account_add_success))
        setResult(Activity.RESULT_OK, Intent())
        finish()
    }

}
