package com.hankkin.reading.ui.login.register

import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import com.afollestad.materialdialogs.MaterialDialog
import com.bilibili.magicasakura.widgets.KeyEditText
import com.hankkin.library.utils.RxBusTools
import com.hankkin.library.utils.ToastUtils
import com.hankkin.reading.R
import com.hankkin.reading.base.BaseMvpFragment
import com.hankkin.reading.control.UserControl
import com.hankkin.reading.domain.UserBean
import com.hankkin.reading.event.EventMap
import com.hankkin.reading.utils.*
import kotlinx.android.synthetic.main.fragment_register.*

/**
 * Created by huanghaijie on 2018/5/15.
 */
class RegFragment : BaseMvpFragment<RegisterContract.IPresenter>(), RegisterContract.IView,KeyEditText.KeyPreImeListener {


    override fun getLayoutId(): Int {
        return R.layout.fragment_register
    }

    override fun initData() {
        tv_reg_back.setOnClickListener { RxBusTools.getDefault().post(EventMap.LoginSetTabEvent(0)) }
        reg_btn.setOnClickListener {
            var map = HashMap<String,String>()
            map.put(RegisterPresenter.NAME,et_reg_email.text.toString())
            map.put(RegisterPresenter.PASSWORD,et_reg_pwd.text.toString())
            map.put(RegisterPresenter.RPASSWORD,et_reg_pwd_repeat.text.toString())
            getPresenter().verifiyFormat(map)
        }
    }

    override fun initView() {
        et_reg_email.setKeyPreImeListener(this)
        et_reg_pwd.setKeyPreImeListener(this)
        et_reg_pwd_repeat.setKeyPreImeListener(this)
        et_reg_email.addTextChangedListener(watcher)
        et_reg_pwd.addTextChangedListener(watcher)
        et_reg_pwd_repeat.addTextChangedListener(watcher)
    }

    private val watcher = object : TextWatcher{
        override fun afterTextChanged(s: Editable?) {
            reg_btn.isEnabled = et_reg_email.text.isNotEmpty() && et_reg_pwd.text.isNotEmpty() && et_reg_pwd_repeat.text.isNotEmpty()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }

    override fun showLoading() {
        LoadingUtils.showLoading(context)
    }

    override fun hideLoading() {
        LoadingUtils.hideLoading()
    }

    override fun registerPresenter() = RegisterPresenter::class.java



    override fun verifiyFormatResult(msg: String) {
        context?.let { ToastUtils.showInfo(it,msg) }
    }

    override fun regResult(userBean: UserBean) {
        UserControl.setCurrentUser(userBean)
        ViewHelper.showConfirmDialog(context!!,
                context!!.resources.getString(R.string.reg_suc_toast),
                MaterialDialog.SingleButtonCallback { dialog, which ->
                    RxBusTools.getDefault().post(EventMap.LoginSetTabEvent(0,et_reg_email.text.toString(),et_reg_pwd.text.toString()))
                })
    }

    override fun onKeyPreImeUp(keyCode: Int, event: KeyEvent?) {
        et_reg_pwd.clearFocus()
        et_reg_email.clearFocus()
        et_reg_pwd_repeat.clearFocus()
    }


}