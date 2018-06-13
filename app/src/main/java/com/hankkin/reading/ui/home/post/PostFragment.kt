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
        vp_post.offscreenPageLimit = tags.size
        reflex(tab)
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


    fun reflex(tabLayout: TabLayout) {
        //了解源码得知 线的宽度是根据 tabView的宽度来设置的
        tabLayout.post {
            try {
                //拿到tabLayout的mTabStrip属性
                val mTabStrip = tabLayout.getChildAt(0) as LinearLayout

                val dp10 = DisplayUtil.dip2px(tabLayout.context, 25F)

                for (i in 0 until mTabStrip.childCount) {
                    val tabView = mTabStrip.getChildAt(i)

                    //拿到tabView的mTextView属性  tab的字数不固定一定用反射取mTextView
                    val mTextViewField = tabView::class.java.getDeclaredField("mTextView")
                    mTextViewField.setAccessible(true)

                    val mTextView = mTextViewField.get(tabView) as TextView

                    tabView.setPadding(0, 0, 0, 0)

                    //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                    var width = 0
                    width = mTextView.width
                    if (width == 0) {
                        mTextView.measure(0, 0)
                        width = mTextView.measuredWidth
                    }

                    //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                    val params = tabView.layoutParams as LinearLayout.LayoutParams
                    params.width = width
                    params.leftMargin = dp10
                    params.rightMargin = dp10
                    tabView.layoutParams = params

                    tabView.invalidate()
                }

            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
        }

    }


}