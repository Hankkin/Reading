package com.hankkin.reading.ui.login

import com.hankkin.reading.R
import com.hankkin.reading.base.BaseMvpFragment
import com.hankkin.reading.domain.CaptchaBean
import com.hankkin.reading.domain.UserBean
import com.hankkin.reading.utils.LoadingUtils
import com.hankkin.reading.utils.ToastUtils
import com.wuba.guchejia.img.ImageLoader
import kotlinx.android.synthetic.main.fragment_login.*

/**
 * Created by huanghaijie on 2018/5/15.
 */
class LoginBaseMvpFragment : BaseMvpFragment<LoginContract.IPresenter>(), LoginContract.IView {

    private lateinit var code:String

    override fun loginResult(userBean: UserBean) {
        ToastUtils.showToast(context,"登录成功"+userBean.username)
    }


    override fun registerPresenter() = LoginPresenter::class.java


    override fun getLayoutId(): Int {
        return R.layout.fragment_login
    }

    override fun initData() {
        iv_login_code.setOnClickListener { getPresenter().getCapchaHttp() }
        tv_login_btn.setOnClickListener { loginClick() }
    }

    override fun initView() {
        getPresenter().getCapchaHttp()
    }



    fun loginClick(){
        val map = HashMap<String,Any>()
        map.put("username",et_login_name.text)
        map.put("password",et_login_pwd.text)
        map.put("captcha_1",et_login_code.text)
        map.put("captcha_0",code)
        getPresenter().loginHttp(map)
    }

    override fun showLoading() {
        LoadingUtils.showLoading(context)
    }

    override fun hideLoading() {
        LoadingUtils.hideLoading()
    }

    override fun getCapcha(captchaBean: CaptchaBean) {
        code = captchaBean.key
        if (captchaBean.image_url.isNotEmpty()){
            ImageLoader.load(context,captchaBean.image_url,iv_login_code)
        }
    }


}