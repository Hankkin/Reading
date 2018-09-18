package com.hankkin.reading.ui.tools

import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.hankkin.reading.R
import com.hankkin.reading.base.BaseActivity
import com.hankkin.reading.domain.KuaiDiCompanyBean
import com.hankkin.reading.ui.home.articledetail.CommonWebActivity
import com.hankkin.reading.utils.ViewHelper
import kotlinx.android.synthetic.main.activity_kuai_di.*
import kotlinx.android.synthetic.main.layout_title_bar_back.*


class KuaiDiActivity : BaseActivity() {
    var KUAIDI_URL = "https://m.kuaidi100.com/index_all.html"
    private lateinit var mCompanys: MutableList<KuaiDiCompanyBean>
    private lateinit var mSelectCompany: KuaiDiCompanyBean

    override fun getLayoutId(): Int {
        return R.layout.activity_kuai_di
    }

    override fun initData() {
    }

    override fun initViews(savedInstanceState: Bundle?) {
        setStatusBarColor()
        mCompanys = mutableListOf()
        mCompanys.add(KuaiDiCompanyBean("debangwuliu", "德邦物流"))
        mCompanys.add(KuaiDiCompanyBean("shentong", "申通快递"))
        mCompanys.add(KuaiDiCompanyBean("shunfeng", "顺丰快递"))
        mCompanys.add(KuaiDiCompanyBean("tiantian", "天天快递"))
        mCompanys.add(KuaiDiCompanyBean("yuantong", "圆通速递"))
        mCompanys.add(KuaiDiCompanyBean("yunda", "韵达快运"))
        var data = mutableListOf<String>()
        for (c in mCompanys) {
            data.add(c.name)
        }
        tv_normal_title.text = resources.getString(R.string.tools_kuaidi_title)
        iv_back_icon.setOnClickListener { finish() }
        et_kuaidi_number.addTextChangedListener(watcher)
        et_kuaidi_company.addTextChangedListener(watcher)
        et_kuaidi_company.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                showCompany(data)
            }
        }
        et_kuaidi_company.setOnClickListener { showCompany(data) }
        btn_kuaidi.setOnClickListener {
            CommonWebActivity.loadUrl(this, KUAIDI_URL + "?type=" + mSelectCompany.code + "&postid=" + et_kuaidi_number.text.toString(), "查询结果")
        }
    }

    private fun showCompany(data: MutableList<String>){
        et_kuaidi_company.inputType = InputType.TYPE_NULL
        ViewHelper.showListTitleDialog(this,"请选择快递", data,
                MaterialDialog.ListCallback { dialog, itemView, which, text ->
                    et_kuaidi_company.setText(mCompanys[which].name)
                    mSelectCompany = mCompanys[which]
                    dialog.dismiss()
                })
    }


    private val watcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            btn_kuaidi.isEnabled = et_kuaidi_number.text.isNotEmpty() && et_kuaidi_company.text.isNotEmpty()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }

}
