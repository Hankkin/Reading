package com.hankkin.library.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.wifi.WifiManager
import android.telephony.TelephonyManager
import android.text.TextUtils
import java.util.*

/**
 * Created by huanghaijie on 2018/5/16.
 */
object DeviceInfoUtils {

    private var mDeviceImei: String = ""
    private  lateinit var mContext: Context

    fun init(context: Context) {
        mContext = context
    }

    /**
     * @return 获取imei, 如果获取不到，则生成一个UUID
     */
    @SuppressLint("MissingPermission", "HardwareIds")
    fun getImei(): String {

        if (mContext == null) throw IllegalStateException("DeviceInfoUtils not init")

        if (!TextUtils.isEmpty(mDeviceImei)) {
            return mDeviceImei
        }

        val tm = mContext!!.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        var imei = tm.deviceId

        if (TextUtils.isEmpty(imei) || "0" == imei) {
            // 如果imei号为空或0，取mac地址作为imei号传递
            imei = try {
                val wifi = mContext!!.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                val info = wifi.connectionInfo
                info.macAddress
            } catch (e: Exception) {
                ""
            }

            // 如果mac地址为空或0，则通过uuid生成的imei号来传递
            if (TextUtils.isEmpty(imei) || "0" == imei) {
                imei = getUUID()
                if (TextUtils.isEmpty(imei)) {
                    imei = "0"
                }
            }
        }
        imei = imei.toLowerCase(Locale.US)
        mDeviceImei = imei
        return imei
    }

    private fun getUUID(): String {
        val uuid = UUID.randomUUID()
        var uuidstr = uuid.toString()
        try {
            uuidstr = (uuidstr.substring(0, 8) + uuidstr.substring(9, 13) + uuidstr.substring(14, 18)
                    + uuidstr.substring(19, 23) + uuidstr.substring(24))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return uuidstr
    }

}