package com.hankkin.reading.ui.main

import android.Manifest
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.widget.Toast
import com.hankkin.reading.R
import com.hankkin.reading.adapter.MainFragmentAdapter
import com.hankkin.reading.base.BaseActivity
import com.hankkin.reading.ui.dictionary.DictionaryFragment
import com.hankkin.reading.ui.person.PersonFragment
import com.hankkin.reading.ui.translate.TranslateFragment
import com.hankkin.reading.utils.LogUtils
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<MainContract.IPresenter>(), MainContract.IView {



    override fun createPresenter() = MainPresenter(this)

    companion object {
        private const val DEFAULT_FG_SIZE = 3
        private const val DICTIONARY_INDEX = 0
        private const val TRANSLATE_INDEX = 1
        private const val PERSON_INDEX = 2
    }


    private val fgList = listOf<Fragment>(
            DictionaryFragment(),
            TranslateFragment(),
            PersonFragment()
    )

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initData() {
        RxPermissions(this).requestEach(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_CALENDAR,
                Manifest.permission.READ_CALL_LOG,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_SMS,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.SEND_SMS,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe { p0 ->
                    if (p0.granted) {
                        LogUtils.d(p0.name+" is granted")
                    } else if (p0.shouldShowRequestPermissionRationale) {
                        Toast.makeText(activity, "请在设置-应用-权限管理中开启权限", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(activity, "权限被拒绝，无法启用存储功能", Toast.LENGTH_SHORT).show()
                    }
                }
    }

    override fun initViews(savedInstanceState: Bundle?) {
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        val mainAdapter = MainFragmentAdapter(supportFragmentManager, fgList)
        vp_home.adapter = mainAdapter
        vp_home.offscreenPageLimit = DEFAULT_FG_SIZE
    }


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_dic -> {
                vp_home.setCurrentItem(DICTIONARY_INDEX, false)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_tran -> {
                vp_home.setCurrentItem(TRANSLATE_INDEX, false)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_person -> {
                vp_home.setCurrentItem(PERSON_INDEX, false)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

}
