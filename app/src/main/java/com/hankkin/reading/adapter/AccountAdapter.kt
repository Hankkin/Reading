package com.hankkin.reading.adapter

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.hankkin.library.utils.DateUtils
import com.hankkin.reading.R
import com.hankkin.reading.adapter.base.BaseRecyclerViewAdapter
import com.hankkin.reading.adapter.base.BaseRecyclerViewHolder
import com.hankkin.reading.adapter.base.OnItemClickListener
import com.hankkin.reading.adapter.base.OnItemLongClickListener
import com.hankkin.reading.common.Constant
import com.hankkin.reading.domain.AccountBean

/**
 * @author Hankkin
 * @date 2018/8/14
 */
class AccountAdapter : BaseRecyclerViewAdapter<AccountBean>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder<AccountBean> {
        return ViewHolder(parent, R.layout.adapter_account_item, listener, onItemLongClickListener)
    }


    private class ViewHolder(parent: ViewGroup, layoutId: Int, val onItemClickListener: OnItemClickListener<AccountBean>, val onItemLongClickListener: OnItemLongClickListener<AccountBean>) : BaseRecyclerViewHolder<AccountBean>(parent, layoutId) {

        private val ivIcon by lazy { itemView.findViewById<ImageView>(R.id.iv_adapter_account_icon) }
        private val tvName by lazy { itemView.findViewById<TextView>(R.id.tv_adapter_account_name) }
        private val tvNumber by lazy { itemView.findViewById<TextView>(R.id.tv_adapter_account_number) }
        private val tvCate by lazy { itemView.findViewById<TextView>(R.id.tv_adapter_account_cate) }
        private val tvBZ by lazy { itemView.findViewById<TextView>(R.id.tv_adapter_account_bz) }
        private val tvTime by lazy { itemView.findViewById<TextView>(R.id.tv_adapter_account_time) }

        override fun onBindViewHolder(bean: AccountBean?, position: Int) {
            bean?.apply {
                tvName.text = name
                tvNumber.text = if (number.length > 3) number.substring(0, 3) + "..." else number + "..."
                tvCate.text = "【 $cate 】"
                tvBZ.text = if (beizhu.isEmpty()) "暂无备注..." else beizhu
                tvTime.text = DateUtils.format(createAt)
                when (cate) {
                    Constant.ACCOUNT_CATE.BANK -> ivIcon.setImageResource(R.mipmap.icon_account_bank)
                    Constant.ACCOUNT_CATE.SHOP -> ivIcon.setImageResource(R.mipmap.icon_account_shop)
                    Constant.ACCOUNT_CATE.SOCIAL -> ivIcon.setImageResource(R.mipmap.icon_account_social)
                    Constant.ACCOUNT_CATE.EMAIL -> ivIcon.setImageResource(R.mipmap.icon_account_email)
                    Constant.ACCOUNT_CATE.CODE -> ivIcon.setImageResource(R.mipmap.icon_account_code)
                    Constant.ACCOUNT_CATE.WORK -> ivIcon.setImageResource(R.mipmap.icon_account_work)
                    Constant.ACCOUNT_CATE.OTHER -> ivIcon.setImageResource(R.mipmap.icon_account_other)
                }
            }
            itemView.setOnClickListener { onItemClickListener.onClick(bean, position) }
            itemView.setOnLongClickListener {
                onItemLongClickListener.onLongClick(bean, position)
                false
            }
        }

    }

}