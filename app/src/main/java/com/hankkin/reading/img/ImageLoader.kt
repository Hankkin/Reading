package com.wuba.guchejia.img

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.hankkin.reading.R
import com.hankkin.reading.common.Constant

object ImageLoader {

    enum class ImageForm {
        STANDARD, CIRCLE, ROUND
    }

    fun load(context: Context, url: String, img: ImageView,
             loadingImgResId: Int = R.color.grey,
             loadErrorImgResId: Int = loadingImgResId,
             form: ImageForm = ImageForm.STANDARD) {
        val imgUrl = handleUrl(url)
        val imageBuilder = Glide.with(context).load(imgUrl)
                .crossFade()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(loadingImgResId)
                .error(loadErrorImgResId)
        when (form) {
            ImageForm.CIRCLE -> imageBuilder.transform(GlideCircleTransform(context))
            ImageForm.ROUND -> imageBuilder.transform(GlideRoundTransform(context))
            ImageForm.STANDARD -> Unit
        }
        imageBuilder.into(img)
    }

    private fun handleUrl(url: String): String{
        val length = Constant.ConfigUrl.BASE_URL.length
        val baseUrl = Constant.ConfigUrl.BASE_URL.substring(0,length-1)
        return baseUrl+url
    }

}