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
     * 保存账号
     */
    override fun saveAccount(accountBean: AccountBean) {
        daoSession.accountBeanDao.insertOrReplace(accountBean)
    }

    /**
     * 查询所有账号
     */
    override fun queryAllAccount(): MutableList<AccountBean> =
            daoSession.accountBeanDao.queryBuilder().orderDesc(AccountBeanDao.Properties.CreateAt).list()




}