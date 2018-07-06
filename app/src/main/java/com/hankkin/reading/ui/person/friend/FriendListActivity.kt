package com.hankkin.reading.ui.person.friend

import com.hankkin.library.utils.SPUtils
import com.hankkin.reading.R
import com.hankkin.reading.base.BaseMvpActivity
import com.hankkin.reading.control.UserControl
import com.hankkin.reading.domain.FriendBean
import kotlinx.android.synthetic.main.activity_friend_list.*

class FriendListActivity : BaseMvpActivity<FriendPresenter>(), FriendContract.IView {

    override fun registerPresenter() = FriendPresenter::class.java


    override fun getLayoutId(): Int {
        return R.layout.activity_friend_list
    }

    override fun initView() {
        refresh_friend.setColorSchemeResources(R.color.theme_color_primary)
    }

    override fun setFriendList(data: FriendBean) {
    }

    override fun refresh() {
        refresh_friend.isRefreshing = true
    }

    override fun refreshStop() {
        refresh_friend.isRefreshing = false
    }

    fun setMap(){
        val map =  HashMap<String,Any>()
        map.put("page",1)
        map.put("pageSize",20)
        map.put("relation",1)
        map.put("access_token",SPUtils.getString(UserControl.TOKEN))
        getPresenter().getFriendList(map)
    }

}
