package com.hankkin.reading.ui.login

import com.hankkin.reading.R
import com.hankkin.reading.base.BaseMvpFragment
import com.hankkin.reading.domain.CaptchaBean
import com.hankkin.reading.domain.UserBean
import com.hankkin.reading.event.EventMap
import com.hankkin.reading.utils.LoadingUtils
import com.hankkin.reading.utils.RxBus
import com.hankkin.reading.utils.ToastUtils
import kotlinx.android.synthetic.main.fragment_login.*

/**
 * Created by huanghaijie on 2018/5/15.
 */
class LoginFragment : BaseMvpFragment<LoginContract.IPresenter>(), LoginContract.IView {

    private lateinit var code:String

    override fun loginResult(userBean: UserBean) {
        ToastUtils.showToast(context,"登录成功"+userBean.name)
    }


    override fun registerPresenter() = LoginPresenter::class.java


    override fun getLayoutId(): Int {
        return R.layout.fragment_login
    }

    override fun initData() {
        tv_login_go_reg.setOnClickListener { RxBus.getDefault().post(EventMap.LoginSetTabEvent(1)) }
    }

    override fun initView() {

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
//            ImageLoader.load(context,captchaBean.image_url,iv_login_code)
        }
    }


}