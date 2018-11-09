package com.hankkin.library.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by Hankkin on 16/7/19.
 */
public class BitmapUtils {
    /**
     * drawable转bitmao
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else {
            Bitmap bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                    : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        }
    }


    /**
     * uri转bitmap
     *
     * @param uri
     * @param context
     * @return
     */
    public static Bitmap getBitmapFromUri(Uri uri, Context context) {
        try {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            Log.e("[Android]", e.getMessage());
            Log.e("[Android]", "目录为：" + uri);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * string转成bitmap
     *
     * @param st
     */
    public static Bitmap getBitmapFromString(String st) {
        // OutputStream out;
        Bitmap bitmap = null;
        try {
            // out = new FileOutputStream("/sdcard/aa.jpg");
            byte[] bitmapArray;
            bitmapArray = Base64.decode(st, Base64.DEFAULT);
            bitmap =
                    BitmapFactory.decodeByteArray(bitmapArray, 0,
                            bitmapArray.length);
            // bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }

    public static Bitmap getCompressedBitmap(Activity act, String filepath) {
        Bitmap tempBitmap = null;
        float width = act.getResources().getDisplayMetrics().widthPixels;
        float height = act.getResources().getDisplayMetrics().heightPixels;
        try {
            if (width > 640) {
                tempBitmap = getSuitableBitmap(act, Uri.fromFile(new File(filepath)), 640, (640 / width) * height);
            } else {
                tempBitmap = getSuitableBitmap(act, Uri.fromFile(new File(filepath)), (int) width, (int) height);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return tempBitmap;
    }

    /**
     * 说明：请调用getSuitableBitmap()方法并传入图像路径，返回Bitmap
     * <p/>
     * 修改宽高压缩比例
     * by:Hankkin at:2015-2-14
     */
    public static Bitmap getSuitableBitmap(Activity act, Uri uri, float ww, float hh)
        throws FileNotFoundException {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;//只读边,不读内容
        Bitmap bitmap = null;
        bitmap = BitmapFactory.decodeStream(act.getContentResolver().openInputStream(uri), null, newOpts);

        newOpts.inJustDecodeBounds = false;
        float w = newOpts.outWidth;
        float h = newOpts.outHeight;

        float wwh = 640f;//
        float hhh = (wwh / w) * h;//
        int be = 1;
        if (w > h && w > wwh) {
            be = (int) (newOpts.outWidth / wwh);
        } else if (w < h && h > hhh) {
            be = (int) (newOpts.outHeight / hhh);
            be += 1;
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置采样率

        newOpts.inPreferredConfig = Bitmap.Config.ARGB_8888;//该模式是默认的,可不设
        newOpts.inPurgeable = true;// 同时设置才会有效
        newOpts.inInputShareable = true;//。当系统内存不够时候图片自动被回收

        bitmap = BitmapFactory.decodeStream(act.getContentResolver().openInputStream(uri), null, newOpts);
        //      return compressBmpFromBmp(bitmap);//原来的方法调用了这个方法企图进行二次压缩
        return bitmap;
    }
}
