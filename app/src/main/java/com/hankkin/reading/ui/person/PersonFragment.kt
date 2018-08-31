package com.hankkin.reading.ui.person

import android.content.Intent
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.design.widget.AppBarLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.hankkin.library.utils.AppUtils
import com.hankkin.library.utils.RxBusTools
import com.hankkin.library.utils.SPUtils
import com.hankkin.library.utils.ToastUtils
import com.hankkin.reading.MainActivity
import com.hankkin.reading.R
import com.hankkin.reading.adapter.PersonListAdapter
import com.hankkin.reading.base.BaseMvpFragment
import com.hankkin.reading.common.Constant
import com.hankkin.reading.control.UserControl
import com.hankkin.reading.domain.PersonListBean
import com.hankkin.reading.event.EventMap
import com.hankkin.reading.ui.login.LoginActivity
import com.hankkin.reading.ui.todo.AddToDoActivity
import com.hankkin.reading.utils.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_person.*

/**
 * Created by huanghaijie on 2018/5/15.
 */
class PersonFragment : BaseMvpFragment<PersonContract.IPresenter>(), PersonContract.IView {

    private lateinit var mAdapter: PersonListAdapter
    private var mCurrentTheme: Int = 0

    override fun registerPresenter() = PersonPresenter::class.java

    override fun getLayoutId(): Int {
        return R.layout.fragment_person
    }

    override fun isHasBus(): Boolean {
        return true
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun initView() {
        initHeaderAnim()
        iv_person_avatar.setOnClickListener { llAvatarClick() }
        iv_person_set.setOnClickListener { startActivity(Intent(context, SettingActivity::class.java)) }
        ll_person_new.setOnClickListener { context?.let { it1 -> AddToDoActivity.intentTo(it1, null) } }
        ll_person_todo.setOnClickListener { (activity as MainActivity).setTabColor(1) }
        ll_person_done.setOnClickListener { (activity as MainActivity).setTabColor(1) }
        iv_person_feedback.setOnClickListener {
            activity?.let { it1 -> CommonUtils.feedBack(it1) }
        }
    }

    override fun initData() {
        initAdapter()
        xrv_person_lisy.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
        mCurrentTheme = ThemeHelper.getTheme(context)
        tv_person_version.text = context?.let { "当前版本：" + AppUtils.getVersionName(it) }
    }

    private fun initAdapter(){
        mAdapter = PersonListAdapter()
        mAdapter.apply {
            data.add(PersonListBean(R.mipmap.icon_person_star, resources.getString(R.string.person_follow)))
            data.add(PersonListBean(R.mipmap.icon_person_list_theme, resources.getString(R.string.person_theme)))
            data.add(PersonListBean(R.mipmap.icon_person_db, resources.getString(R.string.setting_db)))
            data.add(PersonListBean(R.mipmap.icon_person_set_list, resources.getString(R.string.setting)))
            if (UserControl.isLogin()){
                data.add(PersonListBean(R.mipmap.icon_person_set_exit, resources.getString(R.string.person_info_logout)))
            }
        }
    }


    fun initHeaderAnim() {
        val name = if (!UserControl.isLogin()) {
            "未登录"
        } else {
            UserControl.getCurrentUser()!!.username
        }
        tv_person_name.text = name
        ll_person_header.apply {
            viewTreeObserver.addOnGlobalLayoutListener {
                app_bar.apply {
                    addOnOffsetChangedListener(object : AppBarStateChangeListener() {
                        override fun onStateChanged(appBarLayout: AppBarLayout?, state: State?) {
                        }

                        override fun onAppBarOffsetChanged(state: State?, appBarLayout: AppBarLayout?, i: Int) {
                            when (state) {
                                AppBarStateChangeListener.State.COLLAPSED -> {
                                    tv_person_title.visibility = View.VISIBLE
                                    ll_person_header.visibility = View.GONE
                                }
                                else -> {
                                    tv_person_title.visibility = View.GONE
                                    ll_person_header.visibility = View.VISIBLE
                                }
                            }
                            when {
                                i == 0 -> ll_person_header.alpha = 1F
                                i in 1..(height - 30) -> ll_person_header.alpha = ((height - i).toFloat() / height)
                                i > height - 30 -> ll_person_header.alpha = 0F
                            }
                        }

                    })
                }
            }
        }
    }

    private fun llAvatarClick() {
        startActivity(
                if (!UserControl.isLogin()) {
                    Intent(context, LoginActivity::class.java)
                } else {
                    Intent(context, PersonInfoActivity::class.java)
                })
    }


    private fun setUserHeader() {
        if (UserControl.isLogin()) {
            val user = UserControl.getCurrentUser()
            tv_person_name.text = user!!.username
        } else {
            tv_person_name.text = resources.getString(R.string.person_no_login)
        }
    }


    override fun onEvent(event: EventMap.BaseEvent) {
        if (event is EventMap.LoginEvent) {
            setUserHeader()
            initAdapter()
            mAdapter.apply {
                add(PersonListBean(R.mipmap.icon_person_set_exit, resources.getString(R.string.person_info_logout)))
                mAdapter.notifyDataSetChanged()
            }
        } else if (event is EventMap.PersonClickEvent) {
            when (event.index) {
                0 -> startActivity(Intent(context, MyCollectActivity::class.java))
                1 -> startActivity(Intent(context, ThemeActivity::class.java))
                2 -> syncData()
                3 -> context?.startActivity(Intent(context, SettingActivity::class.java))
                4 -> context?.let {
                    ViewHelper.showConfirmDialog(it,
                            resources.getString(R.string.person_info_logout_hint),
                            MaterialDialog.SingleButtonCallback { dialog, which ->
                                UserControl.logout()
                                setUserHeader()
                                mAdapter.apply {
                                    remove(data.size-1)
                                    notifyDataSetChanged()
                                }
                                RxBusTools.getDefault().post(EventMap.LogOutEvent())
                                ToastUtils.showInfo(context!!, "已注销登录!")
                            })
                }
            }
        }
    }

    /**
     * 数据还原
     */
    private fun syncData() {
        context?.let {
            if (SPUtils.getInt(Constant.SP_KEY.LOCK_BACKUP_OPEN) == 1) {
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
            } else {
                ViewHelper.showConfirmDialog(context!!, context!!.resources.getString(R.string.setting_db_hint),
                        MaterialDialog.SingleButtonCallback { dialog, which ->
                            context!!.startActivity(Intent(context, SettingActivity::class.java))
                        })
            }
        }
    }

}