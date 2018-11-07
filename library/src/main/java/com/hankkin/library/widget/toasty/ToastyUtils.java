package com.hankkin.library.widget.toasty;

import android.graphics.drawable.GradientDrawable;
import android.view.View;

/**
 * created by bravin on 2018/7/16.
 */
public class ToastyUtils {

    private ToastyUtils() {

    }

     public static void setGradientDrawable(View layout,float[] radiusArr, int color){
        GradientDrawable gradientDrawable = new GradientDrawable();

        gradientDrawable.setColor(color);
        gradientDrawable.setCornerRadii(radiusArr);

        layout.setBackground(gradientDrawable);
    }

    public static boolean isTrimEmpty(CharSequence text) {
        return text == null || text.toString().trim().equals("");
    }
}
