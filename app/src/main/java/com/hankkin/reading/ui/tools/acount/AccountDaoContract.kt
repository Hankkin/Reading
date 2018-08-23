package com.hankkin.reading.ui.tools.acount

import com.hankkin.reading.domain.AccountBean
import com.hankkin.reading.dao.BaseDaoContract

/**
 * @author Hankkin
 * @date 2018/8/14
 */
interface AccountDaoContract : BaseDaoContract {

    fun queryAllAccount(): MutableList<AccountBean>

    fun saveAccount(accountBean: AccountBean)

    fun queryAccountById(id: Long): AccountBean?

    fun deleteAccountById(id: Long)

    fun updateAccountById(accountBean: AccountBean)

    fun insertAccounts(data: MutableList<AccountBean>)

}