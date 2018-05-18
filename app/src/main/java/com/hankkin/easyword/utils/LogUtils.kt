package com.hankkin.easyword.utils

import android.util.Log
import com.hankkin.easyword.BuildConfig

/**
 * Created by huanghaijie on 2018/5/16.
 */
object LogUtils {

    /** 日志输出时的TAG  */
    private const val mTag = "EasyWord"

    /** 日志输出级别All */
    const val LEVEL_ALL = 10

    /** 日志输出级别NONE  */
    const val LEVEL_NONE = 0

    /** 日志输出级别V  */
    const val LEVEL_VERBOSE = 1

    /** 日志输出级别D  */
    const val LEVEL_DEBUG = 2

    /** 日志输出级别I  */
    const val LEVEL_INFO = 3

    /** 日志输出级别W  */
    const val LEVEL_WARN = 4

    /** 日志输出级别E  */
    const val LEVEL_ERROR = 5

    private val DEBUG_LEVEL = if (BuildConfig.DEBUG) LEVEL_ALL else LEVEL_NONE


    /** 以级别为v 的形式输出LOG  */
    fun v(msg: String?) {
        if (DEBUG_LEVEL >= LEVEL_VERBOSE) {
            Log.v(mTag, msg)
        }
    }

    /** 以级别为 d 的形式输出LOG  */
    fun d(msg: String?) {
        if (DEBUG_LEVEL >= LEVEL_DEBUG) {
            Log.d(mTag, msg)
        }
    }

    /** 以级别为 i 的形式输出LOG  */
    fun i(msg: String?) {
        if (DEBUG_LEVEL >= LEVEL_INFO) {
            Log.i(mTag, msg)
        }
    }

    /** 以级别为 w 的形式输出LOG  */
    fun w(msg: String?) {
        if (DEBUG_LEVEL >= LEVEL_WARN) {
            Log.w(mTag, msg)
        }
    }

    /** 以级别为 w 的形式输出Throwable  */
    fun w(tr: Throwable) {
        if (DEBUG_LEVEL >= LEVEL_WARN) {
            Log.w(mTag, "", tr)
        }

    }

    /** 以级别为 w 的形式输出LOG信息和Throwable  */
    fun w(msg: String?, tr: Throwable) {
        if (DEBUG_LEVEL >= LEVEL_WARN && null != msg) {
            Log.w(mTag, msg, tr)
        }
    }

    /** 以级别为 e 的形式输出LOG  */
    fun e(msg: String?) {
        if (DEBUG_LEVEL >= LEVEL_ERROR) {
            Log.e(mTag, msg)
        }
    }

    /** 以级别为 e 的形式输出Throwable  */
    fun e(tr: Throwable) {
        if (DEBUG_LEVEL >= LEVEL_ERROR) {
            Log.e(mTag, "", tr)
        }
    }

    /** 以级别为 e 的形式输出LOG信息和Throwable  */
    fun e(msg: String?, tr: Throwable) {
        if (DEBUG_LEVEL >= LEVEL_ERROR) {
            Log.e(mTag, msg, tr)
        }
    }
}