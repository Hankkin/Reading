package com.hankkin.reading.ui.login.register

import com.hankkin.reading.R
import com.hankkin.reading.base.BaseMvpFragment
import com.hankkin.reading.control.UserControl
import com.hankkin.reading.domain.CaptchaBean
import com.hankkin.reading.domain.UserBean
import com.hankkin.reading.event.EventMap
import com.hankkin.reading.ui.login.LoginActivity
import com.hankkin.reading.ui.login.register.RegisterPresenter.Companion.CAPTCHA_INPUT
import com.hankkin.reading.ui.login.register.RegisterPresenter.Companion.CAPTCHA_KEY
import com.hankkin.reading.ui.login.register.RegisterPresenter.Companion.EMAIL
import com.hankkin.reading.ui.login.register.RegisterPresenter.Companion.NAME
import com.hankkin.reading.ui.login.register.RegisterPresenter.Companion.PASSWORD
import com.hankkin.reading.ui.login.register.RegisterPresenter.Companion.RPASSWORD
import com.hankkin.reading.utils.LoadingUtils
import com.hankkin.reading.utils.RxBus
import com.hankkin.reading.utils.ToastUtils
import kotlinx.android.synthetic.main.fragment_register.*

/**
 * Created by huanghaijie on 2018/5/15.
 */
class RegFragment : BaseMvpFragment<RegisterContract.IPresenter>(), RegisterContract.IView {
    override fun registerPresenter() = RegisterPresenter::class.java


    lateinit var captchaBean: CaptchaBean

    override fun verifiyFormatResult(msg: String) {
        ToastUtils.showToast(context,msg)
    }

    override fun regResult(userBean: UserBean) {

    }






    override fun getLayoutId(): Int {
        return R.layout.fragment_register
    }

    override fun initData() {
        tv_reg_back.setOnClickListener { RxBus.getDefault().post(EventMap.LoginSetTabEvent(0)) }
    }

    override fun initView() {
        getPresenter().getCapchaHttp()
    }




    override fun showLoading() {
        LoadingUtils.showLoading(context)
    }

    override fun hideLoading() {
        LoadingUtils.hideLoading()
    }

    override fun getCapcha(captchaBean: CaptchaBean) {

    }


}