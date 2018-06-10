package com.hankkin.reading.ui.person

import com.hankkin.reading.R
import com.hankkin.reading.base.BaseMvpActivity

class PersonInfoActivity : BaseMvpActivity<PersonInfoPresenter>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_person_info
    }

    override fun registerPresenter(): Class<out PersonInfoPresenter> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
