package com.hankkin.reading.ui.tools.scaner

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.SurfaceHolder
import android.view.View
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer
import com.hankkin.library.fuct.OnRxScanerListener
import com.hankkin.library.fuct.CameraManager
import com.hankkin.library.fuct.InactivityTimer
import com.hankkin.library.utils.RxAnimationTool
import com.hankkin.library.utils.RxBeepTool
import com.hankkin.library.utils.RxQrBarTool
import com.hankkin.library.utils.StatusBarUtil
import com.hankkin.reading.R
import com.hankkin.reading.base.BaseActivity
import kotlinx.android.synthetic.main.activity_scaner_code.*
import java.io.IOException
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicInteger

class ScanerCodeActivity : BaseActivity() {


    /**
     * 扫描结果监听
     */
    private var mScanerListener: OnRxScanerListener? = null

    private lateinit var inactivityTimer: InactivityTimer

    /**
     * 扫描处理
     */
    private  var handler: CaptureActivityHandler? = null


    /**
     * 扫描边界的宽度
     */
    private var mCropWidth = 0

    /**
     * 扫描边界的高度
     */
    private var mCropHeight = 0

    /**
     * 是否有预览
     */
    private var hasSurface: Boolean = false

    /**
     * 扫描成功后是否震动
     */
    private val vibrate = true

    /**
     * 闪光灯开启状态
     */
    private var mFlashing = true



    private lateinit var multiFormatReader: MultiFormatReader


    override fun getLayoutId(): Int {
        return R.layout.activity_scaner_code
    }

    override fun initData() {
        //界面控件初始化
        initDecode()
        initView()
        //权限初始化
        initPermission()
        //扫描动画初始化
        initScanerAnimation()
        //初始化 CameraManager
        CameraManager.init(this)
        hasSurface = false
        inactivityTimer = InactivityTimer(this)
    }

    override fun initViews(savedInstanceState: Bundle?) {
        StatusBarUtil.setTransparent(this)
    }

    private fun initDecode() {
        multiFormatReader = MultiFormatReader()

        // 解码的参数
        val hints = Hashtable<DecodeHintType, Any>(2)
        // 可以解析的编码类型
        var decodeFormats: Vector<BarcodeFormat>? = Vector<BarcodeFormat>()
        if (decodeFormats == null || decodeFormats.isEmpty()) {
            decodeFormats = Vector<BarcodeFormat>()

            val PRODUCT_FORMATS = Vector<BarcodeFormat>(5)
            PRODUCT_FORMATS.add(BarcodeFormat.UPC_A)
            PRODUCT_FORMATS.add(BarcodeFormat.UPC_E)
            PRODUCT_FORMATS.add(BarcodeFormat.EAN_13)
            PRODUCT_FORMATS.add(BarcodeFormat.EAN_8)
            // PRODUCT_FORMATS.add(BarcodeFormat.RSS14);
            val ONE_D_FORMATS = Vector<BarcodeFormat>(PRODUCT_FORMATS.size + 4)
            ONE_D_FORMATS.addAll(PRODUCT_FORMATS)
            ONE_D_FORMATS.add(BarcodeFormat.CODE_39)
            ONE_D_FORMATS.add(BarcodeFormat.CODE_93)
            ONE_D_FORMATS.add(BarcodeFormat.CODE_128)
            ONE_D_FORMATS.add(BarcodeFormat.ITF)
            val QR_CODE_FORMATS = Vector<BarcodeFormat>(1)
            QR_CODE_FORMATS.add(BarcodeFormat.QR_CODE)
            val DATA_MATRIX_FORMATS = Vector<BarcodeFormat>(1)
            DATA_MATRIX_FORMATS.add(BarcodeFormat.DATA_MATRIX)

            // 这里设置可扫描的类型，我这里选择了都支持
            decodeFormats.addAll(ONE_D_FORMATS)
            decodeFormats.addAll(QR_CODE_FORMATS)
            decodeFormats.addAll(DATA_MATRIX_FORMATS)
        }
        hints[DecodeHintType.POSSIBLE_FORMATS] = decodeFormats

        multiFormatReader.setHints(hints)
    }

    protected override fun onResume() {
        super.onResume()
        val surfaceHolder = capture_preview.getHolder()
        if (hasSurface) {
            //Camera初始化
            initCamera(surfaceHolder)
        } else {
            surfaceHolder.addCallback(object : SurfaceHolder.Callback {
                override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

                }

                override fun surfaceCreated(holder: SurfaceHolder) {
                    if (!hasSurface) {
                        hasSurface = true
                        initCamera(holder)
                    }
                }

                override fun surfaceDestroyed(holder: SurfaceHolder) {
                    hasSurface = false

                }
            })
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        }
    }

    protected override fun onPause() {
        super.onPause()
        if (handler != null) {
            handler!!.quitSynchronously()
            handler!!.removeCallbacksAndMessages(null)
            handler = null
        }
        CameraManager.get().closeDriver()
    }

    protected override fun onDestroy() {
        inactivityTimer.shutdown()
        mScanerListener = null
        super.onDestroy()
    }


    private fun initPermission() {
        //请求Camera权限 与 文件读写 权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        }
    }

    private fun initScanerAnimation() {
        RxAnimationTool.ScaleUpDowm(capture_scan_line)
    }

    fun getCropWidth(): Int {
        return mCropWidth
    }

    fun setCropWidth(cropWidth: Int) {
        mCropWidth = cropWidth
        CameraManager.FRAME_WIDTH = mCropWidth

    }

    fun getCropHeight(): Int {
        return mCropHeight
    }

    fun setCropHeight(cropHeight: Int) {
        this.mCropHeight = cropHeight
        CameraManager.FRAME_HEIGHT = mCropHeight
    }

    fun btn(view: View) {
        val viewId = view.id
        if (viewId == R.id.top_mask) {
            light()
        } else if (viewId == R.id.top_back) {
            finish()
        } else if (viewId == R.id.top_openpicture) {
            RxPhotoTool.openLocalImage(mContext)
        }
    }

    private fun light() {
        if (mFlashing) {
            mFlashing = false
            // 开闪光灯
            CameraManager.get().openLight()
        } else {
            mFlashing = true
            // 关闪光灯
            CameraManager.get().offLight()
        }

    }

    private fun initCamera(surfaceHolder: SurfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder)
            val point = CameraManager.get().cameraResolution
            val width = AtomicInteger(point.y)
            val height = AtomicInteger(point.x)
            val cropWidth = capture_crop_layout.getWidth() * width.get() / capture_containter.getWidth()
            val cropHeight = capture_crop_layout.getHeight() * height.get() / capture_containter.getHeight()
            setCropWidth(cropWidth)
            setCropHeight(cropHeight)
        } catch (ioe: IOException) {
            return
        } catch (ioe: RuntimeException) {
            return
        }

        if (handler == null) {
            handler = CaptureActivityHandler()
        }
    }


    //--------------------------------------打开本地图片识别二维码 start---------------------------------
    protected override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val resolver = contentResolver
            // 照片的原始资源地址
            val originalUri = data.data
            try {
                // 使用ContentProvider通过URI获取原始图片
                val photo = MediaStore.Images.Media.getBitmap(resolver, originalUri)

                // 开始对图像资源解码
                val rawResult = RxQrBarTool.decodeFromPhoto(photo)
                if (rawResult != null) {
                    if (mScanerListener == null) {
                        initDialogResult(rawResult!!)
                    } else {
                        mScanerListener!!.onSuccess("From to Picture", rawResult)
                    }
                } else {
                    if (mScanerListener == null) {
                        RxToast.error("图片识别失败.")
                    } else {
                        mScanerListener!!.onFail("From to Picture", "图片识别失败")
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }
    //========================================打开本地图片识别二维码 end=================================


    private fun initDialogResult(result: Result) {
        val type = result.getBarcodeFormat()
        val realContent = result.getText()

//        if (rxDialogSure == null) {
//            //提示弹窗
//            rxDialogSure = RxDialogSure(mContext)
//        }
//
//        if (BarcodeFormat.QR_CODE == type) {
//            rxDialogSure.setTitle("二维码扫描结果")
//        } else if (BarcodeFormat.EAN_13 == type) {
//            rxDialogSure.setTitle("条形码扫描结果")
//        } else {
//            rxDialogSure.setTitle("扫描结果")
//        }
//
//        rxDialogSure.setContent(realContent)
//        rxDialogSure.setSureListener(View.OnClickListener { rxDialogSure.cancel() })
//        rxDialogSure.setOnCancelListener(DialogInterface.OnCancelListener {
//            if (handler != null) {
//                // 连续扫描，不发送此消息扫描一次结束后就不能再次扫描
//                handler.sendEmptyMessage(R.id.restart_preview)
//            }
//        })
//
//        if (!rxDialogSure.isShowing()) {
//            rxDialogSure.show()
//        }
//
//        RxSPTool.putContent(mContext, RxConstants.SP_SCAN_CODE, RxDataTool.stringToInt(RxSPTool.getContent(mContext, RxConstants.SP_SCAN_CODE)) + 1 + "")
    }

    fun handleDecode(result: Result) {
        inactivityTimer.onActivity()
        //扫描成功之后的振动与声音提示
        RxBeepTool.playBeep(this, vibrate)

        val result1 = result.getText()
        Log.v("二维码/条形码 扫描结果", result1)
        if (mScanerListener == null) {
            RxToast.success(result1)
            initDialogResult(result)
        } else {
            mScanerListener!!.onSuccess("From to Camera", result)
        }
    }

    internal inner class DecodeHandler : Handler() {

        override fun handleMessage(message: Message) {
            if (message.what == R.id.decode) {
                decode(message.obj as ByteArray, message.arg1, message.arg2)
            } else if (message.what == R.id.quit) {
                Looper.myLooper()!!.quit()
            }
        }
    }

    internal inner class DecodeThread : Thread() {

        private val handlerInitLatch: CountDownLatch
        private var handler: Handler? = null

        init {
            handlerInitLatch = CountDownLatch(1)
        }

        fun getHandler(): Handler? {
            try {
                handlerInitLatch.await()
            } catch (ie: InterruptedException) {
                // continue?
            }

            return handler
        }

        override fun run() {
            Looper.prepare()
            handler = DecodeHandler()
            handlerInitLatch.countDown()
            Looper.loop()
        }
    }

    internal inner class CaptureActivityHandler : Handler() {

        var decodeThread: DecodeThread? = null
        private var state: State? = null

        init {
            decodeThread = DecodeThread()
            decodeThread!!.start()
            state = State.SUCCESS
            CameraManager.get().startPreview()
            restartPreviewAndDecode()
        }

        override fun handleMessage(message: Message) {
            if (message.what == R.id.auto_focus) {
                if (state == State.PREVIEW) {
                    CameraManager.get().requestAutoFocus(this, R.id.auto_focus)
                }
            } else if (message.what == R.id.restart_preview) {
                restartPreviewAndDecode()
            } else if (message.what == R.id.decode_succeeded) {
                state = State.SUCCESS
                handleDecode(message.obj as Result)// 解析成功，回调
            } else if (message.what == R.id.decode_failed) {
                state = State.PREVIEW
                CameraManager.get().requestPreviewFrame(decodeThread!!.getHandler(), R.id.decode)
            }
        }

        fun quitSynchronously() {
            state = State.DONE
            decodeThread!!.interrupt()
            CameraManager.get().stopPreview()
            removeMessages(R.id.decode_succeeded)
            removeMessages(R.id.decode_failed)
            removeMessages(R.id.decode)
            removeMessages(R.id.auto_focus)
        }

        private fun restartPreviewAndDecode() {
            if (state == State.SUCCESS) {
                state = State.PREVIEW
                CameraManager.get().requestPreviewFrame(decodeThread!!.getHandler(), R.id.decode)
                CameraManager.get().requestAutoFocus(this, R.id.auto_focus)
            }
        }
    }

    private fun decode(data: ByteArray, width: Int, height: Int) {
        var width = width
        var height = height
        val start = System.currentTimeMillis()
        var rawResult: Result? = null

        //modify here
        val rotatedData = ByteArray(data.size)
        for (y in 0 until height) {
            for (x in 0 until width) {
                rotatedData[x * height + height - y - 1] = data[x + y * width]
            }
        }
        // Here we are swapping, that's the difference to #11
        val tmp = width
        width = height
        height = tmp

        val source = CameraManager.get().buildLuminanceSource(rotatedData, width, height)
        val bitmap = BinaryBitmap(HybridBinarizer(source))
        try {
            rawResult = multiFormatReader.decodeWithState(bitmap)
        } catch (e: ReaderException) {
            // continue
        } finally {
            multiFormatReader.reset()
        }

        if (rawResult != null) {
            val end = System.currentTimeMillis()
            Log.d(javaClass.simpleName, "Found barcode (" + (end - start) + " ms):\n" + rawResult!!.toString())
            val message = Message.obtain(handler, R.id.decode_succeeded, rawResult)
            val bundle = Bundle()
            bundle.putParcelable("barcode_bitmap", source.renderCroppedGreyscaleBitmap())
            message.data = bundle
            //Log.d(TAG, "Sending decode succeeded message...");
            message.sendToTarget()
        } else {
            val message = Message.obtain(handler, R.id.decode_failed)
            message.sendToTarget()
        }
    }


    private enum class State {
        //预览
        PREVIEW,
        //成功
        SUCCESS,
        //完成
        DONE
    }

}
