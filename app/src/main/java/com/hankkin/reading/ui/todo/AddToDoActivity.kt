package com.hankkin.reading.ui.todo

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.afollestad.materialdialogs.MaterialDialog
import com.hankkin.library.utils.RxBusTools
import com.hankkin.library.utils.ToastUtils
import com.hankkin.reading.R
import com.hankkin.reading.base.BaseMvpActivity
import com.hankkin.reading.common.Constant
import com.hankkin.reading.domain.ToDoListBean
import com.hankkin.reading.event.EventMap
import com.hankkin.reading.utils.LoadingUtils
import com.hankkin.reading.utils.ThemeHelper
import com.hankkin.reading.utils.ViewHelper
import kotlinx.android.synthetic.main.activity_add_to_do.*
import kotlinx.android.synthetic.main.layout_white_title_bar_back.*
import java.util.*
import kotlin.collections.HashMap

class AddToDoActivity : BaseMvpActivity<AddToDoContract.IPresenter>(), AddToDoContract.IView {

    private var toDoListBean: ToDoListBean? = null
    private var mCate: Int = 0
    private val CATE by lazy {
        mutableListOf<String>("只用这一个",
                "工作",
                "学习",
                "生活")
    }

    override fun registerPresenter() = AddToDoPresenter::class.java


    companion object {
        fun intentTo(context: Context,toDoListBean: ToDoListBean?){
            val intent = Intent(context,AddToDoActivity::class.java)
            val bundle = Bundle()
            bundle.putSerializable(Constant.CONSTANT_KEY.KEY_TODO,toDoListBean)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_add_to_do
    }

    override fun initView() {
        setMiuiStatusBar()
        fab_add_todo.apply {
            setColorPressedResId(ThemeHelper.getCurrentColor(this@AddToDoActivity))
            setColorNormalResId(ThemeHelper.getCurrentColor(this@AddToDoActivity))
        }
        tv_normal_title_white.text = resources.getString(R.string.todo_add_title)
        iv_back_icon_white.setOnClickListener { finish() }
        val calendar = Calendar.getInstance()
        tv_add_todo_time.setOnClickListener {
            DatePickerDialog(this,
                    DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                        val monthTemp = if (month > 8) month + 1 else "0${month + 1}"
                        tv_add_todo_time.text = "${year}-${monthTemp}-${dayOfMonth}"
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
        tv_add_todo_cate.setOnClickListener {
            ViewHelper.showListTitleDialog(this, resources.getString(R.string.account_add_cate_hint), CATE,
                    MaterialDialog.ListCallback { dialog, itemView, which, text ->
                        mCate = which
                        tv_add_todo_cate.text = text
                        dialog.dismiss()
                    })
        }
        fab_add_todo.setOnClickListener { addTodo() }
    }


    override fun initData() {
        intent?.apply {
            toDoListBean = getSerializableExtra(Constant.CONSTANT_KEY.KEY_TODO) as ToDoListBean?
        }
        if (toDoListBean != null) {
            et_add_todo_content.setText(toDoListBean!!.content)
            et_add_todo_title.setText(toDoListBean!!.title)
            tv_add_todo_time.text = toDoListBean!!.dateStr
            mCate = toDoListBean!!.type
            tv_add_todo_cate.text = CATE[mCate]
        }
    }

    override fun addTodoSuccess() {
        setResult(resources.getString(R.string.todo_add_success))
    }

    override fun updateToDoSuccess() {
        setResult(resources.getString(R.string.todo_update_success))
    }

    override fun setFail() {
        LoadingUtils.hideLoading()
    }

    private fun setResult(msg: String) {
        RxBusTools.getDefault().post(EventMap.ToDoRefreshEvent())
        ToastUtils.showSuccess(this, msg)
        LoadingUtils.hideLoading()
        finish()
    }

    private fun addTodo() {
        if (et_add_todo_title.text.toString().trim().isEmpty()) {
            ToastUtils.showError(this, resources.getString(R.string.todo_add_title_hint))
            return
        }
        if (tv_add_todo_time.text.toString().isEmpty()) {
            ToastUtils.showError(this, resources.getString(R.string.todo_add_time_select_hint))
            return
        }
        val map = HashMap<String, Any>()
        map.apply {
            put("title", et_add_todo_title.text.toString().trim())
            put("content", et_add_todo_content.text.toString().trim())
            if (et_add_todo_content.text.toString().trim().isNotEmpty()) {
                put("content", et_add_todo_content.text.toString().trim())
            }
            put("date", tv_add_todo_time.text.toString())
            put("type", mCate)
        }
        LoadingUtils.showLoading(this)
        if (toDoListBean != null) {
            getPresenter().updateTodo(toDoListBean!!.id, map)
        } else {
            getPresenter().addTodo(map)
        }
    }

}
