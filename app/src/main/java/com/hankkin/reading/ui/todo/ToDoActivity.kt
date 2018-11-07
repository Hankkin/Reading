package com.hankkin.reading.ui.todo

import android.os.Bundle
import com.hankkin.reading.R
import com.hankkin.reading.base.BaseActivity

class ToDoActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_to_do
    }

    override fun initViews(savedInstanceState: Bundle?) {
        setStatusBarColor()
    }

    override fun initData() {
        supportFragmentManager.beginTransaction()
                .add(R.id.ll_todo,ToDoFragment())
                .commit()

    }


    override fun isSupportSwipeBack() = true

}
