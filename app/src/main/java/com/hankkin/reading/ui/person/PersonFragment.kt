package com.hankkin.reading.ui.person

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.design.widget.AppBarLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import com.hankkin.library.fuct.android.CaptureActivity
import com.hankkin.library.fuct.bean.ZxingConfig
import com.hankkin.library.utils.AppUtils
import com.hankkin.library.utils.RxBusTools
import com.hankkin.library.utils.SPUtils
import com.hankkin.library.utils.ToastUtils
import com.hankkin.reading.MainActivity
import com.hankkin.reading.R
import com.hankkin.reading.SplashActivity
import com.hankkin.reading.adapter.PersonListAdapter
import com.hankkin.reading.base.BaseMvpFragment
import com.hankkin.reading.common.Constant
import com.hankkin.reading.control.UserControl
import com.hankkin.reading.dao.DaoFactory
import com.hankkin.reading.domain.PersonListBean
import com.hankkin.reading.domain.WordNoteBean
import com.hankkin.reading.event.EventMap
import com.hankkin.reading.ui.home.articledetail.CommonWebActivity
import com.hankkin.reading.ui.login.LoginActivity
import com.hankkin.reading.ui.todo.AddToDoActivity
import com.hankkin.reading.ui.todo.ToDoActivity
import com.hankkin.reading.ui.tools.acount.AccountListActivity
import com.hankkin.reading.ui.tools.acount.LockSetActivity
import com.hankkin.reading.ui.tools.translate.TranslateActivity
import com.hankkin.reading.ui.tools.wordnote.WordNoteActivity
import com.hankkin.reading.ui.tools.wordnote.WordNoteDaoContract
import com.hankkin.reading.utils.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.util.BackpressureHelper.add
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_person.*
import kotlinx.android.synthetic.main.fragment_person.iv_person_change
import kotlinx.android.synthetic.main.fragment_word.*
import kotlinx.android.synthetic.main.layout_card_word.*
import kotlinx.android.synthetic.main.layout_tools.*
import kotlinx.android.synthetic.main.layout_word_every.*
import kotlinx.android.synthetic.main.layout_word_no_data.*
import java.util.*

/**
 * Created by huanghaijie on 2018/5/15.
 */
class PersonFragment : BasePersonFragment() {

    private var mCurrentTheme: Int = 0

    override fun getChildLayoutId() = R.layout.fragment_person

    override fun isHasBus() = true

    @RequiresApi(Build.VERSION_CODES.M)
    override fun initViews() {
        super.initViews()
        initHeaderAnim()
        iv_person_avatar.setOnClickListener { goLogin() }
        iv_person_set.setOnClickListener { startActivity(Intent(context, SettingActivity::class.java)) }
        ll_person_new.setOnClickListener { context?.let { it1 -> AddToDoActivity.intentTo(it1, null) } }
        ll_person_todo.setOnClickListener { startActivity(Intent(context, ToDoActivity::class.java)) }
        ll_person_done.setOnClickListener { startActivity(Intent(context, ToDoActivity::class.java)) }
        iv_person_feedback.setOnClickListener { activity?.let { it1 -> CommonUtils.feedBack(it1) } }
        tv_word_go.setOnClickListener { goTranslate() }
        ll_tools_scan.setOnClickListener { goScan() }
        ll_tools_note.setOnClickListener { goWordNote() }
        ll_tools_word.setOnClickListener { goTranslate() }
        ll_tools_pwd.setOnClickListener { goAccount() }
        iv_person_change.setOnClickListener { changeTheme(1) }
        tv_word_next.setOnClickListener {
            ViewHelper.startShakeAnim(card_word_every)
            setEveryWord()
        }
        tv_word_note.setOnClickListener { goWordNote() }
    }

    override fun initData() {
        setEveryWord()
        initAdapter()
        xrv_person_lisy.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
        mCurrentTheme = ThemeHelper.getTheme(context)
        tv_person_version.text = getVersion()
    }


    private fun initHeaderAnim() {
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


    private fun setUserHeader() {
        tv_person_name.text = if (UserControl.isLogin()) "Hi,${UserControl.getCurrentUser()?.username}" else resources.getString(R.string.person_no_login)
    }

    override fun onEvent(event: EventMap.BaseEvent) {
        super.onEvent(event)
        if (event is EventMap.LoginEvent) {
            setUserHeader()
            mAdapter.apply {
                add(PersonListBean(R.mipmap.icon_person_set_exit, resources.getString(R.string.person_info_logout)))
                mAdapter.notifyDataSetChanged()
            }
        } else if (event is EventMap.PersonClickEvent) {
            when (event.index) {
                4 -> {
                    context?.let {
                        ViewHelper.showConfirmDialog(it,
                                resources.getString(R.string.person_info_logout_hint),
                                MaterialDialog.SingleButtonCallback { dialog, which ->
                                    UserControl.logout()
                                    mAdapter.apply {
                                        remove(data.size - 1)
                                        notifyDataSetChanged()
                                    }
                                    ToastUtils.showInfo(context!!, "已注销登录!")
                                    tv_person_name.text = resources.getString(R.string.person_no_login)
                                })
                    }
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SCAN) {
            if (resultCode == Activity.RESULT_OK) {
                val url = data!!.getStringExtra(com.hankkin.library.fuct.common.Constant.CODED_CONTENT)
                context?.let { CommonWebActivity.loadUrl(it, url, "扫描结果") }
            }
        }
    }

}