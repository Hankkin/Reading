package com.hankkin.reading.ui.tools.acount

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.hankkin.library.utils.EncodeUtils
import com.hankkin.library.utils.SPUtils
import com.hankkin.library.utils.ToastUtils
import com.hankkin.reading.R
import com.hankkin.reading.base.BaseActivity
import com.hankkin.reading.common.Constant
import com.hankkin.reading.domain.AccountBean
import com.hankkin.reading.event.EventMap
import com.hankkin.reading.mvp.model.DaoFactory
import com.hankkin.reading.utils.PatternHelper
import com.hankkin.reading.utils.RxBusTools
import com.hankkin.reading.utils.ThemeHelper
import com.hankkin.reading.utils.ViewHelper
import kotlinx.android.synthetic.main.activity_add_acount.*
import kotlinx.android.synthetic.main.layout_white_title_bar_back.*

class AddAcountActivity : BaseActivity() {

    private var accountId: Long? = null
    private var accountBean: AccountBean? = null

    private val CATE by lazy {
        mutableListOf<String>(Constant.ACCOUNT_CATE.SOCIAL,
                Constant.ACCOUNT_CATE.BANK, Constant.ACCOUNT_CATE.CODE,
                Constant.ACCOUNT_CATE.WORK, Constant.ACCOUNT_CATE.SHOP,
                Constant.ACCOUNT_CATE.EMAIL, Constant.ACCOUNT_CATE.OTHER)
    }


    companion object {
        fun intentTo(context: Context, id: Long){
            val intent = Intent(context,AddAcountActivity::class.java)
            intent.putExtra("id",id)
            context.startActivity(intent)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_add_acount
    }

    override fun initViews(savedInstanceState: Bundle?) {
        setMiuiStatusBar()
        fab_add_acount.setColorPressedResId(ThemeHelper.getCurrentColor(this))
        fab_add_acount.setColorNormalResId(ThemeHelper.getCurrentColor(this))
        tv_normal_title_white.text = resources.getString(R.string.account_add_title)
        iv_back_icon_white.setOnClickListener { finish() }
        tv_add_account_cate.setOnClickListener {
            ViewHelper.showListTitleDialog(this, resources.getString(R.string.account_add_cate_hint), CATE,
                    MaterialDialog.ListCallback { dialog, itemView, which, text ->
                        tv_add_account_cate.text = text
                        iv_add_acount_cate.visibility = View.VISIBLE
                        setCateImg(text.toString())
                        dialog.dismiss()
                    })
        }
        fab_add_acount.setOnClickListener {
            checkMsg()
        }
    }

    override fun initData() {
        accountId = intent.getLongExtra("id",0L)
        if (accountId != 0L){
            accountBean = DaoFactory.getProtocol(AccountDaoContract::class.java).queryAccountById(accountId!!)
            setAccount()
        }
    }

    private fun setAccount(){
        if (accountBean != null){
            et_add_account_name.setText(accountBean!!.name)
            et_add_account_name.setSelection(accountBean!!.name.length)
            et_add_account_number.setText(accountBean!!.number)
            et_add_account_number.setSelection(accountBean!!.number.length)
            et_add_account_password.setText(EncodeUtils.decodePwd(accountBean!!.password))
            et_add_account_password.setSelection(EncodeUtils.decodePwd(accountBean!!.password).length)
            et_add_account_bz.setText(accountBean!!.beizhu)
            et_add_account_bz.setSelection(accountBean!!.beizhu.length)
            tv_add_account_cate.setText(accountBean!!.cate)
            setCateImg(accountBean!!.cate)
        }
    }

    private fun setCateImg(text: String){
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
        saveAccount()
    }

    private fun saveAccount() {
        var accountBean = AccountBean()
        accountBean.cate = tv_add_account_cate.text.toString()
        accountBean.id = if(accountId != 0L) this!!.accountId!! else accountBean.hashCode().toLong()
        accountBean.name = et_add_account_name.text.toString()
        accountBean.number = et_add_account_number.text.toString()
        accountBean.password = EncodeUtils.encodePwd(et_add_account_password.text.toString(),SPUtils.getString(PatternHelper.GESTURE_PWD_KEY))
        accountBean.createAt = System.currentTimeMillis()
        accountBean.beizhu = et_add_account_bz.text.toString()
        if (accountId != 0L){
            DaoFactory.getProtocol(AccountDaoContract::class.java).updateAccountById(accountBean)
        }
        else{
            DaoFactory.getProtocol(AccountDaoContract::class.java).saveAccount(accountBean)
        }
        ToastUtils.showSuccess(this, resources.getString(R.string.account_add_success))
        RxBusTools.getDefault().post(EventMap.UpdateAccountListEvent())
        finish()
    }

}
