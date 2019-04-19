package com.hankkin.reading.ui.person

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.hankkin.library.fuct.android.CaptureActivity
import com.hankkin.library.fuct.bean.ZxingConfig
import com.hankkin.library.mvp.contract.IBaseViewContract
import com.hankkin.library.mvp.contract.IPresenterContract
import com.hankkin.library.mvp.presenter.RxLifePresenter
import com.hankkin.library.utils.AppUtils
import com.hankkin.library.utils.SPUtils
import com.hankkin.library.utils.ToastUtils
import com.hankkin.reading.R
import com.hankkin.reading.SplashActivity
import com.hankkin.reading.adapter.PersonListAdapter
import com.hankkin.reading.base.BaseFragment
import com.hankkin.reading.base.BaseMvpFragment
import com.hankkin.reading.common.Constant
import com.hankkin.reading.control.UserControl
import com.hankkin.reading.dao.DaoFactory
import com.hankkin.reading.domain.PersonListBean
import com.hankkin.reading.domain.WordNoteBean
import com.hankkin.reading.event.EventMap
import com.hankkin.reading.ui.login.LoginActivity
import com.hankkin.reading.ui.tools.acount.AccountListActivity
import com.hankkin.reading.ui.tools.acount.LockSetActivity
import com.hankkin.reading.ui.tools.translate.TranslateActivity
import com.hankkin.reading.ui.tools.wordnote.WordNoteActivity
import com.hankkin.reading.ui.tools.wordnote.WordNoteDaoContract
import com.hankkin.reading.utils.DBUtils
import com.hankkin.reading.utils.LoadingUtils
import com.hankkin.reading.utils.ThemeHelper
import com.hankkin.reading.utils.ViewHelper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_word.*
import kotlinx.android.synthetic.main.layout_card_word.*
import kotlinx.android.synthetic.main.layout_word_every.*
import java.util.*

/**
 * Created by huanghaijie on 2018/5/15.
 */
abstract class BasePersonFragment : BaseFragment() {

    val REQUEST_CODE_SCAN = 0x1
    lateinit var mAdapter: PersonListAdapter

    private var mCurrentTheme: Int = 0
    private var mWords: MutableList<WordNoteBean>? = null

    abstract fun getChildLayoutId(): Int


    override fun getLayoutId(): Int {
        return getChildLayoutId()
    }

    override fun isHasBus(): Boolean {
        return true
    }

    override fun initViews() {
        initAdapter()
    }

    override fun initData() {
    }

    fun getVersion(): String = "当前版本：" + context?.let { AppUtils.getVersionName(it) }

    fun goTranslate() {
        startActivity(Intent(context, TranslateActivity::class.java))
    }

    fun goWordNote() {
        startActivity(Intent(context, WordNoteActivity::class.java))
    }

    fun goAccount() {
        if (SPUtils.getInt(Constant.SP_KEY.LOCK_OPEN) != 0) {
            startActivity(Intent(context, LockSetActivity::class.java))
        } else {
            startActivity(Intent(context, AccountListActivity::class.java))
        }
    }

    fun goScan() {
        val intent = Intent(context, CaptureActivity::class.java)
        val bundle = Bundle()
        val config = ZxingConfig()
        config.reactColor = ThemeHelper.getCurrentColor(context)
        bundle.putSerializable(com.hankkin.library.fuct.common.Constant.INTENT_ZXING_CONFIG, config)
        intent.putExtras(bundle)
        startActivityForResult(intent, REQUEST_CODE_SCAN)
    }

    fun goLogin() {
        startActivity(
                if (!UserControl.isLogin()) {
                    Intent(context, LoginActivity::class.java)
                } else {
                    Intent(context, PersonInfoActivity::class.java)
                })
    }

    fun changeTheme(flag: Int) {
        context?.let {
            ViewHelper.showConfirmDialog(it, getString(R.string.change_person_theme), MaterialDialog.SingleButtonCallback { dialog, which ->
                SPUtils.put(Constant.SP_KEY.PERSON_THEME, flag)
                startActivity(Intent(it, SplashActivity::class.java))
                activity?.finish()
            })
        }
    }

    fun setEveryWord() {
        mWords = getWords()
        if (mWords != null && mWords!!.size > 0) {
            layout_word_every.visibility = View.VISIBLE
            layout_word_no_data.visibility = View.GONE
            val word = mWords!!.get(Random().nextInt(mWords!!.size))
            tv_word_key.text = word.translateBean.query
            tv_word_content.text = if (word.translateBean.explains == null) "..." else word.translateBean.explains.toString()
            tv_word_phoneic.text = "/${word.translateBean.phonetic}/"
        } else {
            layout_word_every.visibility = View.GONE
            layout_word_no_data.visibility = View.VISIBLE
        }
    }

    private fun getWords() = DaoFactory.getProtocol(WordNoteDaoContract::class.java).queryEmphasisWord()

    fun initAdapter() {
        mAdapter = PersonListAdapter()
        mAdapter.apply {
            data.add(PersonListBean(R.mipmap.icon_person_star, resources.getString(R.string.person_follow)))
            data.add(PersonListBean(R.mipmap.icon_person_list_theme, resources.getString(R.string.person_theme)))
            data.add(PersonListBean(R.mipmap.icon_person_db, resources.getString(R.string.setting_db)))
            data.add(PersonListBean(R.mipmap.icon_person_set_list, resources.getString(R.string.setting)))
            if (UserControl.isLogin()) {
                data.add(PersonListBean(R.mipmap.icon_person_set_exit, resources.getString(R.string.person_info_logout)))
            }
        }
    }


    override fun onEvent(event: EventMap.BaseEvent) {
        when (event) {
            is EventMap.UpdateEveryEvent -> setEveryWord()
            is EventMap.PersonClickEvent -> when (event.index) {
                0 -> startActivity(
                        if (!UserControl.isLogin()) {
                            Intent(context, LoginActivity::class.java)
                        } else {
                            Intent(context, MyCollectActivity::class.java)
                        })
                1 -> startActivity(Intent(context, ThemeActivity::class.java))
                2 -> syncData()
                3 -> context?.startActivity(Intent(context, SettingActivity::class.java))
            }
        }
    }

    /**
     * 数据还原
     */
    fun syncData() {
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