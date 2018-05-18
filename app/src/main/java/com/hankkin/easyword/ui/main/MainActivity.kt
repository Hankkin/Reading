package com.hankkin.easyword.ui.main

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import butterknife.BindView
import com.hankkin.easyword.R
import com.hankkin.easyword.adapter.MainFragmentAdapter
import com.hankkin.easyword.base.BaseActivity
import com.hankkin.easyword.ui.dictionary.DictionaryFragment
import com.hankkin.easyword.ui.person.PersonFragment
import com.hankkin.easyword.ui.translate.TranslateFragment
import com.hankkin.easyword.view.NoScrollViewPager

class MainActivity : BaseActivity<MainContract.IPresenter>(), MainContract.IView {

    override fun createPresenter() = MainPresenter(this)

    companion object {
        private const val DEFAULT_FG_SIZE = 3
        private const val DICTIONARY_INDEX = 0
        private const val TRANSLATE_INDEX = 1
        private const val PERSON_INDEX = 2
    }

    @BindView(R.id.vp_home) lateinit var vp: NoScrollViewPager
    @BindView(R.id.navigation) lateinit var navigation: BottomNavigationView

    private val fgList = listOf<Fragment>(
            DictionaryFragment(),
            TranslateFragment(),
            PersonFragment()
    )

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initData() {
    }

    override fun initViews(savedInstanceState: Bundle?) {
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        val mainAdapter = MainFragmentAdapter(supportFragmentManager, fgList)
        vp.adapter = mainAdapter
        vp.offscreenPageLimit = DEFAULT_FG_SIZE
    }


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_dic -> {
                vp.setCurrentItem(DICTIONARY_INDEX,false)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_tran -> {
                vp.setCurrentItem(TRANSLATE_INDEX,false)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_person -> {
                vp.setCurrentItem(PERSON_INDEX,false)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

}
