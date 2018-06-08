package com.hankkin.reading.ui.user

import com.hankkin.reading.common.Constant
import com.hankkin.reading.http.HttpClient
import com.hankkin.reading.http.api.UserApi
import com.hankkin.reading.mvp.presenter.RxLifePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by huanghaijie on 2018/6/8.
 */
class AuthorizePresenter: RxLifePresenter<AuthorizeContract.IView>(),AuthorizeContract.IPresenter{


    override fun getToken(code: String) {
        val map = HashMap<String,Any>()
        map.put("client_id",Constant.OSChinaUrl.CLIENT_ID)
        map.put("client_secret",Constant.OSChinaUrl.SCRET)
        map.put("grant_type","authorization_code")
        map.put("redirect_uri",Constant.OSChinaUrl.REDIRECT_URL)
        map.put("code",code)
        map.put("dataType","json")
        HttpClient.getnorRetrofit().create(UserApi::class.java)
                .getToken(map)
                .flatMap {
                    val map = HashMap<String,Any>()
                    map.put("dataType","json")
                    map.put("access_token",it.access_token)
                    HttpClient.getnorRetrofit().create(UserApi::class.java).getUser(map)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeNx({
                    getMvpView().saveUserInfo(it)
                })
    }

}