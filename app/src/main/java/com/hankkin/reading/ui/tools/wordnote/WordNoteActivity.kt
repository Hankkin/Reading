package com.hankkin.reading.ui.tools.wordnote

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import com.afollestad.materialdialogs.MaterialDialog
import com.hankkin.library.utils.RxBusTools
import com.hankkin.library.utils.ToastUtils
import com.hankkin.reading.R
import com.hankkin.reading.adapter.WordNoteAdapter
import com.hankkin.reading.base.BaseActivity
import com.hankkin.reading.dao.DaoFactory
import com.hankkin.reading.event.EventMap
import com.hankkin.reading.ui.tools.translate.TranslateActivity
import com.hankkin.reading.utils.SnackbarUtils
import com.hankkin.reading.utils.ThemeHelper
import com.hankkin.reading.utils.ViewHelper
import kotlinx.android.synthetic.main.activity_word_note.*
import kotlinx.android.synthetic.main.layout_title_bar_back.*

class WordNoteActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var mAdapter: WordNoteAdapter


    override fun getLayoutId(): Int {
        return R.layout.activity_word_note
    }


    @SuppressLint("StringFormatInvalid")
    override fun initViews(savedInstanceState: Bundle?) {
        setStatusBarColor()
        tv_normal_title.text = resources.getString(R.string.word_note_title)
        initPageLayout(rv_word_note)

        SnackbarUtils.Custom(ll, "长按可以进行更多操作奥", 3000)
                .backColor(resources.getColor(ThemeHelper.getCurrentColor(this)))
                .gravityFrameLayout(Gravity.BOTTOM).messageCenter().show()

        ViewHelper.setRefreshLayout(this, true, refresh_word_note, this)
        rv_word_note.layoutManager = LinearLayoutManager(this)
        mAdapter = WordNoteAdapter()
        mAdapter.setOnItemClickListener { t, position ->
            TranslateActivity.intentTo(this, t.translateBean.query)
        }
        rv_word_note.adapter = mAdapter
        val longClickItems = mutableListOf<String>(resources.getString(R.string.word_note_detail), resources.getString(R.string.word_note_remove), resources.getString(R.string.word_note_emphasis))
        mAdapter.setOnItemLongClickListener { t, position ->
            ViewHelper.showListTitleDialog(this,"操作", longClickItems, MaterialDialog.ListCallback { dialog, itemView, which, text ->
                when (which) {
                    0 -> TranslateActivity.intentTo(this, t.translateBean.query)
                    1 -> {
                        DaoFactory.getProtocol(WordNoteDaoContract::class.java).removeWordNote(t)
                        setAdapter()
                        ToastUtils.showInfo(this, resources.getString(R.string.word_note_remove_success) + t.translateBean.query)
                    }
                    2 -> {
                        if (!t.isEmphasis) {
                            t.isEmphasis = true
                            mAdapter.notifyItemChanged(position)
                            DaoFactory.getProtocol(WordNoteDaoContract::class.java).updateWord(t)
                            RxBusTools.getDefault().post(EventMap.UpdateEveryEvent())
                            ToastUtils.showInfo(this, resources.getString(R.string.word_note_add_emphasis) + t.translateBean.query)
                        }

                    }
                }
            })
        }

        iv_back_icon.setOnClickListener { finish() }
    }

    override fun initData() {
        setAdapter()
    }

    override fun onRefresh() {
        refresh_word_note.isRefreshing = true
        setAdapter()
    }

    private fun setAdapter() {
        mPageLayout?.showLoading()
        mAdapter.clear()
        mAdapter.addAll(DaoFactory.getProtocol(WordNoteDaoContract::class.java).queryWordNotes())
        mAdapter.notifyDataSetChanged()
        if (mAdapter.data.size == 0) {
            mPageLayout?.showEmpty()
        } else {
            mPageLayout?.hide()
        }
        refresh_word_note.isRefreshing = false
    }

}
