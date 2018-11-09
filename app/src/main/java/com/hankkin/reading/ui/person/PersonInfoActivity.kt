package com.hankkin.reading.ui.person

import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.hankkin.library.utils.SPUtils
import com.hankkin.reading.R
import com.hankkin.reading.base.BaseMvpActivity
import com.hankkin.reading.control.UserControl
import com.hankkin.reading.domain.UserInfoBean
import kotlinx.android.synthetic.main.activity_person_info.*
import kotlinx.android.synthetic.main.layout_title_bar_back.*

class PersonInfoActivity : BaseMvpActivity<PersonInfoPresenter>(), PersonInfoContract.IView {


    override fun refresh() {
        refresh_person_info.isRefreshing = true
    }

    override fun refreshStop() {
        refresh_person_info.isRefreshing = false
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_person_info
    }

     override fun registerPresenter(): Class<out PersonInfoPresenter> = PersonInfoPresenter::class.java

    override fun initView() {
        setStatusBarColor()
        tv_normal_title.text = resources.getString(R.string.person_info_title)
        iv_back_icon.setOnClickListener { finish() }
        tv_person_info_logout.setOnClickListener {
            UserControl.logout()
            finish()
        }
        refresh_person_info.setColorSchemeResources(R.color.theme_color_primary)
        refresh_person_info.setOnRefreshListener { initData() }
        iv_person_info_avatar.setOnClickListener { changeAvatar() }
    }

    override fun initData() {
        getPresenter().getUserInfo(SPUtils.getString(UserControl.TOKEN))
    }


    override fun setInfo(userInfoBean: UserInfoBean) {
        if (userInfoBean != null) {
            tv_person_info_name.text = userInfoBean.name
            tv_person_info_expertise.text = userInfoBean.expertise.toString()
            tv_person_info_platforms.text = userInfoBean.platforms.toString()
            tv_person_info_fans.text = userInfoBean.fansCount.toString()
            tv_person_info_favorite.text = userInfoBean.favoriteCount.toString()
            tv_person_info_followers.text = userInfoBean.followersCount.toString()
            tv_person_info_city.text = userInfoBean.city
            tv_person_info_join_time.text = userInfoBean.joinTime
            tv_person_info_last_login_time.text = userInfoBean.lastLoginTime
            when (userInfoBean.gender) {
                1 -> {
                    iv_person_info_gender.setImageResource(R.mipmap.icon_boy)
                    tv_person_info_gender.text = resources.getString(R.string.person_info_boy)
                }
                2 -> {
                    iv_person_info_gender.setImageResource(R.mipmap.icon_girl)
                    tv_person_info_gender.text = resources.getString(R.string.person_info_girl)
                }
            }
        }
    }

    fun changeAvatar(){
        MaterialDialog.Builder(this)
                .items(R.array.select_img_items)
                .itemsCallback { dialog, itemView, which, text ->
//                    SnackbarUtils.Short(ll_person_info_header_info,text.toString())
                }.show()
    }
}
