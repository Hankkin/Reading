package com.hankkin.reading.event

import android.text.TextUtils
import com.hankkin.reading.domain.AppInfo
import com.hankkin.reading.domain.CategoryBean
import com.hankkin.reading.domain.PersonListBean
import com.hankkin.reading.domain.ToDoListBean

import java.util.HashMap

/**
 * Created by hankkin on 2017/10/18.
 * Blog: http://hankkin.cn
 * Mail: 1019283569@qq.com
 */

object EventMap {

    //错误类存储器
    var ERROR_MAPS = HashMap<String, String>()

    //事件总线基类
    open class BaseEvent {
        var code: String? = null        //错误码

        var message: String? = null     //错误信息
    }

    //本地存储各种错误信息
    init {
        ERROR_MAPS.put("-1", "上传失败")
        ERROR_MAPS.put("0", "连接超时，请检查网络后重试")
        ERROR_MAPS.put("1", "服务器内部错误,请重试")
        ERROR_MAPS.put("119", "客户端没有权限执行该项操作")
        ERROR_MAPS.put("127", "手机号无效，尚未发送验证码")
        ERROR_MAPS.put("206", "操作失败")
        ERROR_MAPS.put("210", "密码不正确，请重新输入")
        ERROR_MAPS.put("211", "用户不存在，请重新输入")
        ERROR_MAPS.put("213", "该手机号尚未注册")
        ERROR_MAPS.put("214", "该手机号已经被注册，请更换手机号重新注册")
        ERROR_MAPS.put("215", "该手机号尚未验证，无法修改密码")
        ERROR_MAPS.put("601", "发送短信验证码过快，请稍后重试")
    }

    /**
     * 根据错误码返回错误信息
     * @param code  错误码
     * @return
     */
    fun pickMessage(code: String): String? {
        if (TextUtils.isEmpty(code)) {
            return null
        }
        return if (ERROR_MAPS.containsKey(code)) {
            ERROR_MAPS[code]
        } else null
    }

    /**
     * 错误异常事件
     */
    class HExceptionEvent : BaseEvent {
        var isPickedMessage = false

        constructor(message: String) {
            this.message = message
            this.isPickedMessage = true
        }

        constructor(code: Int, message: String) {
            this.code = code.toString()
            this.isPickedMessage = ERROR_MAPS.containsKey(this.code!!)
            val pick = pickMessage(this.code!!)
            this.message = if (TextUtils.isEmpty(pick)) message else pick
        }
    }

    class ChangeThemeEvent : BaseEvent()
    class ChangeFabEvent : BaseEvent()
    class ToUpEvent : BaseEvent()
    class LoginEvent : BaseEvent()
    class LogOutEvent : BaseEvent()
    class HomeRefreshEvent : BaseEvent()
    class UpdateEveryEvent : BaseEvent()
    class ToDoRefreshEvent : BaseEvent()
    class CompleteToDoEvent(val bean: ToDoListBean) : BaseEvent()
    class DeleteToDoEvent(val id: Int) : BaseEvent()
    class UpdateAccountListEvent : BaseEvent()
    class WifiImgEvent : BaseEvent()
    class XrvScollToPosEvent(val index: Int) : BaseEvent()
    class SelectAppEvent(val bean: AppInfo) : BaseEvent()
    class SearchHistoryDeleteEvent(val position:Int) : BaseEvent()

    class LoginSetTabEvent() : BaseEvent(){
         var name: String = ""
         var pwd: String = ""
        var index: Int = 0
        constructor( index: Int,name: String, pwd: String) : this(){
            this.name = name
            this.pwd = pwd
            this.index = index
        }
        constructor( index: Int) : this(){
            this.index = index
        }
    }

    class CollectEvent(val flag: Int,val id: Int) : BaseEvent(){
        companion object {
            val COLLECT: Int = 0x1
            val UNCOLLECT: Int = 0x2
        }
    }
    class PersonClickEvent(val index: Int,bean: PersonListBean): BaseEvent()

    class CateEvent(val data: ArrayList<CategoryBean>): BaseEvent()

}
