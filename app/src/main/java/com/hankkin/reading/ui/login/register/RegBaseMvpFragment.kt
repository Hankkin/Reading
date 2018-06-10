package com.hankkin.reading.ui.login.register

import com.hankkin.reading.R
import com.hankkin.reading.base.BaseMvpFragment
import com.hankkin.reading.control.UserControl
import com.hankkin.reading.domain.CaptchaBean
import com.hankkin.reading.domain.UserBean
import com.hankkin.reading.ui.login.register.RegisterPresenter.Companion.CAPTCHA_INPUT
import com.hankkin.reading.ui.login.register.RegisterPresenter.Companion.CAPTCHA_KEY
import com.hankkin.reading.ui.login.register.RegisterPresenter.Companion.EMAIL
import com.hankkin.reading.ui.login.register.RegisterPresenter.Companion.NAME
import com.hankkin.reading.ui.login.register.RegisterPresenter.Companion.PASSWORD
import com.hankkin.reading.ui.login.register.RegisterPresenter.Companion.RPASSWORD
import com.hankkin.reading.utils.LoadingUtils
import com.hankkin.reading.utils.ToastUtils
import kotlinx.android.synthetic.main.fragment_register.*

/**
 * Created by huanghaijie on 2018/5/15.
 */
class RegBaseMvpFragment : BaseMvpFragment<RegisterContract.IPresenter>(), RegisterContract.IView {
    override fun registerPresenter() = RegisterPresenter::class.java


    lateinit var captchaBean: CaptchaBean

    override fun verifiyFormatResult(msg: String) {
        ToastUtils.showToast(context,msg)
    }

    override fun regResult(userBean: UserBean) {
        UserControl.setCurrentUser(userBean)
        activity!!.finish()
    }






    override fun getLayoutId(): Int {
        return R.layout.fragment_register
    }

    override fun initData() {
        iv_reg_code.setOnClickListener { getPresenter().getCapchaHttp() }
        tv_reg_reg.setOnClickListener { regClick() }
    }

    override fun initView() {
        getPresenter().getCapchaHttp()
    }


    fun regClick(){
        val map = HashMap<String,String>()
        map.put(EMAIL,et_reg_email.text.toString())
        map.put(NAME,et_reg_name.text.toString())
        map.put(PASSWORD,et_reg_pwd.text.toString())
        map.put(RPASSWORD,et_reg_pwd_repeat.text.toString())
        map.put(CAPTCHA_INPUT,et_reg_code.text.toString())
        map.put(CAPTCHA_KEY,captchaBean.key)
        getPresenter().verifiyFormat(map)
    }


    override fun showLoading() {
        LoadingUtils.showLoading(context)
    }

    override fun hideLoading() {
        LoadingUtils.hideLoading()
    }

    override fun getCapcha(captchaBean: CaptchaBean) {
        this.captchaBean = captchaBean
        if (captchaBean.image_url.isNotEmpty()) {
        }
    }


}