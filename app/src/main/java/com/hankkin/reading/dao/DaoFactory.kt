package com.hankkin.reading.dao

object DaoFactory {

    private val daoFactoryHolder = DaoFactoryHolder()


    private fun <T> getNewProtocol(clazz: Class<T>): T = DaoFactoryUtils.getDao(clazz)


    private fun <T : BaseDaoContract> addNewProtocol(clazz: Class<T>): T{
        val protocol = getNewProtocol(clazz)
        daoFactoryHolder.add(clazz,protocol)
        return protocol
    }

    fun <T : BaseDaoContract> getProtocol(clazz: Class<T>): T =
            daoFactoryHolder.getProtocol(clazz) ?: addNewProtocol(clazz)

}