package com.hankkin.library.utils

import io.reactivex.disposables.Disposable

/**
 * Created by huanghaijie on 2018/5/16.
 */
object RxUtils{
    fun dispose(disposable: Disposable?) {
        if (disposable != null && !disposable.isDisposed) {
            disposable.dispose()
        }
    }
}