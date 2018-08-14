package com.hankkin.reading.adapter

import android.view.ViewGroup
import com.hankkin.reading.R
import com.hankkin.reading.adapter.base.BaseRecyclerViewAdapter
import com.hankkin.reading.adapter.base.BaseRecyclerViewHolder
import com.hankkin.reading.domain.AccountBean

/**
 * @author Hankkin
 * @date 2018/8/14
 */
class AccountAdapter : BaseRecyclerViewAdapter<AccountBean>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder<AccountBean> {
        return ViewHolder(parent, R.layout.adapter_account_item)
    }


    private class ViewHolder(parent: ViewGroup, layoutId: Int) : BaseRecyclerViewHolder<AccountBean>(parent,layoutId){
        override fun onBindViewHolder(bean: AccountBean, position: Int) {

        }

    }

}