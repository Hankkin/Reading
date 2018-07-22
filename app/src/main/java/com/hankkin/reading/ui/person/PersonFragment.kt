package com.hankkin.reading.ui.person

import android.content.Intent
import android.support.design.widget.AppBarLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.hankkin.library.utils.SPUtils
import com.hankkin.reading.R
import com.hankkin.reading.adapter.PersonListAdapter
import com.hankkin.reading.base.BaseMvpFragment
import com.hankkin.reading.control.UserControl
import com.hankkin.reading.domain.NoticeBean
import com.hankkin.reading.domain.PersonListBean
import com.hankkin.reading.event.EventMap
import com.hankkin.reading.ui.login.LoginActivity
import com.hankkin.reading.utils.RxBus
import com.hankkin.reading.utils.ViewHelper
import kotlinx.android.synthetic.main.fragment_android.*
import kotlinx.android.synthetic.main.fragment_person.*

/**
 * Created by huanghaijie on 2018/5/15.
 */
class PersonFragment : BaseMvpFragment<PersonContract.IPresenter>(), PersonContract.IView {

    private lateinit var mAdapter: PersonListAdapter

    override fun registerPresenter() = PersonPresenter::class.java
    private var mSelfHeight = 0f//用以判断是否得到正确的宽高值
    private var mTitleScale: Float = 0.toFloat()
    private var tv_me_setScale: Float = 0.toFloat()
    private var tv_me_setScaleX: Float = 0.toFloat()
    private var mHeadImgScale: Float = 0.toFloat()

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

        RxBus.getDefault().toObservable(EventMap.BaseEvent::class.java)
                .subscribe({
                    if (it is EventMap.ChangeThemeEvent){
                        ViewHelper.changeRefreshColor(refresh_person,context)
                    }
                    else if (it is EventMap.LoginEvent){
                        setUserHeader()
                    }
                })

    }

    override fun initView() {
        initHeaderAnim()
        refresh_person.setColorSchemeResources(R.color.theme_color_primary)
        refresh_person.setOnRefreshListener { getPresenter().getUserNotice(SPUtils.getString(UserControl.TOKEN)) }
        iv_person_avatar.setOnClickListener { llAvatarClick() }
        tv_me_set.setOnClickListener { startActivity(Intent(context,SettingActivity::class.java)) }
    }
    
    fun initHeaderAnim(){
        val screenW = resources.displayMetrics.widthPixels
        val toolbarHeight = resources.getDimension(R.dimen.toolbar_height)
        val initHeight = resources.getDimension(R.dimen.subscription_head)
        app_bar.addOnOffsetChangedListener({ appBarLayout, verticalOffset ->
            if (mSelfHeight == 0f) {
                mSelfHeight = tv_person_name.getHeight().toFloat()
                val distanceTitle = tv_person_name.getTop() + (mSelfHeight - toolbarHeight) / 2.0f
                val distanceSubscribe = tv_me_set.getY() + (tv_me_set.getHeight() - toolbarHeight) / 2.0f
                val distanceHeadImg = iv_person_avatar.getY() + (iv_person_avatar.getHeight() - toolbarHeight) / 2.0f
                val distanceSubscribeX = screenW / 2.0f - (tv_me_set.getWidth() / 2.0f + resources.getDimension(R.dimen.dp_10))
                mTitleScale = distanceTitle / (initHeight - toolbarHeight)
                tv_me_setScale = distanceSubscribe / (initHeight - toolbarHeight)
                mHeadImgScale = distanceHeadImg / (initHeight - toolbarHeight)
                tv_me_setScaleX = distanceSubscribeX / (initHeight - toolbarHeight)
            }
            val scale = 1.0f - -verticalOffset / (initHeight - toolbarHeight)
            iv_person_avatar.setScaleX(scale)
            iv_person_avatar.setScaleY(scale)
            iv_person_avatar.setTranslationY(mHeadImgScale * verticalOffset)
            tv_person_name.setTranslationY(mTitleScale * verticalOffset)
            tv_me_set.setTranslationY(tv_me_setScale * verticalOffset)
            tv_me_set.setTranslationX(-tv_me_setScaleX * verticalOffset)
            if (scale == 1f) {
                refresh_person.setEnabled(true)
                tv_person_name.text = "未登录"
                tv_me_set.text = "设置"
            } else {
                iv_person_set.visibility = View.GONE
                refresh_person.setEnabled(false)
                tv_me_set.text = "设置"
            }
            if (scale == 0f){
                iv_person_set.visibility = View.VISIBLE
                tv_person_name.text = "我的"
                tv_me_set.text = ""
            }
        })
    }
    
    fun llAvatarClick() {
        if (!UserControl.isLogin()) {
//            val authorizeUrl = Constant.OSChinaUrl.BASE_URL +
//                    "oauth2/authorize?response_type=code&client_id=${Constant.OSChinaUrl.CLIENT_ID}&redirect_uri=${Constant.OSChinaUrl.REDIRECT_URL}"
//            val intent = Intent(activity, AuthorizeWebActivity::class.java)
//            intent.putExtra(Key4Intent.KEY_WEB_URL, authorizeUrl)
//            intent.putExtra(Key4Intent.KEY_WEB_TITLE, resources.getString(R.string.person_authorize_login))
//            startActivity(intent)
            startActivity(Intent(context,LoginActivity::class.java))
        } else {
            startActivity(Intent(context, PersonInfoActivity::class.java))
        }
    }


    fun setUserHeader() {
        if (UserControl.isLogin()) {
            val user = UserControl.getCurrentUser()
            tv_person_name.text = user!!.username
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