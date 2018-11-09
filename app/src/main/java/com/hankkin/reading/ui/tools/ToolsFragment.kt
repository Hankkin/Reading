package com.hankkin.reading.ui.tools

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.hankkin.library.fuct.android.CaptureActivity
import com.hankkin.library.fuct.bean.ZxingConfig
import com.hankkin.library.utils.AppUtils
import com.hankkin.library.utils.SPUtils
import com.hankkin.library.utils.ToastUtils
import com.hankkin.reading.R
import com.hankkin.reading.adapter.PersonListAdapter
import com.hankkin.reading.base.BaseMvpFragment
import com.hankkin.reading.common.Constant
import com.hankkin.reading.control.UserControl
import com.hankkin.reading.dao.DaoFactory
import com.hankkin.reading.domain.PersonListBean
import com.hankkin.reading.domain.Weatherbean
import com.hankkin.reading.domain.WordNoteBean
import com.hankkin.reading.event.EventMap
import com.hankkin.reading.ui.home.articledetail.CommonWebActivity
import com.hankkin.reading.ui.login.LoginActivity
import com.hankkin.reading.ui.person.MyCollectActivity
import com.hankkin.reading.ui.person.PersonInfoActivity
import com.hankkin.reading.ui.person.SettingActivity
import com.hankkin.reading.ui.person.ThemeActivity
import com.hankkin.reading.ui.tools.acount.AccountListActivity
import com.hankkin.reading.ui.tools.acount.LockSetActivity
import com.hankkin.reading.ui.tools.translate.TranslateActivity
import com.hankkin.reading.ui.tools.wordnote.WordNoteActivity
import com.hankkin.reading.ui.tools.wordnote.WordNoteDaoContract
import com.hankkin.reading.utils.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_word.*
import kotlinx.android.synthetic.main.layout_tools.*
import kotlinx.android.synthetic.main.layout_word_every.*
import kotlinx.android.synthetic.main.layout_word_no_data.*
import java.util.*


/**
 * Created by huanghaijie on 2018/5/15.
 */
class ToolsFragment : BaseMvpFragment<ToolsContract.IPresenter>(), ToolsContract.IView {
    val REQUEST_CODE_SCAN = 0x1

    private lateinit var mAdapter: PersonListAdapter

    private var mWords: MutableList<WordNoteBean>? = null


    override fun registerPresenter() = ToolsPresenter::class.java

    override fun isHasBus(): Boolean {
        return true
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_word
    }

    override fun initView() {
        tv_translate_weather.text = "正在获取天气..."
        tv_tools_version.text = context?.let { "当前版本：" + AppUtils.getVersionName(it) }
        tv_word_go.setOnClickListener { startActivity(Intent(context, TranslateActivity::class.java)) }
        tv_word_note.setOnClickListener { startActivity(Intent(context, WordNoteActivity::class.java)) }
        tv_word_next.setOnClickListener {
            ViewHelper.startShakeAnim(card_word_every)
            setEveryWord()
        }
        tv_tools_title.setOnClickListener {
            startActivity(
                    if (!UserControl.isLogin()) {
                        Intent(context, LoginActivity::class.java)
                    } else {
                        Intent(context, PersonInfoActivity::class.java)
                    })
        }
        ll_tools_scan.setOnClickListener {
            val intent = Intent(context, CaptureActivity::class.java)
            val bundle = Bundle()
            val config = ZxingConfig()
            config.reactColor = ThemeHelper.getCurrentColor(context)
            bundle.putSerializable(com.hankkin.library.fuct.common.Constant.INTENT_ZXING_CONFIG, config)
            intent.putExtras(bundle)
            startActivityForResult(intent, REQUEST_CODE_SCAN)
        }
        ll_tools_note.setOnClickListener { startActivity(Intent(context, WordNoteActivity::class.java)) }
        ll_tools_word.setOnClickListener { startActivity(Intent(context, TranslateActivity::class.java)) }
        ll_tools_pwd.setOnClickListener { if (SPUtils.getInt(Constant.SP_KEY.LOCK_OPEN) != 0) {
            startActivity(Intent(context, LockSetActivity::class.java))
        } else {
            startActivity(Intent(context, AccountListActivity::class.java))
        } }
    }


    override fun initData() {
        setEveryWord()
        setSetting()
        getPresenter().getWeather("beijing")
    }


    fun getWords() = DaoFactory.getProtocol(WordNoteDaoContract::class.java).queryEmphasisWord()

    private fun setSetting() {
        mAdapter = PersonListAdapter()
        mAdapter.apply {
            data.add(PersonListBean(R.mipmap.icon_person_star, resources.getString(R.string.person_follow)))
            data.add(PersonListBean(R.mipmap.icon_person_list_theme, resources.getString(R.string.person_theme)))
            data.add(PersonListBean(R.mipmap.icon_person_db, resources.getString(R.string.setting_db)))
            data.add(PersonListBean(R.mipmap.icon_person_set_list, resources.getString(R.string.setting)))
            if (UserControl.isLogin()) {
                tv_tools_title.text = "Hi,${UserControl.getCurrentUser()?.username}"
                data.add(PersonListBean(R.mipmap.icon_person_set_exit, resources.getString(R.string.person_info_logout)))
            } else {
                tv_tools_title.text = "Hi,小猿猿"
            }
        }
        xrv_tools_lisy.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
    }

    private fun setEveryWord() {
        mWords = getWords()
        if (mWords != null && mWords!!.size > 0) {
            layout_word_every.visibility = View.VISIBLE
            layout_word_no_data.visibility = View.GONE
            val word = mWords!!.get(Random().nextInt(mWords!!.size))
            tv_word_key.text = word.translateBean.query
            tv_word_content.text = word.translateBean.explains.toString()
            tv_word_phoneic.text = "/${word.translateBean.phonetic}/"
        } else {
            layout_word_every.visibility = View.GONE
            layout_word_no_data.visibility = View.VISIBLE
        }
    }

    override fun setWeather(weatherbean: Weatherbean) {
        val now = weatherbean.results[0].now
        val format = resources.getString(R.string.format_weather)
        tv_translate_weather.text = String.format(format, now.text, now.temperature)
        iv_translate_weather.setImageResource(WeatherUtils.getWeatherImg(now.code, context))
    }

    override fun setWeatherError() {
        tv_translate_weather.text = "获取天气失败"
    }

    override fun hideLoading() {
        LoadingUtils.hideLoading()
    }

    override fun showLoading() {
        LoadingUtils.showLoading(context)
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

    override fun onEvent(event: EventMap.BaseEvent) {
        if (event is EventMap.UpdateEveryEvent) {
            setEveryWord()
        } else if (event is EventMap.LoginEvent) {
            mAdapter.apply {
                add(PersonListBean(R.mipmap.icon_person_set_exit, resources.getString(R.string.person_info_logout)))
                mAdapter.notifyDataSetChanged()
                if (UserControl.isLogin()) {
                    tv_tools_title.text = "Hi,${UserControl.getCurrentUser()?.username}"
                } else {
                    tv_tools_title.text = "Hi,小猿猿"
                }
            }
        } else if (event is EventMap.PersonClickEvent) {
            when (event.index) {
                0 -> startActivity(
                        if (!UserControl.isLogin()) {
                            Intent(context, LoginActivity::class.java)
                        } else {
                            Intent(context, MyCollectActivity::class.java)
                        })
                1 -> startActivity(Intent(context, ThemeActivity::class.java))
                2 -> syncData()
                3 -> context?.startActivity(Intent(context, SettingActivity::class.java))
                4 -> context?.let {
                    ViewHelper.showConfirmDialog(it,
                            resources.getString(R.string.person_info_logout_hint),
                            MaterialDialog.SingleButtonCallback { dialog, which ->
                                UserControl.logout()
                                mAdapter.apply {
                                    remove(data.size - 1)
                                    notifyDataSetChanged()
                                }
                                ToastUtils.showInfo(context!!, "已注销登录!")
                                tv_tools_title.text = "Hi,小猿猿"
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

