package com.hankkin.reading.ui.login

import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import com.bilibili.magicasakura.widgets.KeyEditText
import com.hankkin.library.utils.ToastUtils
import com.hankkin.reading.R
import com.hankkin.reading.base.BaseMvpFragment
import com.hankkin.reading.control.UserControl
import com.hankkin.reading.domain.UserBean
import com.hankkin.reading.event.EventMap
import com.hankkin.reading.ui.login.register.RegisterPresenter
import com.hankkin.reading.utils.LoadingUtils
import com.hankkin.library.utils.RxBusTools
import kotlinx.android.synthetic.main.fragment_login.*

/**
 * Created by huanghaijie on 2018/5/15.
 */
class LoginFragment : BaseMvpFragment<LoginContract.IPresenter>(), LoginContract.IView, KeyEditText.KeyPreImeListener {


    override fun registerPresenter() = LoginPresenter::class.java


    override fun getLayoutId(): Int {
        return R.layout.fragment_login
    }

    override fun isHasBus(): Boolean {
        return true
    }

    override fun initData() {
        tv_login_go_reg.setOnClickListener { RxBusTools.getDefault().post(EventMap.LoginSetTabEvent(1)) }
        login_btn.setOnClickListener {
            var map = HashMap<String, Any>()
            map.put(RegisterPresenter.NAME, et_login_name.text.toString())
            map.put(RegisterPresenter.PASSWORD, et_login_pwd.text.toString())
            getPresenter().loginHttp(map)
        }
    }

    override fun initView() {
        et_login_name.setKeyPreImeListener(this)
        et_login_pwd.setKeyPreImeListener(this)
        et_login_pwd.addTextChangedListener(watcher)
        et_login_name.addTextChangedListener(watcher)
    }


    override fun showLoading() {
        LoadingUtils.showLoading(context)
    }

    override fun hideLoading() {
        LoadingUtils.hideLoading()
    }


    private val watcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            login_btn.isEnabled = et_login_name.text.isNotEmpty() && et_login_pwd.text.isNotEmpty()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }

    override fun onKeyPreImeUp(keyCode: Int, event: KeyEvent?) {
        et_login_name.clearFocus()
        et_login_pwd.clearFocus()
    }

    override fun loginResult(userBean: UserBean) {
        context?.let { ToastUtils.showSuccess(it, "登录成功" + userBean.username) }
        UserControl.setCurrentUser(userBean)
        RxBusTools.getDefault().post(EventMap.LoginEvent())
        activity!!.finish()
    }

    override fun onEvent(event: EventMap.BaseEvent) {
        if (event is EventMap.LoginSetTabEvent) {
            et_login_name.setText(event.name)
            et_login_pwd.setText(event.pwd)
        }
    }

}