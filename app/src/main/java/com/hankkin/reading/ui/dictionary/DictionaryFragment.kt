package com.hankkin.reading.ui.dictionary

import android.os.Bundle
import com.hankkin.reading.R
import com.hankkin.reading.base.BaseFragment

/**
 * Created by huanghaijie on 2018/5/15.
 */
class DictionaryFragment : BaseFragment<DictionaryContract.IPresenter>(), DictionaryContract.IView {

    override fun createmPresenter() = DictionaryPresenter(this)

    public fun newInstance(index: Int){
        val fragment = DictionaryFragment()
        val args = Bundle()
        args.putInt("index",index)
        fragment.arguments = args
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_dictionary
    }

    override fun initData() {
    }

    override fun initViews() {
    }

}