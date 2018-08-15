package com.hankkin.reading.ui.tools.acount

import android.os.Bundle
import com.afollestad.materialdialogs.MaterialDialog
import com.hankkin.reading.R
import com.hankkin.reading.base.BaseActivity
import com.hankkin.reading.common.Constant
import com.hankkin.reading.utils.ThemeHelper
import com.hankkin.reading.utils.ViewHelper
import kotlinx.android.synthetic.main.activity_add_acount.*

class AddAcountActivity : BaseActivity() {

    private val CATE by lazy {
        mutableListOf<String>(Constant.ACCOUNT_CATE.SOCIAL,
                Constant.ACCOUNT_CATE.BANK,Constant.ACCOUNT_CATE.CODE,
                Constant.ACCOUNT_CATE.WORK,Constant.ACCOUNT_CATE.SHOP,
                Constant.ACCOUNT_CATE.EMAIL,Constant.ACCOUNT_CATE.OTHER)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_add_acount
    }

    override fun initViews(savedInstanceState: Bundle?) {
        setMiuiStatusBar()
        fab_add_acount.setColorPressedResId(ThemeHelper.getCurrentColor(this))
        tv_add_account_cate.setOnClickListener {
            ViewHelper.showListTitleDialog(this,resources.getString(R.string.account_add_cate_hint),CATE,
                    MaterialDialog.ListCallback { dialog, itemView, which, text ->
                        tv_add_account_cate.text = text
                        dialog.dismiss()
                    })
        }
    }

    override fun initData() {
    }


}
