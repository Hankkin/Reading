package com.hankkin.library.widget.toasty.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

public class AnimationLayout extends RelativeLayout implements ValueAnimator.AnimatorUpdateListener {

    private ValueAnimator valueAnimator;

    private long animDuration = 800;

    private View child;

    private int preValue = 0;

    private boolean firstLayout = true;

    private int animateGravity = 0;

    public static final int GRAVITY_LEFT = 1;
    public static final int GRAVITY_TOP = 2;
    public static final int GRAVITY_RIGHT = 3;
    public static final int GRAVITY_BOTTOM = 4;

    public AnimationLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimationLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AnimationLayout(@NonNull Context context, @Nullable AttributeSet attrs,
                           int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public AnimationLayout(Context context) {
        super(context);
    }

    private void init() {
        valueAnimator = ValueAnimator.ofInt();
        valueAnimator.setDuration(animDuration);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (!firstLayout) {
            return;
        }
        firstLayout = false;
        int childHeight = getMeasuredHeight();
        int childWidth = getMeasuredWidth();
        child = getChildAt(0);
        if (child == null) {
            throw new IllegalStateException("AnimationLayout has no child!");
        }

        if (animateGravity == GRAVITY_TOP) {
            child.layout(left, -childHeight, right, 0);
            valueAnimator.setIntValues(0, childHeight);
        } else if (animateGravity == GRAVITY_BOTTOM) {
            child.layout(left, childHeight, right, childHeight * 2);
            valueAnimator.setIntValues(0, -childHeight);
        } else if (animateGravity == GRAVITY_LEFT) {
            child.layout(-childWidth, top, 0, bottom);
            valueAnimator.setIntValues(0, childWidth);
        } else {
            child.layout(childWidth, top, childWidth * 2, bottom);
            valueAnimator.setIntValues(0, -childWidth);
        }

        valueAnimator.addUpdateListener(this);
        valueAnimator.start();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        int value = (int) animation.getAnimatedValue();
        if (animateGravity == GRAVITY_TOP || animateGravity == GRAVITY_BOTTOM) {
            child.offsetTopAndBottom(value - preValue);
        } else {
            child.offsetLeftAndRight(value - preValue);
        }
        preValue = value;
    }

    public void setAnimDuration(long animDuration) {
        this.animDuration = animDuration;
    }

    public void setAnimateGravity(int animateGravity) {
        this.animateGravity = animateGravity;
    }
}
