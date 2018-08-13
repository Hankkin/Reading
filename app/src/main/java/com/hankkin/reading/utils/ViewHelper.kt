package com.hankkin.reading.utils

import android.R.attr.duration
import android.animation.Animator
import android.animation.Keyframe
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.support.design.widget.BottomSheetDialog
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.hankkin.library.utils.ToastUtils
import com.hankkin.reading.R
import com.hankkin.reading.common.Constant
import com.hankkin.reading.ui.home.articledetail.CommonWebActivity


/**
 * Created by huanghaijie on 2018/7/10.
 */
object ViewHelper {

    /**
     * 设置下拉刷新组件
     */
    fun setRefreshLayout(context: Context?, isAutoRefresh: Boolean,
                         layout: SwipeRefreshLayout,
                         onRefreshListener: SwipeRefreshLayout.OnRefreshListener) {
        layout.setColorSchemeResources(ThemeHelper.getCurrentColor(context))
        layout.setOnRefreshListener(onRefreshListener)
        if (isAutoRefresh) layout.isRefreshing = true
    }

    fun changeRefreshColor(layout: SwipeRefreshLayout, context: Context?) {
        layout.setColorSchemeResources(ThemeHelper.getCurrentColor(context))
    }

    /**
     * normal dialog
     */
    fun showConfirmDialog(context: Context, content: String, callback: MaterialDialog.SingleButtonCallback) {
        MaterialDialog.Builder(context)
                .content(content)
                .positiveText(context.resources.getString(R.string.ok))
                .negativeText(context.resources.getString(R.string.cancel))
                .onPositive(callback)
                .show()

    }

    /**
     * list dialog
     */
    fun showListNoTitleDialog(context: Context,list: MutableList<String>,calback: MaterialDialog.ListCallback){
        MaterialDialog.Builder(context)
                .items(list)
                .itemsCallback(calback)
                .show()
    }

    /**
     * about dialog
     */
    fun showAboutDialog(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_about_dialog,null)
        view.findViewById<ImageView>(R.id.iv_about_github).setOnClickListener { CommonWebActivity.loadUrl(context,Constant.AboutUrl.GITHUB,Constant.AboutUrl.GITHUB_TITLE) }
        view.findViewById<ImageView>(R.id.iv_about_juejin).setOnClickListener { CommonWebActivity.loadUrl(context,Constant.AboutUrl.JUEJIN,Constant.AboutUrl.JUEJIN_TITLE) }
        view.findViewById<ImageView>(R.id.iv_about_jianshu).setOnClickListener { CommonWebActivity.loadUrl(context,Constant.AboutUrl.JIANSHU,Constant.AboutUrl.JIANSHU_TITLE) }
        view.findViewById<ImageView>(R.id.iv_about_csdn).setOnClickListener { CommonWebActivity.loadUrl(context,Constant.AboutUrl.CSDN,Constant.AboutUrl.CSDN_TITLE) }
        val bottomSheet = BottomSheetDialog(context,R.style.BottomSheetDialog)
        view.findViewById<TextView>(R.id.tv_about_close).setOnClickListener { bottomSheet.dismiss() }
        view.findViewById<TextView>(R.id.tv_about_rate).setOnClickListener { ToastUtils.showInfo(context,"敬请期待") }
        bottomSheet.setContentView(view)
        bottomSheet.show()
    }

    /**
     * 抖动动画
     */
    fun startShakeAnim(view: View){
        //先变小后变大
        val scaleXValuesHolder = PropertyValuesHolder.ofKeyframe(View.SCALE_X,
                Keyframe.ofFloat(0f, 1.0f),
                Keyframe.ofFloat(0.25f, 0.9f),
                Keyframe.ofFloat(0.5f, 1.1f),
                Keyframe.ofFloat(0.75f, 1.1f),
                Keyframe.ofFloat(1.0f, 1.0f)
        )
        val scaleYValuesHolder = PropertyValuesHolder.ofKeyframe(View.SCALE_Y,
                Keyframe.ofFloat(0f, 1.0f),
                Keyframe.ofFloat(0.25f, 0.9f),
                Keyframe.ofFloat(0.5f, 1.1f),
                Keyframe.ofFloat(0.75f, 1.1f),
                Keyframe.ofFloat(1.0f, 1.0f)
        )

        //先往左再往右
        val rotateValuesHolder = PropertyValuesHolder.ofKeyframe(View.ROTATION,
                Keyframe.ofFloat(0f, 0f),
                Keyframe.ofFloat(0.1f, -10f),
                Keyframe.ofFloat(0.2f, 10f),
                Keyframe.ofFloat(0.3f, -10f),
                Keyframe.ofFloat(0.4f, 10f),
                Keyframe.ofFloat(0.5f, -10f),
                Keyframe.ofFloat(0.6f, 10f),
                Keyframe.ofFloat(0.7f, -10f),
                Keyframe.ofFloat(0.8f, 10f),
                Keyframe.ofFloat(0.9f, -10f),
                Keyframe.ofFloat(1.0f, 0f)
        )

        val objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, scaleXValuesHolder, scaleYValuesHolder, rotateValuesHolder)
        objectAnimator.duration = duration.toLong()
        objectAnimator.start()
        objectAnimator.addListener(object : Animator.AnimatorListener{
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                objectAnimator.cancel()
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }

        })
    }

}