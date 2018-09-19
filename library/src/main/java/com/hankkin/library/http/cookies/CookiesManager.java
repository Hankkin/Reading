package com.hankkin.library.http.cookies;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * @author lw
 * @date 2018/1/25
 */

public class CookiesManager implements CookieJar {

    private Context mContext;

    public CookiesManager(Context context) {
        mContext = context;
        COOKIE_STORE = new PersistentCookieStore(context);
    }

    private static PersistentCookieStore COOKIE_STORE = null;

    @Override
    public void saveFromResponse(@NonNull HttpUrl url, @NonNull List<Cookie> cookies) {
        if (cookies.size() > 0) {
            for (Cookie item : cookies) {
                COOKIE_STORE.add(url, item);
            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(@NonNull HttpUrl url) {
        return COOKIE_STORE.get(url);
    }

    /**
     * 清除所有cookie
     */
    public static void clearAllCookies() {
        if (COOKIE_STORE != null)
            COOKIE_STORE.removeAll();
    }

    /**
     * 清除指定cookie
     *
     * @param url    HttpUrl
     * @param cookie Cookie
     * @return if clear cookies
     */
    public static boolean clearCookies(HttpUrl url, Cookie cookie) {
        return COOKIE_STORE.remove(url, cookie);
    }

    /**
     * 获取cookies
     *
     * @return List<Cookie>
     */
    public static List<Cookie> getCookies() {
        return COOKIE_STORE.getCookies();
    }

}
