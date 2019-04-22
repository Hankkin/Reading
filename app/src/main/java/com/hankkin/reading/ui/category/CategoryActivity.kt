package com.hankkin.reading.ui.category

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import com.google.gson.Gson
import com.hankkin.library.utils.RxBusTools
import com.hankkin.library.utils.SPUtils
import com.hankkin.library.widget.view.RecycleViewDivider
import com.hankkin.reading.R
import com.hankkin.reading.base.BaseActivity
import com.hankkin.reading.common.Constant
import com.hankkin.reading.domain.CategoryBean
import com.hankkin.reading.event.EventMap
import com.hankkin.reading.utils.ThemeHelper
import kotlinx.android.synthetic.main.activity_category.*
import kotlinx.android.synthetic.main.layout_title_bar_back.*
import org.json.JSONArray
import java.util.*


class CategoryActivity : BaseActivity() {

    lateinit var mAdapter: CateAdapter
    private var mData: ArrayList<CategoryBean>? = null

    companion object {
        fun intentTo(context: Context?, data: ArrayList<CategoryBean>) {
            val intent = Intent(context, CategoryActivity::class.java)
            val bundle = Bundle()
            bundle.putSerializable(Constant.CONSTANT_KEY.KEY_CATEGORY, data)
            intent.putExtras(bundle)
            context?.startActivity(intent)
        }
    }

    override fun getLayoutId() = R.layout.activity_category

    override fun initViews(savedInstanceState: Bundle?) {
        tv_normal_title.text = getString(R.string.category_sort)
        iv_back_icon.setOnClickListener { finish() }
        setStatusBarColor()
        fab_cate.setColorPressedResId(ThemeHelper.getCurrentColor(this))
        fab_cate.setColorNormalResId(ThemeHelper.getCurrentColor(this))
        fab_cate.setOnClickListener {
            RxBusTools.getDefault().post(EventMap.CateEvent(mAdapter.data))
            SPUtils.put(Constant.SP_KEY.CATEGORY_SORT,Gson().toJson(mAdapter.data))
            finish()
        }
    }

    override fun initData() {
        mData = intent.getSerializableExtra(Constant.CONSTANT_KEY.KEY_CATEGORY) as ArrayList<CategoryBean>?
        rv_category.layoutManager = LinearLayoutManager(this)
        val helper = ItemTouchHelper(dragTouchHelper)
        mAdapter = CateAdapter(mData as ArrayList<CategoryBean>, object : OnDragListener {
            override fun startDragItem(holder: RecyclerView.ViewHolder) {
                helper.startDrag(holder)
            }
        })
        rv_category.addItemDecoration(RecycleViewDivider(this,LinearLayoutManager.VERTICAL))
        rv_category.adapter = mAdapter
        helper.attachToRecyclerView(rv_category)
    }


    interface OnDragListener {
        fun startDragItem(holder: RecyclerView.ViewHolder)
    }

    class CateAdapter(val data: ArrayList<CategoryBean>, val onDragListener: OnDragListener?) : RecyclerView.Adapter<CateAdapter.Holder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            val item = LayoutInflater.from(parent.context).inflate(R.layout.adapter_category_item, parent, false)
            return Holder(item)
        }

        override fun getItemCount(): Int = data.size

        override fun onBindViewHolder(holder: Holder, postion: Int) {
            holder.tvTitle.text = data[postion].title
            holder.switch.isChecked = data[postion].isOpen
            holder.itemView.setOnTouchListener { _, event ->
                if (event?.action == MotionEvent.ACTION_DOWN) {
                    onDragListener?.startDragItem(holder)
                }
                false
            }
            holder.switch.setOnCheckedChangeListener { _, b ->
                data[postion].isOpen = b
            }
        }


        class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvTitle: TextView = itemView.findViewById(R.id.tv_adapter_category_title)
            val switch: Switch = itemView.findViewById(R.id.switch_category)
        }
    }


    private val dragTouchHelper = object : ItemTouchHelper.Callback() {
        override fun getMovementFlags(p0: RecyclerView, p1: RecyclerView.ViewHolder): Int {
            val swipeFlag = 0
            val dragFlag = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            return makeMovementFlags(dragFlag, swipeFlag)
        }

        override fun onMove(p0: RecyclerView, holder1: RecyclerView.ViewHolder, holder2: RecyclerView.ViewHolder): Boolean {
            mAdapter.notifyItemMoved(holder1.adapterPosition, holder2.adapterPosition)
            Collections.swap(mData, holder1.adapterPosition, holder2.adapterPosition)
            return true
        }

        override fun onSwiped(p0: RecyclerView.ViewHolder, p1: Int) {
        }

        override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                viewHolder?.itemView?.setBackgroundColor(resources.getColor(R.color.grey1))
            }
            super.onSelectedChanged(viewHolder, actionState)
        }

        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            viewHolder.itemView.setBackgroundColor(Color.WHITE)
            super.clearView(recyclerView, viewHolder)
        }

    }

}
