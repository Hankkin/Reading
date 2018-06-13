package com.hankkin.reading.ui.home.post

import android.os.Bundle
import android.support.design.widget.TabLayout.MODE_FIXED
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.hankkin.reading.R
import com.hankkin.reading.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_post.*
import android.widget.LinearLayout
import android.opengl.ETC1.getWidth
import android.widget.TextView
import java.lang.reflect.AccessibleObject.setAccessible
import android.support.design.widget.TabLayout
import com.hankkin.library.utils.DisplayUtil
import com.kekstudio.dachshundtablayout.indicators.PointMoveIndicator


/**
 * Created by huanghaijie on 2018/5/15.
 */
class PostFragment : BaseFragment() {

     companion object {
         val tags = listOf<String>(
                 "问答",
                 "分享",
                 "IT杂烩",
                 "站务",
                 "职业生涯"
         )
     }

    override fun initViews() {
        val adapter = MainFragmentAdapter(childFragmentManager)
        vp_post.adapter = adapter
        tab.setupWithViewPager(vp_post)
        tab.tabMode = MODE_FIXED
        val indicator =  PointMoveIndicator(tab)
        tab.animatedIndicator = indicator
        vp_post.offscreenPageLimit = tags.size
    }

    public fun newInstance(index: Int){
        val fragment = PostFragment()
        val args = Bundle()
        args.putInt("index",index)
        fragment.arguments = args
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_post
    }

    override fun initData() {

    }

    class MainFragmentAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        override fun getItem(i: Int): Fragment {
            return PostListFragment()
        }

        override fun getCount(): Int {
            return tags.size
        }

        override fun getPageTitle(position: Int): CharSequence {
            return tags[position]
        }

    }


}