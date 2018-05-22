package com.hankkin.reading.ui.login

import android.widget.ImageView
import butterknife.BindView
import butterknife.OnClick
import com.hankkin.reading.R
import com.hankkin.reading.base.BaseFragment
import com.hankkin.reading.domain.CaptchaBean
import com.hankkin.reading.utils.LoadingUtils
import com.wuba.guchejia.img.ImageLoader

/**
 * Created by huanghaijie on 2018/5/15.
 */
class RegFragment : BaseFragment<LoginContract.IPresenter>(), LoginContract.IView {


    @BindView(R.id.iv_reg_code) lateinit var ivCode: ImageView


    override fun createmPresenter() = LoginPresenter(this)


    override fun getLayoutId(): Int {
        return R.layout.fragment_register
    }

    override fun initData() {
    }

    override fun initViews() {
        getmPresenter().getCapchaHttp()
    }

    @OnClick(R.id.iv_login_code)


    override fun showLoading() {
        LoadingUtils.showLoading(context)
    }

    override fun hideLoading() {
        LoadingUtils.hideLoading()
    }

    override fun getCapcha(captchaBean: CaptchaBean) {
        if (captchaBean.image_url.isNotEmpty()){
            ImageLoader.load(context,captchaBean.image_url,ivCode)
        }
    }



}