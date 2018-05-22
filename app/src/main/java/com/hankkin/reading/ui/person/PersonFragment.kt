package com.hankkin.reading.ui.person

import android.content.Intent
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.OnClick
import com.hankkin.reading.R
import com.hankkin.reading.base.BaseFragment
import com.hankkin.reading.control.UserControl
import com.hankkin.reading.ui.login.LoginActivity

/**
 * Created by huanghaijie on 2018/5/15.
 */
class PersonFragment : BaseFragment<PersonContract.IPresenter>(), PersonContract.IView {

    @BindView(R.id.iv_person_avatar) lateinit var ivAvatar: ImageView
    @BindView(R.id.tv_person_name) lateinit var tvName: TextView



    override fun createmPresenter() = PersonPresenter(this)


    override fun getLayoutId(): Int {
        return R.layout.fragment_person
    }

    override fun initData() {
    }

    override fun initViews() {
        if (UserControl.isLogin())
            tvName.text = UserControl.getCurrentUser().username
        tvName.text = resources.getString(R.string.person_no_login)

    }

    @OnClick(R.id.ll_person_avatar) fun llAvatarClick(){
        if(!UserControl.isLogin()){
            startActivity(Intent(activity,LoginActivity::class.java))
        }
    }

}