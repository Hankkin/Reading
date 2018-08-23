package com.hankkin.reading.ui.tools.acount

import com.hankkin.library.utils.SPUtils
import com.hankkin.reading.common.Constant
import com.hankkin.reading.domain.AccountBean
import com.hankkin.reading.greendao.AccountBeanDao
import com.hankkin.reading.dao.BaseDao

/**
 * @author Hankkin
 * @date 2018/8/14
 */
class AccountDao : BaseDao(),AccountDaoContract{
    override fun insertAccounts(data: MutableList<AccountBean>) {
        updateSPTime()
        daoSession.accountBeanDao.insertOrReplaceInTx(data)
    }

    /**
     * 更新
     */
    override fun updateAccountById(accountBean: AccountBean) {
        updateSPTime()
        daoSession.accountBeanDao.update(accountBean)
    }

    /**
     * 删除账号
     */
    override fun deleteAccountById(id: Long) {
        daoSession.accountBeanDao.deleteByKey(id)
    }

    /**
     * 根据id查询账号
     */
    override fun queryAccountById(id: Long): AccountBean? =
            daoSession.accountBeanDao.queryBuilder().where(AccountBeanDao.Properties.Id.eq(id)).build().unique()

    /**
     * 保存账号
     */
    override fun saveAccount(accountBean: AccountBean) {
        updateSPTime()
        daoSession.accountBeanDao.insertOrReplace(accountBean)
    }

    /**
     * 查询所有账号
     */
    override fun queryAllAccount(): MutableList<AccountBean> =
            daoSession.accountBeanDao.queryBuilder().orderDesc(AccountBeanDao.Properties.CreateAt).list()


    private fun updateSPTime(){
        SPUtils.put(Constant.SP_KEY.DB_UPDATE_TIME,System.currentTimeMillis())
    }


}