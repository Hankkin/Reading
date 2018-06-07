package com.hankkin.reading.ui.person

import android.content.Intent
import com.hankkin.reading.R
import com.hankkin.reading.base.BaseFragment
import com.hankkin.reading.common.Constant
import com.hankkin.reading.control.UserControl
import com.hankkin.reading.ui.CommonWebActivity
import com.hankkin.reading.ui.login.LoginActivity
import com.hankkin.reading.utils.Key4Intent
import kotlinx.android.synthetic.main.fragment_person.*

/**
 * Created by huanghaijie on 2018/5/15.
 */
class PersonFragment : BaseFragment<PersonContract.IPresenter>(), PersonContract.IView {



    override fun createmPresenter() = PersonPresenter(this)


    override fun getLayoutId(): Int {
        return R.layout.fragment_person
    }

    override fun initData() {
        ll_person_avatar.setOnClickListener { llAvatarClick() }
    }

    override fun initViews() {
        if (UserControl.isLogin())
            tv_person_name.text = UserControl.getCurrentUser()!!.username
        tv_person_name.text = resources.getString(R.string.person_no_login)

    }

    fun llAvatarClick(){
        if(!UserControl.isLogin()){
            val intent = Intent(activity,CommonWebActivity::class.java)
            intent.putExtra(Key4Intent.KEY_WEB_URL,Constant.OSCHINA_AUTHORIZE)
            intent.putExtra(Key4Intent.KEY_WEB_TITLE,resources.getString(R.string.person_authorize_login))
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        if (UserControl.getCurrentUser() != null){
            tv_person_name.text = UserControl.getCurrentUser()!!.username
        }
    }

}