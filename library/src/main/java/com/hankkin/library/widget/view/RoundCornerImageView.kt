package com.hankkin.library.widget.view

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.widget.ImageView
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.hankkin.library.R


/**
 * Created by Howshea
 * on 2018/10/8
 */
class RoundCornerImageView : AppCompatImageView {
    private var _borderWidth = 0f
    private var _borderColor = 0
    //radius of corner
    private var _radius = 0f
    private var innerRadius = 0f
    //aspect ratio
    private var _ratio = 0f
    private var imagePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var borderPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
    }
    private lateinit var rectF: RectF
    //去掉边框的内矩阵
    private lateinit var innerRectF: RectF
    private var _matrix: Matrix? = Matrix()

    var borderWidth: Float
        get() = _borderWidth
        set(value) {
            _borderWidth = value
            invalidate()
        }
    var borderColor: Int
        get() = _borderColor
        set(value) {
            _borderColor = value
            invalidate()
        }
    var radius: Float
        get() = _radius
        set(value) {
            _radius = value
            //计算边框内圆角半径
            innerRadius = _radius - _borderWidth / 2f
            invalidate()
        }
    var ratio: Float
        get() = _ratio
        set(value) {
            _ratio = value
            requestLayout()
        }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        val attributes = context?.obtainStyledAttributes(attrs, R.styleable.RoundCornerImageView)
        attributes?.apply {
            _borderWidth = getDimension(R.styleable.RoundCornerImageView_borderWidth1, 0f)
            _borderColor = getColor(R.styleable.RoundCornerImageView_borderColor1, Color.TRANSPARENT)
            _radius = getDimension(R.styleable.RoundCornerImageView_mradius, 0f)
            //计算边框内圆角半径
            innerRadius = _radius - _borderWidth / 2f
            _ratio = getFloat(R.styleable.RoundCornerImageView_ratio1, 0f)
        }
        attributes?.recycle()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (_ratio != 0f) {
            val width = MeasureSpec.getSize(widthMeasureSpec)
            val height = (width / _ratio).round()
            setMeasuredDimension(width, height)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    override fun onDraw(canvas: Canvas) {
        drawable ?: return
        if (_borderWidth == 0f && _radius == 0f) {
            super.onDraw(canvas)
        } else {
            borderPaint.color = _borderColor
            borderPaint.strokeWidth = _borderWidth
            imagePaint.shader = generateBitmapShader(drawable)
            canvas.drawRoundRect(innerRectF, innerRadius, innerRadius, imagePaint)
            if (_borderWidth > 0f) {
                canvas.drawRoundRect(rectF, _radius, _radius, borderPaint)
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        rectF = RectF(_borderWidth / 2f, _borderWidth / 2f, width - _borderWidth / 2f, height - _borderWidth / 2f)
        innerRectF = RectF(_borderWidth, _borderWidth, width - _borderWidth, height - _borderWidth)
    }

    private fun Float.round(): Int {
        return Math.round(this)
    }

    private fun Drawable.toBitmap(): Bitmap {
        //能省点内存是一点，createBitmap太吃内存了
        if (this is GifDrawable || this is ColorDrawable || this is TransitionDrawable) {
            // 取 drawable 的颜色格式
            val config = if (this.opacity != PixelFormat.OPAQUE)
                Bitmap.Config.ARGB_8888
            else
                Bitmap.Config.RGB_565
            val bitmap = if (this is ColorDrawable) {
                Bitmap.createBitmap(2, 2, config)
            } else {
                Bitmap.createBitmap((intrinsicWidth * 0.7).toInt(), (intrinsicHeight * 0.7).toInt(), config)
            }
            val canvas = Canvas(bitmap)
            this.setBounds(0, 0, canvas.width, canvas.height)
            // 把 drawable 内容画到画布中
            this.draw(canvas)
            return bitmap
        } else {
            return (this as BitmapDrawable).bitmap
        }

    }

    private fun generateBitmapShader(drawable: Drawable): BitmapShader {
        val bitmap = drawable.toBitmap()
        val bitmapShader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        val drawableWidth = bitmap.width
        val drawableHeight = bitmap.height
        val viewWidth = (width - borderWidth * 2f).round()
        val viewHeight = (height - borderWidth * 2f).round()
        var dx = 0f
        var dy = 0f
        val fits = (drawableWidth < 0 || viewWidth == drawableWidth) && (drawableHeight < 0 || viewHeight == drawableHeight)
        val scale = Math.max(viewWidth / drawableWidth.toFloat(), viewHeight / drawableHeight.toFloat())
        if (drawableWidth <= 0 || drawableHeight <= 0) {
            drawable.setBounds(0, 0, viewWidth, viewHeight)
            _matrix = null
        } else {
            drawable.setBounds(0, 0, drawableWidth, drawableHeight)
            when {
                fits -> _matrix = null
                ImageView.ScaleType.FIT_XY == scaleType -> {
                    val scaleX = viewWidth / drawableWidth.toFloat()
                    val scaleY = viewHeight / drawableHeight.toFloat()
                    _matrix?.setScale(scaleX, scaleY)
                    _matrix?.postTranslate(borderWidth, borderWidth)
                }
                else -> {
                    if (drawableWidth * viewHeight > viewWidth * drawableHeight) {
                        dx = (viewWidth - drawableWidth * scale) * 0.5f
                    } else {
                        dy = (viewHeight - drawableHeight * scale) * 0.5f
                    }
                    _matrix?.setScale(scale, scale)
                    _matrix?.postTranslate(borderWidth + dx, borderWidth + dy)
                }
            }
        }
        bitmapShader.setLocalMatrix(_matrix)
        return bitmapShader
    }
}