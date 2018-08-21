package com.hankkin.reading.ui.person

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.hankkin.library.utils.LogUtils
import com.hankkin.library.utils.SPUtils
import com.hankkin.library.utils.ToastUtils
import com.hankkin.reading.R
import com.hankkin.reading.adapter.PersonListAdapter
import com.hankkin.reading.base.BaseMvpFragment
import com.hankkin.reading.common.Constant
import com.hankkin.reading.control.UserControl
import com.hankkin.reading.domain.NoticeBean
import com.hankkin.reading.domain.PersonListBean
import com.hankkin.reading.event.EventMap
import com.hankkin.reading.ui.login.LoginActivity
import com.hankkin.reading.utils.DBUtils
import com.hankkin.reading.utils.LoadingUtils
import com.hankkin.reading.utils.ViewHelper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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
    private var iv_me_setScale: Float = 0.toFloat()
    private var iv_me_setScaleX: Float = 0.toFloat()
    private var mHeadImgScale: Float = 0.toFloat()

    override fun getLayoutId(): Int {
        return R.layout.fragment_person
    }

    override fun isHasBus(): Boolean {
        return true
    }

    override fun initData() {
        mAdapter = PersonListAdapter()
        mAdapter.data.add(PersonListBean(R.mipmap.icon_person_star, resources.getString(R.string.person_follow)))
        mAdapter.data.add(PersonListBean(R.mipmap.icon_person_list_theme, resources.getString(R.string.person_theme)))
        mAdapter.data.add(PersonListBean(R.mipmap.icon_person_db, resources.getString(R.string.setting_db)))
        mAdapter.data.add(PersonListBean(R.mipmap.icon_person_set_list, resources.getString(R.string.setting)))
        xrv_person_lisy.layoutManager = LinearLayoutManager(context)
        xrv_person_lisy.adapter = mAdapter

    }

    override fun initView() {
        initHeaderAnim()
        refresh_person.setColorSchemeResources(R.color.theme_color_primary)
        refresh_person.setOnRefreshListener { getPresenter().getUserNotice(SPUtils.getString(UserControl.TOKEN)) }
        iv_person_avatar.setOnClickListener { llAvatarClick() }
        tv_me_set.setOnClickListener { startActivity(Intent(context, SettingActivity::class.java)) }
    }

    fun initHeaderAnim() {
        val name = if (!UserControl.isLogin()) {
            "未登录"
        } else UserControl.getCurrentUser()!!.username
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
                val distanceIcon = iv_person_avatar.getY() + (iv_person_avatar.getHeight() - toolbarHeight) / 2.0f
                val distanceIconX = screenW / 2.0f - (iv_person_avatar.getWidth() / 2.0f + resources.getDimension(R.dimen.dp_10))
                mTitleScale = distanceTitle / (initHeight - toolbarHeight)
                tv_me_setScale = distanceSubscribe / (initHeight - toolbarHeight)
                mHeadImgScale = distanceHeadImg / (initHeight - toolbarHeight)
                tv_me_setScaleX = distanceSubscribeX / (initHeight - toolbarHeight)
                iv_me_setScale = distanceIcon / (initHeight - toolbarHeight)
                iv_me_setScaleX = distanceIconX / (initHeight - toolbarHeight)
            }
            val scale = 1.0f - (-verticalOffset / (initHeight - toolbarHeight)) / 2
            LogUtils.e(">>>>>>scale" + scale.toString())
            iv_person_avatar.scaleX = scale
            iv_person_avatar.scaleY = scale
            iv_person_avatar.translationY = mHeadImgScale * verticalOffset
            tv_person_name.translationY = mTitleScale * verticalOffset
            tv_me_set.translationY = tv_me_setScale * verticalOffset
            tv_me_set.translationX = -tv_me_setScaleX * verticalOffset
            iv_person_avatar.translationX = iv_me_setScaleX * verticalOffset
            iv_person_avatar.translationY = iv_me_setScale * verticalOffset
            if (scale == 1f) {
                refresh_person.setEnabled(true)
                tv_person_name.text = name
                tv_me_set.text = "设置"
            } else {
                iv_person_set.visibility = View.GONE
                refresh_person.setEnabled(false)
                tv_me_set.text = "设置"
            }
            if (scale == 0.5f) {
                iv_person_set.visibility = View.VISIBLE
                tv_person_name.text = name
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
            startActivity(Intent(context, LoginActivity::class.java))
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

    override fun onEvent(event: EventMap.BaseEvent) {
        if (event is EventMap.ChangeThemeEvent) {
            ViewHelper.changeRefreshColor(refresh_person, context)
        } else if (event is EventMap.LoginEvent) {
            setUserHeader()
        }else if (event is EventMap.PersonClickEvent){
            when (event.index) {
                2 -> syncData()
                3 -> context!!.startActivity(Intent(context, SettingActivity::class.java))
            }
        }
    }

    /**
     * 数据还原
     */
    private fun syncData(){
        if (context != null) {
            if (SPUtils.getInt(Constant.SP_KEY.LOCK_BACKUP_OPEN) == 1) {
                if (DBUtils.isNeedSync(context!!)){
                    LoadingUtils.showLoading(context)
                    val disposable = Observable.create<Boolean> {
                        try {
                            DBUtils.loadDBData(context!!)
                            it.onNext(true)
                            it.onComplete()
                        } catch (e: Exception) {
                            it.onError(e)
                        }
                    }
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                LoadingUtils.hideLoading()
                                ToastUtils.showInfo(context!!, resources.getString(R.string.setting_lock_data_restore_success))
                            }, {
                                LoadingUtils.hideLoading()
                                ToastUtils.showError(context!!, resources.getString(R.string.setting_lock_data_restore_fail))
                            })
                    disposables.add(disposable)
                }
                else{
                    ToastUtils.showInfo(context!!, resources.getString(R.string.setting_lock_backup_new))
                }
            } else {
                ViewHelper.showConfirmDialog(context!!, context!!.resources.getString(R.string.setting_db_hint),
                        MaterialDialog.SingleButtonCallback { dialog, which ->
                            context!!.startActivity(Intent(context, SettingActivity::class.java))
                        })
            }
        }
    }

}