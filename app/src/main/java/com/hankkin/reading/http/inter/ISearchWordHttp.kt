package com.hankkin.reading.http.inter

import com.hankkin.reading.domain.WordBean
import com.hankkin.reading.http.protocol.HttpInterface
import io.reactivex.Observable

/**
 * Created by huanghaijie on 2018/5/17.
 */
interface ISearchWordHttp : HttpInterface{

    fun searchWord(word: String) : Observable<WordBean>

    fun downRank(path: String,url: String) : Observable<String>

}