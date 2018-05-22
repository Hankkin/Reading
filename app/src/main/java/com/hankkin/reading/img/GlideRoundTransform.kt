package com.wuba.guchejia.img

import android.content.Context
import android.graphics.*

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation

/**
 * Created by L on 2016/8/22.
 */
class GlideRoundTransform @JvmOverloads constructor(context: Context, private val radius: Float = 10f)
    : BitmapTransformation(context) {

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap? =
            cornersCrop(pool, toTransform)

    private fun cornersCrop(pool: BitmapPool, source: Bitmap?): Bitmap? {
        if (source == null) return null

        val size = Math.min(source.width, source.height)
        val x = (source.width - size) / 2
        val y = (source.height - size) / 2

        val squared = Bitmap.createBitmap(source, x, y, size, size)

        var result: Bitmap? = pool.get(size, size, Bitmap.Config.ARGB_8888)
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        }

        val canvas = Canvas(result)
        val paint = Paint()
        paint.shader = BitmapShader(squared, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.isAntiAlias = true
        val rectF = RectF(0f, 0f, squared.width.toFloat(), squared.height.toFloat())
        canvas.drawRoundRect(rectF, radius, radius, paint)
        return result
    }

    override fun getId(): String = javaClass.name
}
