package com.hankkin.reading.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class MainFragmentAdapter(fm: FragmentManager, private val mFragments: List<Fragment>) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return mFragments.size
    }

    override fun getItem(position: Int): Fragment {
        return mFragments[position]
    }


    override fun getPageTitle(position: Int): CharSequence {
        return ""
    }
}