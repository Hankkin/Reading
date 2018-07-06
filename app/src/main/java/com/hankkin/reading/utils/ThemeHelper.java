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

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.View;

import com.bilibili.magicasakura.utils.ThemeUtils;
import com.hankkin.reading.R;


/**
 * @author xyczero
 * @time 16/5/2
 */
public class ThemeHelper {
    private static final String CURRENT_THEME = "theme_current";

    public static final int COLOR_YIMA = 0x1;
    public static final int COLOR_KUAN = 0x2;
    public static final int COLOR_BILI = 0x3;
    public static final int COLOR_YIDI = 0x5;
    public static final int COLOR_SHUIYA = 0x6;
    public static final int COLOR_YITENG = 0x7;
    public static final int COLOR_JILAO = 0x8;
    public static final int COLOR_ZHIHU = 0x9;
    public static final int COLOR_GUTONG = 0x10;
    public static final int COLOR_DIDIAO = 0x11;
    public static final int COLOR_GAODUAN = 0x12;
    public static final int COLOR_APING = 0x13;

    public static SharedPreferences getSharePreference(Context context) {
        return context.getSharedPreferences("multiple_theme", Context.MODE_PRIVATE);
    }

    public static void setTheme(Context context, int themeId) {
        getSharePreference(context).edit()
                .putInt(CURRENT_THEME, themeId)
                .commit();
    }

    public static int getTheme(Context context) {
        return getSharePreference(context).getInt(CURRENT_THEME, COLOR_YIMA);
    }

    public static boolean isDefaultTheme(Context context) {
        return getTheme(context) == COLOR_YIMA;
    }

    public static String getName(Context context,int currentTheme) {
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
        }
        return context.getResources().getString(R.string.theme_yima);
    }

    public static int getCurrentColor(Context context){
        switch (ThemeHelper.getTheme(context)){
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
        }
        return R.color.yima;
    }
}
