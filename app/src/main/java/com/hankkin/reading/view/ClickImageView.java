package com.hankkin.reading.view;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ClickImageView extends AppCompatImageView {

    public ClickImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ClickImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClickImageView(Context context) {
        super(context);
        init();
    }

    private void init() {
        setOnTouchListener(onTouchListener);
    }

    private OnTouchListener onTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    setColorFilter(null);
                    break;
                case MotionEvent.ACTION_DOWN:
                    changeLight();
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_CANCEL:
                    setColorFilter(null);
                    break;
                default:
                    break;
            }
            return false;
        }
    };

    private void changeLight() {
        int brightness = -80;
        ColorMatrix matrix = new ColorMatrix();
        matrix.set(new float[]{1, 0, 0, 0, brightness, 0, 1, 0, 0,
                brightness, 0, 0, 1, 0, brightness, 0, 0, 0, 1, 0});
        setColorFilter(new ColorMatrixColorFilter(matrix));
    }
}