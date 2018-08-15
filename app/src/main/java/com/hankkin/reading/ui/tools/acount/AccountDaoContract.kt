package com.hankkin.reading.ui.tools.acount

import com.hankkin.reading.domain.AccountBean
import com.hankkin.reading.mvp.model.BaseDaoContract

/**
 * @author Hankkin
 * @date 2018/8/14
 */
interface AccountDaoContract : BaseDaoContract{

    fun queryAllAccount(): MutableList<AccountBean>

    fun saveAccount(accountBean: AccountBean)

}