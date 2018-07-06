package com.hankkin.reading.ui.person

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import com.hankkin.library.utils.SPUtils
import com.hankkin.reading.R
import com.hankkin.reading.adapter.PersonListAdapter
import com.hankkin.reading.base.BaseMvpFragment
import com.hankkin.reading.common.Constant
import com.hankkin.reading.control.UserControl
import com.hankkin.reading.domain.NoticeBean
import com.hankkin.reading.domain.PersonListBean
import com.hankkin.reading.ui.user.AuthorizeWebActivity
import com.hankkin.reading.utils.Key4Intent
import com.hankkin.reading.utils.LogUtils
import kotlinx.android.synthetic.main.fragment_person.*

/**
 * Created by huanghaijie on 2018/5/15.
 */
class PersonFragment : BaseMvpFragment<PersonContract.IPresenter>(), PersonContract.IView {

    private lateinit var mAdapter: PersonListAdapter

    override fun registerPresenter() = PersonPresenter::class.java

    override fun getLayoutId(): Int {
        return R.layout.fragment_person
    }

    override fun initData() {
        mAdapter = PersonListAdapter()
        mAdapter.data.add(PersonListBean(R.mipmap.icon_person_star,resources.getString(R.string.person_star)))
        mAdapter.data.add(PersonListBean(R.mipmap.icon_person_follow,resources.getString(R.string.person_follow)))
        mAdapter.data.add(PersonListBean(R.mipmap.icon_person_active,resources.getString(R.string.person_active)))
        mAdapter.data.add(PersonListBean(R.mipmap.icon_person_look,resources.getString(R.string.person_look)))
        xrv_person_lisy.layoutManager = LinearLayoutManager(context)
        xrv_person_lisy.adapter = mAdapter
    }

    override fun initView() {
        refresh_person.setColorSchemeResources(R.color.theme_color_primary)
        refresh_person.setOnRefreshListener { getPresenter().getUserNotice(SPUtils.getString(UserControl.TOKEN)) }
        iv_person_avatar.setOnClickListener { llAvatarClick() }
        iv_person_setting.setOnClickListener { startActivity(Intent(context,SettingActivity::class.java)) }
    }

    fun changTheme(){
    }

    fun llAvatarClick() {
        if (!UserControl.isLogin()) {
            val authorizeUrl = Constant.OSChinaUrl.BASE_URL +
                    "oauth2/authorize?response_type=code&client_id=${Constant.OSChinaUrl.CLIENT_ID}&redirect_uri=${Constant.OSChinaUrl.REDIRECT_URL}"
            val intent = Intent(activity, AuthorizeWebActivity::class.java)
            intent.putExtra(Key4Intent.KEY_WEB_URL, authorizeUrl)
            intent.putExtra(Key4Intent.KEY_WEB_TITLE, resources.getString(R.string.person_authorize_login))
            startActivity(intent)
        } else {
            startActivity(Intent(context, PersonInfoActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        setUserHeader()
    }

    fun setUserHeader() {
        if (UserControl.isLogin()) {
            val user = UserControl.getCurrentUser()
            tv_person_name.text = user!!.name
            if (user.avatar.isNotEmpty())
                LogUtils.d(javaClass.simpleName + ">>>>" + user.avatar)
            iv_person_avatar.loadCircleImage(user.avatar, R.mipmap.icon_person_avatar)
            getPresenter().getUserNotice(SPUtils.getString(UserControl.TOKEN))
        } else {
            tv_person_name.text = resources.getString(R.string.person_no_login)
        }
    }

    override fun refresh() {
        refresh_person.isRefreshing = true
    }

    override fun refreshStop() {
        refresh_person.isRefreshing = false
    }

    override fun setNotice(noticeBean: NoticeBean) {
        tv_person_comments.text = noticeBean.replyCount.toString()
        tv_person_fans.text = noticeBean.fansCount.toString()
        tv_person_msg.text = noticeBean.msgCount.toString()
        tv_person_fans.text = noticeBean.fansCount.toString()
    }

}