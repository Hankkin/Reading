package com.hankkin.reading.ui.home.search

import com.hankkin.reading.domain.HotBean
import com.hankkin.reading.mvp.model.IBaseDaoContract

/**
 * Created by huanghaijie on 2018/7/25.
 */
interface SearchDaoContract : IBaseDaoContract {

    fun insertHot(hotBean: HotBean)

    fun query()

}