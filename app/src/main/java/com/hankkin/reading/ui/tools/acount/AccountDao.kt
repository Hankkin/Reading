package com.hankkin.reading.ui.tools.acount

import com.hankkin.reading.domain.AccountBean
import com.hankkin.reading.greendao.AccountBeanDao
import com.hankkin.reading.mvp.model.BaseDao

/**
 * @author Hankkin
 * @date 2018/8/14
 */
class AccountDao : BaseDao(),AccountDaoContract{
    /**
     * 查询所有账号
     */
    override fun queryAllAccount(): MutableList<AccountBean> =
            daoSession.accountBeanDao.queryBuilder().orderAsc(AccountBeanDao.Properties.UpdateAt).list()


}