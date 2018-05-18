package com.hankkin.reading.http.protocol

import com.hankkin.reading.http.HttpClient
import io.reactivex.Observable

/**
 * Created by huanghaijie on 2018/5/16.
 */
abstract class BaseHttpProtocol {
    /**
     *  创建一个被观察者(被订阅者)对象
     *  @param url
     *  @param method
     *  @param params
     */
    protected fun createObservable(url: String, method: String, params: Map<String, String>?): Observable<String> {
        return Observable.create<String> {
            try {
                val request = HttpClient.getRequest(url, method, params)
                val response = HttpClient.execute(request)
                val json = response.body()?.string()
                if (json.isNullOrBlank()) {
                    it.onError(Throwable("not http data"))
                } else {
                    it.onNext(json!!)
                    it.onComplete()
                }

            } catch (e: Exception) {
                it.onError(e)
            }
        }
    }

    protected fun createObservable(url: String, method: String): Observable<String> {
        return createObservable(url, method, null)
    }


    /**
     * 创建指定解析器的Observable
     * */
    protected fun <T> createObservable(url: String, method: String, params: Map<String, String>?, parser: (String) -> T): Observable<T> {
        return Observable.create {
            try {
                val request = HttpClient.getRequest(url, method, params)
                val response = HttpClient.execute(request)
                val json = response.body()?.string()
                if (response.code() in 200..300){
                    if (json.isNullOrBlank()) {
                        it.onError(Throwable("not http data"))
                    } else {
                        it.onNext(parser.invoke(json!!))
                        it.onComplete()
                    }
                }
                else{
                    it.onError(Throwable("http fail"))
                }

            } catch (e: Exception) {
                it.onError(e)
            }
        }
    }

    protected fun <T> createObservable(url: String, method: String, parser: (String) -> T): Observable<T> {
        return createObservable(url, method, null, parser)
    }

    protected fun cp(map: MutableMap<String, String>, k: String, v: String) {
        if (k.trim().isEmpty() || v.trim().isEmpty()) return
        map[k] = v
    }

}