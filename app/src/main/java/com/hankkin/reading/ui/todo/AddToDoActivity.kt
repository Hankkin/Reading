package com.hankkin.reading.ui.todo

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import com.hankkin.reading.R
import com.hankkin.reading.base.BaseActivity
import kotlinx.android.synthetic.main.activity_add_to_do.*
import kotlinx.android.synthetic.main.layout_white_title_bar_back.*
import java.util.*

class AddToDoActivity : BaseActivity() {


    override fun getLayoutId(): Int {
        return R.layout.activity_add_to_do
    }

    @SuppressLint("SetTextI18n")
    override fun initViews(savedInstanceState: Bundle?) {
        setMiuiStatusBar()
        tv_normal_title_white.text = resources.getString(R.string.todo_add_title)
        iv_back_icon_white.setOnClickListener { finish() }
        val calendar = Calendar.getInstance()
        tv_add_todo_time.setOnClickListener {
            DatePickerDialog(this,
                    DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                        tv_add_todo_time.text = "${year}-${month + 1}-${dayOfMonth}"
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    override fun initData() {
    }


}
