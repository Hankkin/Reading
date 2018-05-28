package com.hankkin.reading.ui.login.register

import android.widget.EditText
import android.widget.ImageView
import butterknife.BindView
import butterknife.OnClick
import com.hankkin.reading.R
import com.hankkin.reading.base.BaseFragment
import com.hankkin.reading.control.UserControl
import com.hankkin.reading.domain.BaseResponse
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
import com.wuba.guchejia.img.ImageLoader

/**
 * Created by huanghaijie on 2018/5/15.
 */
class RegFragment : BaseFragment<RegisterContract.IPresenter>(), RegisterContract.IView {

    @BindView(R.id.iv_reg_code) lateinit var ivCode: ImageView
    @BindView(R.id.et_reg_name) lateinit var etName: EditText
    @BindView(R.id.et_reg_email) lateinit var etEmail: EditText
    @BindView(R.id.et_reg_pwd) lateinit var etPwd: EditText
    @BindView(R.id.et_reg_pwd_repeat) lateinit var etPwdRepeat: EditText
    @BindView(R.id.et_reg_code) lateinit var etCode: EditText

    lateinit var captchaBean: CaptchaBean

    override fun verifiyFormatResult(msg: String) {
        ToastUtils.showToast(context,msg)
    }

    override fun regResult(userBean: UserBean) {
        UserControl.saveUserSp(userBean)
        activity!!.finish()
    }




    override fun createmPresenter() = RegisterPresenter(this)


    override fun getLayoutId(): Int {
        return R.layout.fragment_register
    }

    override fun initData() {
    }

    override fun initViews() {
        getmPresenter().getCapchaHttp()
    }

    @OnClick(R.id.iv_reg_code)
    fun codeClick() {
        getmPresenter().getCapchaHttp()
    }

    @OnClick(R.id.tv_reg_reg)
    fun regClick(){
        val map = HashMap<String,String>()
        map.put(EMAIL,etEmail.text.toString())
        map.put(NAME,etName.text.toString())
        map.put(PASSWORD,etPwd.text.toString())
        map.put(RPASSWORD,etPwdRepeat.text.toString())
        map.put(CAPTCHA_INPUT,etCode.text.toString())
        map.put(CAPTCHA_KEY,captchaBean.key)
        getmPresenter().verifiyFormat(map)
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
            ImageLoader.load(context, captchaBean.image_url, ivCode)
        }
    }


}