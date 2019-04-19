/*
 * Copyright (C) 2016 Bilibili
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hankkin.reading.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.hankkin.reading.R;


/**
 * @author xyczero
 * @time 16/5/2
 */
public class ThemeHelper {
    private static final String CURRENT_THEME = "theme_current";

    public static final int COLOR_YIMA = 0x1;
    public static final int COLOR_BILI = 0x2;
    public static final int COLOR_YITENG = 0x3;
    public static final int COLOR_XINGHONG = 0x4;
    public static final int COLOR_ANLUOLAN = 0x5;
    public static final int COLOR_KUAN = 0x6;
    public static final int COLOR_JILAO = 0x7;
    public static final int COLOR_YIDI = 0x8;
    public static final int COLOR_ZHIHU = 0x9;
    public static final int COLOR_SHUIYA = 0x10;
    public static final int COLOR_DIDIAO = 0x11;
    public static final int COLOR_GUTONG = 0x12;
    public static final int COLOR_APING = 0x13;
    public static final int COLOR_GAODUAN = 0x14;
    public static final int COLOR_LIANGBAI = 0x15;

    public static String[] themeList = {"yima","kuan","bili","yidi","shuiya","yiteng","jilao","zhihu","gutong","didiao","gaoduan","aping","liangbai","anluolan","xinghong"}
    ;

    public static SharedPreferences getSharePreference(Context context) {
        return context.getSharedPreferences("multiple_theme", Context.MODE_PRIVATE);
    }

    public static void setTheme(Context context, int themeId) {
        getSharePreference(context).edit()
                .putInt(CURRENT_THEME, themeId)
                .commit();
    }

    public static int getTheme(Context context) {
        return getSharePreference(context).getInt(CURRENT_THEME, COLOR_ZHIHU);
    }

    public static boolean isDefaultTheme(Context context) {
        return getTheme(context) == COLOR_ZHIHU;
    }

    public static String getName(Context context, int currentTheme) {
        switch (currentTheme) {
            case COLOR_YIMA:
                return context.getResources().getString(R.string.theme_yima);
            case COLOR_KUAN:
                return context.getResources().getString(R.string.theme_kuan);
            case COLOR_BILI:
                return context.getResources().getString(R.string.theme_bili);
            case COLOR_YIDI:
                return context.getResources().getString(R.string.theme_yidi);
            case COLOR_SHUIYA:
                return context.getResources().getString(R.string.theme_shuiya);
            case COLOR_YITENG:
                return context.getResources().getString(R.string.theme_yiteng);
            case COLOR_JILAO:
                return context.getResources().getString(R.string.theme_jilao);
            case COLOR_ZHIHU:
                return context.getResources().getString(R.string.theme_zhihu);
            case COLOR_GUTONG:
                return context.getResources().getString(R.string.theme_gutong);
            case COLOR_DIDIAO:
                return context.getResources().getString(R.string.theme_didiao);
            case COLOR_GAODUAN:
                return context.getResources().getString(R.string.theme_gaoduan);
            case COLOR_APING:
                return context.getResources().getString(R.string.theme_aping);
            case COLOR_LIANGBAI:
                return context.getResources().getString(R.string.theme_liangbai);
            case COLOR_ANLUOLAN:
                return context.getResources().getString(R.string.theme_anluolan);
            case COLOR_XINGHONG:
                return context.getResources().getString(R.string.theme_xinghong);
        }
        return context.getResources().getString(R.string.theme_yima);
    }

    public static String getNameStr(Context context) {
        String str = "";
        switch (getTheme(context)) {
            case COLOR_YIMA:
                str =  "yima";
                break;
            case COLOR_KUAN:
                str = "kuan";
                break;
            case COLOR_BILI:
                str = "bili";
                break;
            case COLOR_YIDI:
                str = "yidi";
                break;
            case COLOR_SHUIYA:
                str = "shuiya";
                break;
            case COLOR_YITENG:
                str = "yiteng";
                break;
            case COLOR_JILAO:
                str = "jilao";
                break;
            case COLOR_ZHIHU:
                str = "zhihu";
                break;
            case COLOR_GUTONG:
                str = "gutong";
                break;
            case COLOR_DIDIAO:
                str = "didiao";
                break;
            case COLOR_GAODUAN:
                str = "gaoduan";
                break;
            case COLOR_APING:
                str = "aping";
                break;
            case COLOR_LIANGBAI:
                str = "liangbai";
                break;
            case COLOR_ANLUOLAN:
                str = "anluolan";
                break;
            case COLOR_XINGHONG:
                str = "xinghong";
                break;

        }
        return str;
    }

    public static int getCurrentColor(Context context) {
        switch (ThemeHelper.getTheme(context)) {
            case COLOR_YIMA:
                return R.color.yima;
            case COLOR_KUAN:
                return R.color.kuan;
            case COLOR_BILI:
                return R.color.bili;
            case COLOR_YIDI:
                return R.color.yidi;
            case COLOR_SHUIYA:
                return R.color.shuiya;
            case COLOR_YITENG:
                return R.color.yiteng;
            case COLOR_JILAO:
                return R.color.jilao;
            case COLOR_ZHIHU:
                return R.color.zhihu;
            case COLOR_GUTONG:
                return R.color.gutong;
            case COLOR_DIDIAO:
                return R.color.didiao;
            case COLOR_GAODUAN:
                return R.color.gaoduan;
            case COLOR_APING:
                return R.color.aping;
            case COLOR_LIANGBAI:
                return R.color.liangbai;
            case COLOR_ANLUOLAN:
                return R.color.anluolan;
            case COLOR_XINGHONG:
                return R.color.xinghong;
        }
        return R.color.zhihu;
    }

    public static int getCurrentLogo(Context context) {
        switch (ThemeHelper.getTheme(context)) {
            case COLOR_YIMA:
                return R.mipmap.yima;
            case COLOR_KUAN:
                return R.mipmap.kuan;
            case COLOR_BILI:
                return R.mipmap.bili;
            case COLOR_YIDI:
                return R.mipmap.yidi;
            case COLOR_SHUIYA:
                return R.mipmap.shuiya;
            case COLOR_YITENG:
                return R.mipmap.yiteng;
            case COLOR_JILAO:
                return R.mipmap.jilao;
            case COLOR_ZHIHU:
                return R.mipmap.zhihu;
            case COLOR_GUTONG:
                return R.mipmap.gutong;
            case COLOR_DIDIAO:
                return R.mipmap.didiao;
            case COLOR_GAODUAN:
                return R.mipmap.gaoduan;
            case COLOR_APING:
                return R.mipmap.aping;
            case COLOR_LIANGBAI:
                return R.mipmap.liangbai;
            case COLOR_ANLUOLAN:
                return R.mipmap.anluolan;
            case COLOR_XINGHONG:
                return R.mipmap.xinghong;
        }
        return R.mipmap.zhihu;
    }

}
