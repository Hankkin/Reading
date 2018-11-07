package com.hankkin.reading.ui.todo

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.hankkin.reading.R
import com.hankkin.reading.base.BaseFragment
import com.hankkin.reading.control.UserControl
import com.hankkin.reading.ui.login.LoginActivity
import com.kekstudio.dachshundtablayout.indicators.DachshundIndicator
import kotlinx.android.synthetic.main.fragment_todo.*

/**
 * @author Hankkin
 * @date 2018/8/26
 */
class ToDoFragment : BaseFragment() {


    override fun getLayoutId(): Int {
        return R.layout.fragment_todo
    }

    override fun initViews() {
        iv_todo_display.setOnClickListener {
            startActivity(Intent(context, if (UserControl.isLogin()) {
                AddToDoActivity::class.java
            } else { LoginActivity::class.java }))
        }
        val temp = mutableListOf<String>("Only One", "Work", "Study", "Life")
        val adapter = PageAdapter(childFragmentManager, temp)
        vp_todo.adapter = adapter
        tab_todo.setupWithViewPager(vp_todo)
        val indicator = DachshundIndicator(tab_todo)
        tab_todo.animatedIndicator = indicator
        vp_todo.offscreenPageLimit = temp.size
    }

    override fun initData() {

    }


    class PageAdapter(fm: FragmentManager, val data: MutableList<String>) : FragmentStatePagerAdapter(fm) {

        override fun getItem(i: Int): Fragment {
            val bundle = Bundle()
            val fg = ToDoListFragment()
            bundle.putInt("index", i)
            fg.arguments = bundle
            return fg
        }

        override fun getCount(): Int {
            return data.size
        }

        override fun getPageTitle(position: Int): CharSequence {
            return data[position]
        }

    }

}