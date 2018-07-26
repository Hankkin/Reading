package com.hankkin.reading.utils;

import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;

import io.reactivex.Observable;

/**
 * Created by hankkin on 2017/10/11.
 * Blog: http://hankkin.cn
 * Mail: 1019283569@qq.com
 */

public class RxBus {
    private static volatile RxBus instance;
    private Relay<Object> mBus;

    public RxBus() {
        this.mBus = PublishRelay.create().toSerialized();
    }

    public static RxBus getDefault() {
        if (instance == null) {
            synchronized (RxBus.class) {
                if (instance == null) {
                    instance = new RxBus();
                }
            }
        }
        return instance;
    }
    public void post(Object obj) {
        mBus.accept(obj);
    }

    public <T> Observable<T> toObservable(Class<T> tClass) {
        return  mBus.ofType(tClass);
    }

    public Observable<Object> toObservable() {
        return mBus;
    }

    public boolean hasObservers() {
        return mBus.hasObservers();
    }

}
