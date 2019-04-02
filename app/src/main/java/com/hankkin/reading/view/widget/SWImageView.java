package com.hankkin.reading.view.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

import com.hankkin.library.utils.LogUtils;
import com.hankkin.reading.R;


/**
 * Created by Administrator on 2016/7/25.
 */
@SuppressLint("AppCompatCustomView")
public class SWImageView extends ImageView {

    //save bundle state
    private static final String STATE_INSTANCE = "state_instance";
    private static final String STATE_TYPE = "state_type";
    private static final String STATE_BORDER_RADIUS = "state_border_radius";
    private static final String STATE_BORDER_WIDTH = "state_border_width";
    private static final String STATE_BORDER_COLOR = "state_border_color";

    private static final int TYPE_NORMAL = -1;
    private static final int TYPE_CIRCLE = 0;
    private static final int TYPE_ROUND = 1;
    private static final int BODER_RADIUS_DEFAULT = 5;
    private static final int BORDER_WIDTH = 0;
    private static final int BORDER_COLOR = Color.BLACK;

    private int borderRadius;
    private int type;
    private int border_width;
    private int border_color;
    private Paint paint;
    private Paint boder_paint;
    private Matrix matrix;
    private int width;
    private int radius;
    private BitmapShader bitmapShader;
    private RectF rectF;

    public SWImageView(Context context) {
        super(context);
        inital();
    }

    public SWImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SWImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainStyledAttrs(context, attrs, defStyleAttr);
        inital();
    }


    private void obtainStyledAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray array = context.obtainStyledAttributes(attrs,
                R.styleable.SWImageView);
        borderRadius = dp2px(array.getDimension(R.styleable.SWImageView_borderRadius, BODER_RADIUS_DEFAULT));
        type = array.getInt(R.styleable.SWImageView_stype, TYPE_NORMAL);
        border_width = dp2px(array.getDimension(R.styleable.SWImageView_borderWidth, BORDER_WIDTH));
        border_color = array.getInt(R.styleable.SWImageView_borderColor, BORDER_COLOR);
        array.recycle();
    }

    private void inital() {
        matrix = new Matrix();
        paint = new Paint();
        boder_paint = new Paint();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (type == TYPE_CIRCLE) {
            width = Math.min(getMeasuredWidth(), getMeasuredHeight());
            radius = width / 2 - border_width / 2;
            setMeasuredDimension(width, width);
        }
    }

    private void setBitmapShader() {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }
        try {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            if (bitmap == null) {
                return;
            }
            bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            float scale = 1.0f;
            int viewwidth = getWidth();
            int viewheight = getHeight();
            int drawablewidth = bitmap.getWidth();
            int drawableheight = bitmap.getHeight();
            float dx = 0, dy = 0;

            float scale1 = 1.0f;
            float scale2 = 1.0f;
            final boolean fits = (drawablewidth < 0 || viewwidth == drawablewidth)
                    && (drawableheight < 0 || viewheight == drawableheight);
            if (type == TYPE_CIRCLE) {
                int size = Math.min(drawablewidth, drawableheight);
                scale = width * 1.0f / size;
            } else if (type == TYPE_ROUND) {
                scale = Math.max(viewwidth * 1.0f / drawablewidth, viewheight
                        * 1.0f / drawableheight);
            } else {
                return;
            }

            if (drawablewidth <= 0 || drawableheight <= 0) {
                drawable.setBounds(0, 0, viewwidth, viewheight);
                matrix = null;
            } else {
                drawable.setBounds(0, 0, drawablewidth, drawableheight);
                if (ScaleType.MATRIX == getScaleType()) {
                    if (matrix.isIdentity()) {
                        matrix = null;
                    }
                } else if (fits) {
                    matrix = null;
                } else if (ScaleType.CENTER == getScaleType()) {
                    matrix.setTranslate(Math.round((viewwidth - drawablewidth) * 0.5f),
                            Math.round((viewheight - drawableheight) * 0.5f));
                } else if (ScaleType.CENTER_CROP == getScaleType()) {
                    if (drawablewidth * viewheight > viewwidth * drawableheight) {
                        dx = (viewwidth - drawablewidth * scale) * 0.5f;
                    } else {
                        dy = (viewheight - drawableheight * scale) * 0.5f;
                    }
                    matrix.setScale(scale, scale);
                    matrix.postTranslate((int) (dx + 0.5f), (int) (dy + 0.5f));
                } else if (ScaleType.CENTER_INSIDE == getScaleType()) {

                    if (drawablewidth <= viewwidth && drawableheight <= viewheight) {
                        scale = 1.0f;
                    } else {
                        scale = Math.min((float) viewwidth / (float) drawablewidth,
                                (float) viewheight / (float) drawableheight);
                    }
                    dx = Math.round((viewwidth - drawablewidth * scale) * 0.5f);
                    dy = Math.round((viewheight - drawableheight * scale) * 0.5f);
                    matrix.setScale(scale, scale);
                    matrix.postTranslate(dx, dy);
                } else {
                    if (drawablewidth * viewheight > viewwidth * drawableheight) {
                        dx = (viewwidth - drawablewidth * scale) * 0.5f;
                    } else {
                        dy = (viewheight - drawableheight * scale) * 0.5f;
                    }
                    matrix.setScale(scale, scale);
                    matrix.postTranslate((int) (dx + 0.5f), (int) (dy + 0.5f));
                }
            }
            if (ScaleType.FIT_XY == getScaleType() && matrix != null) {
                scale1 = viewwidth * 1.0f / drawablewidth;
                scale2 = viewheight * 1.0f / drawableheight;
                matrix.setScale(scale1, scale2);
            }
            bitmapShader.setLocalMatrix(matrix);
            paint.setShader(bitmapShader);
        }catch (ClassCastException e){
            LogUtils.e(e.getMessage());
        }
    }


    protected void onDraw(Canvas canvas) {
        if (getDrawable() == null) {
            return;
        }
        paint.setAntiAlias(true);
        boder_paint.setAntiAlias(true);
        boder_paint.setStyle(Paint.Style.STROKE);
        boder_paint.setColor(border_color);
        boder_paint.setStrokeWidth(border_width);
        setBitmapShader();
        if (type == TYPE_ROUND) {
            canvas.drawRoundRect(rectF, borderRadius, borderRadius,
                    paint);
            if (border_width > 0) {
                canvas.drawRoundRect(rectF, borderRadius, borderRadius,
                        boder_paint);
            }
        } else if (type == TYPE_CIRCLE) {
            canvas.drawCircle(radius, radius, radius, paint);
            if (border_width > 0) {
                canvas.drawCircle(radius, radius, radius - border_width / 2, boder_paint);
            }
        } else {
            getDrawable().draw(canvas);
        }
    }

    public void setBorder_width(int border_width) {
        int px = dp2px(border_width);
        if (this.border_width != px) {
            this.border_width = px;
            invalidate();
        }
    }

    public void setBorder_color(int border_color) {
        if (this.border_color == border_color) {
            return;
        }
        this.border_color = border_color;
        boder_paint.setColor(border_color);
        invalidate();
    }

    public void setBorderRadius(int borderRadius) {
        int px = dp2px(borderRadius);
        if (this.borderRadius != px) {
            this.borderRadius = px;
            invalidate();
        }
    }

    public void setType(int type) {
        if (this.type != type) {
            this.type = type;
            if (this.type != TYPE_ROUND && this.type != TYPE_CIRCLE) {
                this.type = TYPE_NORMAL;
            }
            requestLayout();
        }
    }

    public int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, getResources().getDisplayMetrics());
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (type == TYPE_ROUND) {
            rectF = new RectF(border_width / 2, border_width / 2, getWidth() - border_width / 2, getHeight() - border_width / 2);
        }
    }

    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(STATE_INSTANCE, super.onSaveInstanceState());
        bundle.putInt(STATE_TYPE, type);
        bundle.putInt(STATE_BORDER_RADIUS, borderRadius);
        bundle.putInt(STATE_BORDER_WIDTH, border_width);
        bundle.putInt(STATE_BORDER_COLOR, border_color);
        return bundle;
    }

    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            super.onRestoreInstanceState(((Bundle) state)
                    .getParcelable(STATE_INSTANCE));
            this.type = bundle.getInt(STATE_TYPE);
            this.borderRadius = bundle.getInt(STATE_BORDER_RADIUS);
            this.border_width = bundle.getInt(STATE_BORDER_WIDTH);
            this.border_color = bundle.getInt(STATE_BORDER_COLOR);
        } else {
            super.onRestoreInstanceState(state);
        }
    }
}
