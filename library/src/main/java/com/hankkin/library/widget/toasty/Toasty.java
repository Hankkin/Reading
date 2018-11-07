package com.hankkin.library.widget.toasty;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.hankkin.library.R;
import com.hankkin.library.widget.toasty.view.AnimationLayout;
import com.hankkin.library.widget.toasty.view.StyleLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 * created by bravin on 2018/6/7.
 */
public class Toasty {

    private static Application app;

    private static Handler mainThreadHandler;

    private static final ArrayList<ToastDesc> toasts = new ArrayList<>();

    private static final int SHOW_TOAST = 1;
    private static final int FINISH_NO_TARGET_TOAST = 2;
    private static final int FINISH_WITH_TARGET_TOAST = 3;

    private static boolean canNotify = false;

    public static int DURATION_SHORT = 3000;
    public static int DURATION_LONG = 4500;

    private static int DEFAULT_DURATION = Toasty.DURATION_SHORT;

    private static int ANIMATION_DURATION = 400;

    @ColorInt
    private static int TEXT_COLOR = Color.parseColor("#FFFFFF");
    @ColorInt
    private static int ERROR_COLOR = Color.parseColor("#e51c23");
    @ColorInt
    private static int INFO_COLOR = Color.parseColor("#aa000000");
    @ColorInt
    private static int SUCCESS_COLOR = Color.parseColor("#259b24");
    @ColorInt
    private static int WARNING_COLOR = Color.parseColor("#f75acb");
    @ColorInt
    private static int NORMAL_COLOR = Color.parseColor("#bb000000");

    private static int TEXT_SIZE = 14;// sp

    private static boolean SHOW_ICON = true;
    private static boolean ANIMATE = false;
    private static boolean SAME_LENGTH = false;

    public static final int ANIMATION_GRAVITY_LEFT = 10;
    public static final int ANIMATION_GRAVITY_TOP = 20;
    public static final int ANIMATION_GRAVITY_RIGHT = 30;
    public static final int ANIMATION_GRAVITY_BOTTOM = 40;

    private static int ANIMATION_GRAVITY = ANIMATION_GRAVITY_TOP;

    public static final int LAYOUT_GRAVITY_LEFT = 1;
    public static final int LAYOUT_GRAVITY_TOP = 2;
    public static final int LAYOUT_GRAVITY_RIGHT = 3;
    public static final int LAYOUT_GRAVITY_BOTTOM = 4;

    private static int LAYOUT_GRAVITY = LAYOUT_GRAVITY_BOTTOM;

    public static final int RELATIVE_GRAVITY_START = 1;
    public static final int RELATIVE_GRAVITY_END = 2;
    public static final int RELATIVE_GRAVITY_CENTER = 3;

    private static int RELATIVE_GRAVITY = RELATIVE_GRAVITY_CENTER;

    public static final int RADIUS_HALF_OF_HEIGHT = -1;

    private static int RADIUS = RADIUS_HALF_OF_HEIGHT;

    private static WeakReference<View> currentToastView;

    private Toasty() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void init(final Application app) {
        Toasty.app = app;
        mainThreadHandler = new MainThreadHandler();
        app.registerActivityLifecycleCallbacks(new ActivityLifecycleImpl());
        runAgentThread();// 启动代理线程
    }

    private static void runAgentThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (; ; ) {
                    synchronized (toasts) {
                        if (toasts.size() > 0) {
                            ToastDesc toastDesc = toasts.remove(0);
                            Message message = Message.obtain();
                            message.what = SHOW_TOAST;
                            message.obj = toastDesc;
                            mainThreadHandler.sendMessage(message);
                            canNotify = false;
                        } else {
                            canNotify = true;
                        }

                        try {
                            toasts.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    private static class MainThreadHandler extends Handler {
        @SuppressLint("WrongConstant")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == SHOW_TOAST) {
                ToastDesc toastDesc = (ToastDesc) msg.obj;
                View target = toastDesc.getTarget();
                if (target == null) {
                    if (toastDesc.animate) {
                        showAnimationToast(toastDesc);
                    } else {
                        showStaticToast(toastDesc);
                    }
                } else {
                    View toastLayout = createToastLayout(target, toastDesc);
                    WindowManager.LayoutParams lp = createLayoutParams(target, toastLayout, toastDesc);

                    WindowManager windowManager = (WindowManager) target.getContext()
                            .getSystemService(Context.WINDOW_SERVICE);

                    windowManager.addView(toastLayout, lp);

                    currentToastView = new WeakReference<>(toastLayout);
                    long delay = toastDesc.duration == DURATION_SHORT ? DURATION_SHORT : DURATION_LONG;
                    mainThreadHandler.sendEmptyMessageDelayed(FINISH_WITH_TARGET_TOAST, delay);
                }
            } else if (msg.what == FINISH_NO_TARGET_TOAST) {
                synchronized (toasts) {
                    toasts.notifyAll();
                }
            } else {
                removeCurrentToastView();
                synchronized (toasts) {
                    toasts.notifyAll();
                }
            }
        }
    }

    private static void removeCurrentToastView() {
        if (currentToastView != null && currentToastView.get() != null) {
            View view = currentToastView.get();
            WindowManager windowManager =
                    (WindowManager) view.getContext().getSystemService(Context.WINDOW_SERVICE);
            if (windowManager != null) {
                windowManager.removeView(view);
            }
        }
        currentToastView = null;
    }


    private static View createToastLayout(View target, ToastDesc toastDesc) {
        if (toastDesc.animate) {
            if (toastDesc.sameLength) {
                AnimationLayout animationLayout = (AnimationLayout) (LayoutInflater.from(app)
                        .inflate(R.layout.toast_layout_animate_same_length, null));
                final StyleLayout styleLayout = animationLayout.findViewById(R.id.toast_content);
                final RelativeLayout wrapper = animationLayout.findViewById(R.id.rl_wrapper);
                setAnimationStyle(animationLayout, toastDesc);
                applyStyle(styleLayout, toastDesc);

                RelativeLayout.LayoutParams lp =
                        (RelativeLayout.LayoutParams) wrapper.getLayoutParams();

                RelativeLayout.LayoutParams styleLayoutLP =
                        (RelativeLayout.LayoutParams) styleLayout.getLayoutParams();
                int relativeGravity;
                if (toastDesc.layoutGravity == LAYOUT_GRAVITY_TOP
                        || toastDesc.layoutGravity == LAYOUT_GRAVITY_BOTTOM) {

                    styleLayoutLP.width = target.getMeasuredWidth() + toastDesc.offsetW;

                    if (toastDesc.relativeGravity == RELATIVE_GRAVITY_START) {
                        relativeGravity = RelativeLayout.ALIGN_PARENT_START;
                    } else if (toastDesc.relativeGravity == RELATIVE_GRAVITY_CENTER) {
                        relativeGravity = RelativeLayout.CENTER_HORIZONTAL;
                    } else {
                        relativeGravity = RelativeLayout.ALIGN_PARENT_END;
                    }
                } else {

                    styleLayoutLP.height = target.getMeasuredHeight() + toastDesc.offsetH;

                    if (toastDesc.relativeGravity == RELATIVE_GRAVITY_START) {
                        relativeGravity = RelativeLayout.ALIGN_PARENT_TOP;
                    } else if (toastDesc.relativeGravity == RELATIVE_GRAVITY_CENTER) {
                        relativeGravity = RelativeLayout.CENTER_VERTICAL;
                    } else {
                        relativeGravity = RelativeLayout.ALIGN_PARENT_BOTTOM;
                    }
                }
                lp.addRule(relativeGravity);
                wrapper.setLayoutParams(lp);

                styleLayout.setLayoutParams(styleLayoutLP);

                return animationLayout;

            } else {
                final AnimationLayout animationLayout = (AnimationLayout) LayoutInflater.from(app)
                        .inflate(R.layout.toast_layout_animate_not_same_length, null);
                final StyleLayout styleLayout = animationLayout.findViewById(R.id.toast_content);
                setAnimationStyle(animationLayout, toastDesc);
                applyStyle(styleLayout, toastDesc);

                if (toastDesc.relativeGravity == RELATIVE_GRAVITY_START) {
                    return animationLayout;
                }
                // 由sameLength代理 RELATIVE_GRAVITY_END RELATIVE_GRAVITY_CENTER
                final RelativeLayout wrapper = animationLayout.findViewById(R.id.rl_wrapper);

                RelativeLayout.LayoutParams wrapperLP = new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT
                        , ViewGroup.LayoutParams.WRAP_CONTENT);

                RelativeLayout.LayoutParams styleLP = new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT
                        , ViewGroup.LayoutParams.WRAP_CONTENT);

                int gravityRule;
                if (toastDesc.layoutGravity == LAYOUT_GRAVITY_TOP
                        || toastDesc.layoutGravity == LAYOUT_GRAVITY_BOTTOM) {
                    wrapperLP.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    if (toastDesc.relativeGravity == RELATIVE_GRAVITY_END) {
                        gravityRule = RelativeLayout.ALIGN_PARENT_END;
                    } else {
                        gravityRule = RelativeLayout.CENTER_HORIZONTAL;
                    }
                } else {
                    wrapperLP.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    if (toastDesc.relativeGravity == RELATIVE_GRAVITY_START) {
                        gravityRule = RelativeLayout.ALIGN_PARENT_TOP;
                    } else if (toastDesc.relativeGravity == RELATIVE_GRAVITY_CENTER){
                        gravityRule = RelativeLayout.CENTER_VERTICAL;
                    }else {
                        gravityRule = RelativeLayout.ALIGN_PARENT_BOTTOM;
                    }
                }
                styleLP.addRule(gravityRule);
                styleLayout.setLayoutParams(styleLP);
                wrapper.setLayoutParams(wrapperLP);

                return animationLayout;
            }
        } else {
            if (toastDesc.sameLength) {
                StyleLayout toastLayout = (StyleLayout) (LayoutInflater.from(app)
                        .inflate(R.layout.toast_layout_no_animation_style, null));
                // 内层的view无需关注style，由外层的ViewGroup代理
                toastLayout.setRadius(0);
                StyleLayout parent = new StyleLayout(target.getContext());
                RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT
                        , ViewGroup.LayoutParams.WRAP_CONTENT);
                int gravityRule;
                // top bottom
                if (toastDesc.layoutGravity == LAYOUT_GRAVITY_TOP
                        || toastDesc.layoutGravity == LAYOUT_GRAVITY_BOTTOM) {
                    if (toastDesc.relativeGravity == RELATIVE_GRAVITY_START) {
                        gravityRule = RelativeLayout.ALIGN_PARENT_START;
                    } else if (toastDesc.relativeGravity == RELATIVE_GRAVITY_END) {
                        gravityRule = RelativeLayout.ALIGN_PARENT_END;
                    } else {
                        gravityRule = RelativeLayout.CENTER_HORIZONTAL;
                    }
                } else {// left right
                    if (toastDesc.relativeGravity == RELATIVE_GRAVITY_START) {
                        gravityRule = RelativeLayout.ALIGN_PARENT_TOP;
                    } else if (toastDesc.relativeGravity == RELATIVE_GRAVITY_END) {
                        gravityRule = RelativeLayout.ALIGN_PARENT_BOTTOM;
                    } else {
                        gravityRule = RelativeLayout.CENTER_VERTICAL;
                    }
                }
                rlp.addRule(gravityRule);
                parent.addView(toastLayout, rlp);
                // ViewGroup代理style
                applyStyle(parent, toastDesc);
                return parent;
            } else {
                // no animation  not sameLength
                StyleLayout toastLayout = (StyleLayout) (LayoutInflater.from(app)
                        .inflate(R.layout.toast_layout_no_animation_style, null));
                applyStyle(toastLayout, toastDesc);
                if (toastDesc.relativeGravity == RELATIVE_GRAVITY_START) {
                    return toastLayout;
                }
                // 由sameLength代理 RELATIVE_GRAVITY_END RELATIVE_GRAVITY_CENTER
                RelativeLayout parent = new RelativeLayout(target.getContext());
                RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT
                        , ViewGroup.LayoutParams.WRAP_CONTENT);

                int gravityRule;
                if (toastDesc.layoutGravity == LAYOUT_GRAVITY_TOP
                        || toastDesc.layoutGravity == LAYOUT_GRAVITY_BOTTOM) {
                    if (toastDesc.relativeGravity == RELATIVE_GRAVITY_END) {
                        gravityRule = RelativeLayout.ALIGN_PARENT_END;
                    } else {
                        gravityRule = RelativeLayout.CENTER_HORIZONTAL;
                    }

                } else {
                    if (toastDesc.relativeGravity == RELATIVE_GRAVITY_START) {
                        gravityRule = RelativeLayout.ALIGN_PARENT_TOP;
                    } else if (toastDesc.relativeGravity == RELATIVE_GRAVITY_CENTER){
                        gravityRule = RelativeLayout.CENTER_VERTICAL;
                    }else {
                        gravityRule = RelativeLayout.ALIGN_PARENT_BOTTOM;
                    }
                }

                rlp.addRule(gravityRule);
                parent.addView(toastLayout, rlp);

                return parent;
            }
        }
    }

    private static WindowManager.LayoutParams createLayoutParams(
            View target, View content, ToastDesc toastDesc) {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

        lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;
        lp.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        lp.format = PixelFormat.TRANSPARENT;

        final int[] viewLocation = new int[2];
        target.getLocationInWindow(viewLocation);

        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.START | Gravity.TOP;
        if (toastDesc.sameLength) {
            switch (toastDesc.layoutGravity) {
                case LAYOUT_GRAVITY_LEFT:
                    int measureSpecL = View.MeasureSpec.makeMeasureSpec(
                            target.getMeasuredHeight() + toastDesc.offsetH,
                            View.MeasureSpec.EXACTLY);
                    // measure is necessary
                    content.measure(ViewGroup.LayoutParams.WRAP_CONTENT,
                            measureSpecL);
                    lp.x = viewLocation[0] - content.getMeasuredWidth() + toastDesc.offsetX;
                    lp.y = viewLocation[1] + toastDesc.offsetY;
                    lp.height = target.getMeasuredHeight() + toastDesc.offsetH;
                    break;
                case LAYOUT_GRAVITY_RIGHT:
                    // measure is necessary
                    int measureSpecR = View.MeasureSpec.makeMeasureSpec(
                            target.getMeasuredHeight() + toastDesc.offsetH,
                            View.MeasureSpec.EXACTLY);
                    content.measure(ViewGroup.LayoutParams.WRAP_CONTENT,
                            measureSpecR);
                    lp.x = viewLocation[0] + target.getMeasuredWidth() + toastDesc.offsetX;
                    lp.y = viewLocation[1] + toastDesc.offsetY;
                    lp.height = target.getMeasuredHeight() + toastDesc.offsetH;
                    break;
                case LAYOUT_GRAVITY_TOP:
                    // measure is necessary
                    int measureSpecT = View.MeasureSpec.makeMeasureSpec(target.getMeasuredWidth()
                            + toastDesc.offsetW, View.MeasureSpec.EXACTLY);
                    content.measure(measureSpecT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    lp.x = viewLocation[0] + toastDesc.offsetX;
                    lp.y = viewLocation[1] - content.getMeasuredHeight() + toastDesc.offsetY;
                    lp.width = target.getMeasuredWidth() + toastDesc.offsetW;
                    break;
                case LAYOUT_GRAVITY_BOTTOM:
                    // measure is necessary
                    int measureSpecB = View.MeasureSpec.makeMeasureSpec(target.getMeasuredWidth()
                            + toastDesc.offsetW, View.MeasureSpec.EXACTLY);
                    content.measure(measureSpecB,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    lp.x = viewLocation[0] + toastDesc.offsetX;
                    lp.y = viewLocation[1] + target.getMeasuredHeight() + toastDesc.offsetY;
                    lp.width = target.getMeasuredWidth() + toastDesc.offsetW;
                    break;
            }
        } else {
            switch (toastDesc.layoutGravity) {
                case LAYOUT_GRAVITY_LEFT:
                    int heightSpec = ViewGroup.LayoutParams.WRAP_CONTENT;
                    // 对于not sameLength且非RELATIVE_GRAVITY_START的情况，也要用精确值约束其高度
                    // 是因为如果简单的用wrap测量，可能高度只是一行字，但是relative center和end也是由
                    // sameLength代理的，所以可能真正展示时文字是多行的，而非测量时的一行，显示上会有误差
                    if (toastDesc.relativeGravity != RELATIVE_GRAVITY_START) {
                        heightSpec = View.MeasureSpec.makeMeasureSpec(target.getMeasuredHeight()
                                + toastDesc.offsetH, View.MeasureSpec.EXACTLY);
                    }
                    content.measure(ViewGroup.LayoutParams.WRAP_CONTENT, heightSpec);
                    lp.x = viewLocation[0] - content.getMeasuredWidth() + toastDesc.offsetX;
                    lp.y = viewLocation[1] + toastDesc.offsetY;
                    if (toastDesc.relativeGravity != RELATIVE_GRAVITY_START) {
                        lp.height = target.getMeasuredHeight() + toastDesc.offsetH;
                    }
                    break;
                case LAYOUT_GRAVITY_TOP:
                    int widthSpec = ViewGroup.LayoutParams.WRAP_CONTENT;
                    // 道理同上面的注释
                    if (toastDesc.relativeGravity != RELATIVE_GRAVITY_START) {
                        widthSpec = View.MeasureSpec.makeMeasureSpec(target.getMeasuredWidth()
                                + toastDesc.offsetW, View.MeasureSpec.EXACTLY);
                    }
                    content.measure(widthSpec, ViewGroup.LayoutParams.WRAP_CONTENT);
                    lp.x = viewLocation[0] + toastDesc.offsetX;
                    lp.y = viewLocation[1] - content.getMeasuredHeight() + toastDesc.offsetY;
                    if (toastDesc.relativeGravity != RELATIVE_GRAVITY_START) {
                        lp.width = target.getMeasuredWidth() + toastDesc.offsetW;
                    }
                    break;
                case LAYOUT_GRAVITY_RIGHT:
                    lp.x = viewLocation[0] + target.getMeasuredWidth() + toastDesc.offsetX;
                    lp.y = viewLocation[1] + toastDesc.offsetY;
                    if (toastDesc.relativeGravity != RELATIVE_GRAVITY_START) {
                        lp.height = target.getMeasuredHeight() + toastDesc.offsetH;
                    }
                    break;
                case LAYOUT_GRAVITY_BOTTOM:
                    lp.x = viewLocation[0] + toastDesc.offsetX;
                    lp.y = viewLocation[1] + target.getMeasuredHeight() + toastDesc.offsetY;
                    if (toastDesc.relativeGravity != RELATIVE_GRAVITY_START) {
                        lp.width = target.getMeasuredWidth() + toastDesc.offsetW;
                    }
                    break;
            }
        }

        return lp;
    }

    private static void showStaticToast(ToastDesc toastDesc) {
        StyleLayout toastLayout = (StyleLayout) (LayoutInflater.from(app)
                .inflate(R.layout.toast_layout_no_animation_style, null));

        applyStyle(toastLayout, toastDesc);

        showToast(toastLayout, toastDesc.duration);
    }

    private static void showToast(View toastView, int duration) {
        Toast toast = new Toast(app);
        toast.setView(toastView);
        toast.show();

        long delay = duration == DURATION_SHORT ? DURATION_SHORT : DURATION_LONG;
        mainThreadHandler.sendEmptyMessageDelayed(FINISH_NO_TARGET_TOAST, delay);
    }

    private static void setAnimationStyle(AnimationLayout animationLayout, ToastDesc toastDesc) {
        int gravity;
        switch (toastDesc.animationGravity) {
            case ANIMATION_GRAVITY_LEFT:
                gravity = AnimationLayout.GRAVITY_LEFT;
                break;
            case ANIMATION_GRAVITY_TOP:
                gravity = AnimationLayout.GRAVITY_TOP;
                break;
            case ANIMATION_GRAVITY_RIGHT:
                gravity = AnimationLayout.GRAVITY_RIGHT;
                break;
            case ANIMATION_GRAVITY_BOTTOM:
                gravity = AnimationLayout.GRAVITY_BOTTOM;
                break;
            default:
                throw new IllegalArgumentException("animateToast must set animate gravity!");
        }

        animationLayout.setAnimateGravity(gravity);
        animationLayout.setAnimDuration(toastDesc.animationDuration);
    }

    private static void showAnimationToast(ToastDesc toastDesc) {
        View toastLayout = LayoutInflater.from(app)
                .inflate(R.layout.toast_layout_animate, null);

        final AnimationLayout animationLayout = toastLayout.findViewById(R.id.al_layout);
        final StyleLayout styleLayout = toastLayout.findViewById(R.id.toast_content);

        setAnimationStyle(animationLayout, toastDesc);

        applyStyle(styleLayout, toastDesc);

        showToast(toastLayout, toastDesc.duration);
    }

    private static void applyStyle(StyleLayout styleLayout, ToastDesc toastDesc) {
        final ImageView toastIcon = styleLayout.findViewById(R.id.toast_icon);
        final TextView toastTextView = styleLayout.findViewById(R.id.toast_text);
        // icon
        if (!toastDesc.showIcon || toastDesc.iconId == 0) {
            toastIcon.setVisibility(View.GONE);
        } else {
            toastIcon.setImageResource(toastDesc.iconId);
        }
        // text
        if (!ToastyUtils.isTrimEmpty(toastDesc.text)) {
            toastTextView.setText(toastDesc.text);
        } else if (toastDesc.textRes != 0) {
            toastTextView.setText(toastDesc.textRes);
        } else {
            throw new IllegalArgumentException("BToast must has one of text or textRes!");
        }
        // textColor
        if (toastDesc.textColor != 0) {
            toastTextView.setTextColor(toastDesc.textColor);
        }
        // textSize
        toastTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, toastDesc.textSize);
        // style tintColor
        styleLayout.setRadius(toastDesc.radius);
        styleLayout.setTintColor(toastDesc.tintColor);
    }

    /**
     * 移除某一个Context中提交的可移除的toast
     *
     * @param context Context类名
     */
    private static void remove(Context context) {
        remove(context.getClass().getSimpleName(), true, 0);
    }

    /**
     * 移除某一个Context中提交的可移除的toast
     *
     * @param context Context 类名
     * @param tag     tag
     */
    private static void remove(Context context, int tag) {
        remove(context.getClass().getSimpleName(), false, tag);
    }

    private static void remove(String className, int tag) {
        remove(className, false, tag);
    }

    /**
     * 移除某一个Context中提交的可移除的toast
     *
     * @param name      Context类名
     * @param removeAll 是否移除对应类名中的所有toast
     * @param tag       tag
     */
    private static void remove(final String name, final boolean removeAll, final int tag) {
        synchronized (toasts) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                toasts.removeIf(new Predicate<ToastDesc>() {
                    @Override
                    public boolean test(ToastDesc toastDesc) {
                        return toastDesc.className.equals(name) &&
                                (removeAll || toastDesc.tag == tag);
                    }
                });
            } else {
                Set<ToastDesc> removeSet = new HashSet<>();
                for (ToastDesc entity : toasts) {
                    if (entity.className.equals(name) &&
                            (removeAll || entity.tag == tag)) {
                        removeSet.add(entity);
                    }
                }
                if (removeSet.size() > 0) {
                    toasts.removeAll(removeSet);
                }
            }
        }
    }

    private static void addToast(ToastDesc toastDesc) {
        synchronized (toasts) {
            remove(toastDesc.className, toastDesc.tag);
            toasts.add(toastDesc);
            if (canNotify)
                toasts.notifyAll();
        }
    }

    public static ToastDesc normal(Context context) {
        return new ToastDesc(context.getClass().getSimpleName(), NORMAL_COLOR);
    }

    public static ToastDesc warning(Context context) {
        return new ToastDesc(context.getClass().getSimpleName(),
                WARNING_COLOR, R.mipmap.ic_warning_outline_white);
    }

    public static ToastDesc info(Context context) {
        return new ToastDesc(context.getClass().getSimpleName(),
                INFO_COLOR, R.mipmap.ic_info_outline_white_48dp);
    }

    public static ToastDesc success(Context context) {
        return new ToastDesc(context.getClass().getSimpleName(),
                SUCCESS_COLOR, R.mipmap.ic_check_white_48dp);
    }

    public static ToastDesc error(Context context) {
        return new ToastDesc(context.getClass().getSimpleName(),
                ERROR_COLOR, R.mipmap.ic_error_outline_white_48dp);
    }

    public static ToastDesc custom(Context context) {
        return new ToastDesc(context.getClass().getSimpleName());
    }

    public static final class ToastDesc {
        private String className;

        private CharSequence text;

        private int textRes = 0;

        private int duration = Toasty.DEFAULT_DURATION;

        @ColorInt
        private int tintColor = 0;

        @DrawableRes
        private int iconId = 0;

        private boolean showIcon = Toasty.SHOW_ICON;

        private boolean animate = Toasty.ANIMATE;

        @ColorInt
        private int textColor = Toasty.TEXT_COLOR;

        private WeakReference<View> target = null;

        private int textSize = Toasty.TEXT_SIZE;

        private int animationGravity = Toasty.ANIMATION_GRAVITY;

        private int layoutGravity = Toasty.LAYOUT_GRAVITY;

        private int relativeGravity = Toasty.RELATIVE_GRAVITY;

        private int offsetX = 0;

        private int offsetY = 0;

        private int offsetW = 0;

        private int offsetH = 0;

        private boolean sameLength = Toasty.SAME_LENGTH;

        private long animationDuration = Toasty.ANIMATION_DURATION;

        private int radius = Toasty.RADIUS;

        private int tag = 0;

        ToastDesc(String className) {
            this.className = className;
        }

        ToastDesc(String className, int tintColor) {
            this.className = className;
            this.tintColor = tintColor;
        }

        ToastDesc(String className, int tintColor, int iconId) {
            this.className = className;
            this.tintColor = tintColor;
            this.iconId = iconId;
        }

        public ToastDesc duration(int duration) {
            if (duration != Toasty.DURATION_SHORT && duration != Toasty.DURATION_LONG) {
                throw new IllegalArgumentException("duration should be" +
                        " BToast.DURATION_SHORT or BToast.DURATION_LONG");
            }
            this.duration = duration;
            return this;
        }

        public ToastDesc text(int textRes) {
            this.textRes = textRes;
            return this;
        }

        public ToastDesc text(CharSequence text) {
            this.text = text;
            return this;
        }

        public ToastDesc tintColor(int tintColor) {
            this.tintColor = tintColor;
            return this;
        }

        public ToastDesc iconId(int iconId) {
            this.iconId = iconId;
            return this;
        }

        public ToastDesc showIcon(boolean showIcon) {
            this.showIcon = showIcon;
            return this;
        }

        public ToastDesc sameLength(boolean sameLength) {
            this.sameLength = sameLength;
            return this;
        }

        public ToastDesc animate(boolean animate) {
            this.animate = animate;
            return this;
        }

        public ToastDesc textColor(int textColor) {
            this.textColor = textColor;
            return this;
        }

        public ToastDesc textSize(int textSize) {
            this.textSize = textSize;
            return this;
        }

        public ToastDesc layoutGravity(int layoutGravity) {
            this.layoutGravity = layoutGravity;
            return this;
        }

        public ToastDesc relativeGravity(int relativeGravity) {
            this.relativeGravity = relativeGravity;
            return this;
        }


        public ToastDesc offsetX(int offsetX) {
            this.offsetX = offsetX;
            return this;
        }

        public ToastDesc offsetY(int offsetY) {
            this.offsetY = offsetY;
            return this;
        }

        public ToastDesc offsetW(int offsetW) {
            this.offsetW = offsetW;
            return this;
        }

        public ToastDesc offsetH(int offsetH) {
            this.offsetH = offsetH;
            return this;
        }

        public ToastDesc animationDuration(int animationDuration) {
            this.animationDuration = animationDuration;
            return this;
        }

        public ToastDesc animationGravity(int animationGravity) {
            this.animationGravity = animationGravity;
            return this;
        }

        public ToastDesc target(View view) {
            this.target = new WeakReference<>(view);
            return this;
        }

        public ToastDesc radius(int radius) {
            this.radius = radius;
            return this;
        }

        public ToastDesc tag(int tag) {
            this.tag = tag;
            return this;
        }

        public void show() {
            Toasty.addToast(this);
        }

        public View getTarget() {
            if (target == null) {
                return null;
            }
            return target.get();
        }
    }

    public static class Config {
        private int duration = 0;

        private int successColor = Toasty.SUCCESS_COLOR;

        private int errorColor = Toasty.ERROR_COLOR;

        private int infoColor = Toasty.INFO_COLOR;

        private int warningColor = Toasty.WARNING_COLOR;

        private int textColor = Toasty.TEXT_COLOR;

        private boolean showIcon = Toasty.SHOW_ICON;

        private boolean animate = Toasty.ANIMATE;

        private boolean sameLength = Toasty.SAME_LENGTH;

        private int textSize = Toasty.TEXT_SIZE;

        private int layoutGravity = Toasty.LAYOUT_GRAVITY;

        private int animationGravity = Toasty.ANIMATION_GRAVITY;

        private int relativeGravity = Toasty.RELATIVE_GRAVITY;

        private int animationDuration = Toasty.ANIMATION_DURATION;

        private int radius = Toasty.RADIUS;

        private int shortDurationMillis = 3000;

        private int longDurationMillis = 4500;

        private Config() {}

        public static Config getInstance() {
            return new Config();
        }

        public Config setDuration(int duration) {
            if (duration != Toasty.DURATION_SHORT && duration != Toasty.DURATION_LONG) {
                throw new IllegalArgumentException("duration should be" +
                        " BToast.DURATION_SHORT or BToast.DURATION_LONG");
            }
            if (duration == Toasty.DURATION_LONG) {
                this.duration = 1;
            }
            return this;
        }

        public Config setSuccessColor(int successColor) {
            this.successColor = successColor;
            return this;
        }

        public Config setErrorColor(int errorColor) {
            this.errorColor = errorColor;
            return this;
        }

        public Config setInfoColor(int infoColor) {
            this.infoColor = infoColor;
            return this;
        }

        public Config setWarningColor(int warningColor) {
            this.warningColor = warningColor;
            return this;
        }

        public Config setTextColor(int textColor) {
            this.textColor = textColor;
            return this;
        }

        public Config setShowIcon(boolean showIcon) {
            this.showIcon = showIcon;
            return this;
        }

        public Config setAnimate(boolean animate) {
            this.animate = animate;
            return this;
        }

        public Config setSameLength(boolean sameLength) {
            this.sameLength = sameLength;
            return this;
        }

        public Config setTextSize(int textSize) {
            this.textSize = textSize;
            return this;
        }

        public Config setLayoutGravity(int layoutGravity) {
            this.layoutGravity = layoutGravity;
            return this;
        }

        public Config setAnimationGravity(int animationGravity) {
            this.animationGravity = animationGravity;
            return this;
        }

        public Config setRelativeGravity(int relativeGravity) {
            this.relativeGravity = relativeGravity;
            return this;
        }

        public Config setAnimationDuration(int animationDuration) {
            this.animationDuration = animationDuration;
            return this;
        }

        public Config setRadius(int radius) {
            this.radius = radius;
            return this;
        }

        public Config setShortDurationMillis(int shortDurationMillis){
            this.shortDurationMillis = shortDurationMillis;
            return this;
        }

        public Config setLongDurationMillis(int longDurationMillis){
            this.longDurationMillis = longDurationMillis;
            return this;
        }

        public void apply(Application app) {
            Toasty.DURATION_SHORT = shortDurationMillis;
            Toasty.DURATION_LONG = longDurationMillis;
            if (this.duration == 0) {
                Toasty.DEFAULT_DURATION = Toasty.DURATION_SHORT;
            } else {
                Toasty.DEFAULT_DURATION = Toasty.DURATION_LONG;
            }

            Toasty.SUCCESS_COLOR = successColor;
            Toasty.ERROR_COLOR = errorColor;
            Toasty.INFO_COLOR = infoColor;
            Toasty.WARNING_COLOR = warningColor;
            Toasty.TEXT_COLOR = textColor;
            Toasty.SHOW_ICON = showIcon;
            Toasty.ANIMATE = animate;
            Toasty.SAME_LENGTH = sameLength;
            Toasty.TEXT_SIZE = textSize;
            Toasty.LAYOUT_GRAVITY = layoutGravity;
            Toasty.ANIMATION_GRAVITY = animationGravity;
            Toasty.RELATIVE_GRAVITY = relativeGravity;
            Toasty.ANIMATION_DURATION = animationDuration;
            Toasty.RADIUS = radius;

            Toasty.init(app);
        }
    }

    static class ActivityLifecycleImpl implements Application.ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {
            removeCurrentToastView();
        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            remove(activity);
        }
    }
}
