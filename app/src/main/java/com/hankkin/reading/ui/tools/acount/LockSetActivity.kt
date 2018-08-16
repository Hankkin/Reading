package com.hankkin.reading.ui.tools.acount

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import com.github.ihsg.patternlocker.OnPatternChangeListener
import com.github.ihsg.patternlocker.PatternLockerView
import com.hankkin.library.utils.SPUtils
import com.hankkin.reading.R
import com.hankkin.reading.base.BaseActivity
import com.hankkin.reading.utils.PatternHelper
import com.hankkin.reading.utils.ThemeHelper
import kotlinx.android.synthetic.main.activity_lock_set.*
import kotlinx.android.synthetic.main.layout_title_bar_back.*

class LockSetActivity : BaseActivity() {

    private lateinit var patternHelper: PatternHelper


    override fun getLayoutId(): Int {
        return R.layout.activity_lock_set
    }

    override fun initViews(savedInstanceState: Bundle?) {
        tv_normal_title.text = resources.getString(R.string.lock_title)
        iv_back_icon.setOnClickListener { finish() }
        setLockStyle()
    }

    override fun initData() {
        patternHelper = PatternHelper()
        pattern_lock_view.setOnPatternChangedListener(object : OnPatternChangeListener{
            override fun onChange(p0: PatternLockerView?, p1: MutableList<Int>?) {

            }

            override fun onComplete(p0: PatternLockerView, p1: MutableList<Int>) {
                val isOk = isPatternOk(p1)
                p0.updateStatus(!isOk)
                pattern_indicator_view.updateState(p1, !isOk)
                updateMsg()
            }

            override fun onClear(p0: PatternLockerView?) {
                finishIfNeeded()
            }

            override fun onStart(p0: PatternLockerView?) {
            }

        })
    }


    private fun setLockStyle(){
        pattern_indicator_view.setFillColor(resources.getColor(R.color.white))
                .setNormalColor(resources.getColor(ThemeHelper.getCurrentColor(this)))
                .setHitColor(resources.getColor(ThemeHelper.getCurrentColor(this)))
                .setErrorColor(resources.getColor(R.color.yima))
                .setLineWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f,
                        resources.displayMetrics))
                .buildWithDefaultStyle()

        pattern_lock_view.setFillColor(resources.getColor(R.color.white))
                .setNormalColor(resources.getColor(ThemeHelper.getCurrentColor(this)))
                .setHitColor(resources.getColor(ThemeHelper.getCurrentColor(this)))
                .setErrorColor(resources.getColor(R.color.yima))
                .setLineWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3f,
                        resources.displayMetrics))
                .buildWithDefaultStyle()

    }

    private fun isPatternOk(hitList: List<Int>): Boolean {
        if (SPUtils.getString(PatternHelper.GESTURE_PWD_KEY).isNotEmpty()){
            this.patternHelper.validateForChecking(hitList)
        }else{
            this.patternHelper.validateForSetting(hitList)
        }
        return this.patternHelper.isOk
    }

    private fun updateMsg() {
        tv_normal_title.text = this.patternHelper.message
        tv_normal_title.setTextColor(if (this.patternHelper.isOk)
            resources.getColor(R.color.white)
        else
            resources.getColor(R.color.white))
    }

    private fun finishIfNeeded() {
        if (this.patternHelper.isFinish) {
            startActivity(Intent(this,AccountListActivity::class.java))
            finish()
        }
    }

}
