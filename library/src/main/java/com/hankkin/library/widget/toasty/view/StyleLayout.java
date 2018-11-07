package com.hankkin.library.widget.toasty.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.hankkin.library.widget.toasty.Toasty;
import com.hankkin.library.widget.toasty.ToastyUtils;


/**
 * created by bravin on 2018/7/17.
 */
public class StyleLayout extends RelativeLayout {
//    private int style = STYLE_RECTANGLE;
    private int radius;

//    public static final int STYLE_FILLET = 1;
//    public static final int STYLE_RECTANGLE = 2;

    private static float[] radiusArr = new float[8];
    private int tintColor;

    public StyleLayout(Context context) {
        super(context);
    }

    public StyleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StyleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int height = getMeasuredHeight();
        if (radius == Toasty.RADIUS_HALF_OF_HEIGHT) {
            int realRadius = height / 2;
            refreshRadius(realRadius);
            ToastyUtils.setGradientDrawable(this, radiusArr, tintColor);
        } else {
            if (radius < 0){
                throw new IllegalArgumentException("radius can not be" +
                        " < 0 but -1(BToast.RADIUS_HALF_OF_HEIGHT)");
            }
            refreshRadius(radius);
            ToastyUtils.setGradientDrawable(this, radiusArr, tintColor);
        }

    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setTintColor(int tintColor) {
        this.tintColor = tintColor;
    }

    private void refreshRadius(int radius) {
        radiusArr[0] = radius;
        radiusArr[1] = radius;
        radiusArr[2] = radius;
        radiusArr[3] = radius;
        radiusArr[4] = radius;
        radiusArr[5] = radius;
        radiusArr[6] = radius;
        radiusArr[7] = radius;
    }
}
