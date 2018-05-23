package com.hankkin.reading.http.cookie;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AddCookiesInterceptor implements Interceptor {
    private Context context;
    private String lang;

    public AddCookiesInterceptor(Context context) {
        super();
        this.context = context;

    }

    @SuppressLint("CheckResult")
    @Override
    public Response intercept(Chain chain) throws IOException {

        final Request.Builder builder = chain.request().newBuilder();
        SharedPreferences sharedPreferences = context.getSharedPreferences("cookie", Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            Observable.just(sharedPreferences.getString("cookie", ""))
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String cookie) throws Exception {
                            if (cookie.contains("lang=ch")) {
                                cookie = cookie.replace("lang=ch", "");
                            }
                            if (cookie.contains("lang=en")) {
                                cookie = cookie.replace("lang=en", "");
                            }
                            //添加cookie
                            builder.addHeader("Cookie", cookie);
                        }
                    });
        }

        return chain.proceed(builder.build());
    }
}