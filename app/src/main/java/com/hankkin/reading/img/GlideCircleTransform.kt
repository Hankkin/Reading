package com.wuba.guchejia.img

import android.content.Context
import android.content.res.Resources
import android.graphics.*

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation

/**
 * Created by zjh on 2016/1/16.
 */
class GlideCircleTransform : BitmapTransformation {

    private var mBorderPaint: Paint? = null
    private var mBorderWidth: Float = 0f

    constructor(context: Context) : super(context)

    constructor(context: Context, borderWidth: Int, borderColor: Int) : super(context) {
        mBorderWidth = Resources.getSystem().displayMetrics.density * borderWidth
        mBorderPaint = Paint()
        mBorderPaint!!.isDither = true
        mBorderPaint!!.isAntiAlias = true
        mBorderPaint!!.color = borderColor
        mBorderPaint!!.style = Paint.Style.STROKE
        mBorderPaint!!.strokeWidth = mBorderWidth
    }


    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap? =
            circleCrop(pool, toTransform)

    private fun circleCrop(pool: BitmapPool, source: Bitmap?): Bitmap? {

        if (source == null) return null

        val size = Math.min(source.width, source.height)
        val x = (source.width - size) / 2
        val y = (source.height - size) / 2

        val squared = Bitmap.createBitmap(source, x, y, size, size)

        var result: Bitmap? = pool.get(size, size, Bitmap.Config.ARGB_8888)
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        }

        val canvas = Canvas(result!!)
        val paint = Paint()
        paint.shader = BitmapShader(squared, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.isAntiAlias = true
        val r = size / 2f
        canvas.drawCircle(r, r, r, paint)

        //加入边框
        if (mBorderPaint != null) {
            val borderRadius = r - mBorderWidth / 2.0f
            canvas.drawCircle(r, r, borderRadius, mBorderPaint)
        }
        return result
    }

    override fun getId(): String = javaClass.name
}
