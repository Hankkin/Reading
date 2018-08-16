package com.hankkin.library.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson


/**
 *
 */
object SPUtils {
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var sp: SharedPreferences

    /**
     * SPUtils构造函数
     * <p>在Application中初始化</p>
     *
     * @param spName spName
     */
    fun init(context: Context,spName: String) {
        sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE)
        editor = sp.edit()
        editor.apply()
    }

    /**
     * SP中写入String类型value
     *
     * @param key   键
     * @param value 值
     */
    fun put(key: String?, value: String) {
        editor.putString(key, value).apply()
    }

    /**
     * SP中读取String
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值{@code null}
     */
    fun getString(key: String?): String {
        return getString(key, "")
    }


    /**
     * SP中读取String
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    fun getString(key: String?, defaultValue: String): String {
        return sp.getString(key, defaultValue)
    }

    /**
     * SP中写入int类型value
     *
     * @param key   键
     * @param value 值
     */
    fun put(key: String?, value: Int) {
        editor.putInt(key, value).apply()
    }


    /**
     * SP中读取int
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值-1
     */
    fun getInt(key: String?): Int {
        return getInt(key,0)
    }

    /**
     * SP中读取int
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    fun getInt(key: String?, defaultValue: Int): Int {
        return sp.getInt(key, defaultValue)
    }


    /**
     * SP中写入long类型value
     *
     * @param key   键
     * @param value 值
     */
    fun put(key: String?, value: Long) {
        editor.putLong(key, value).apply()
    }

    /**
     * SP中读取long
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值-1
     */
    fun getLong(key: String?): Long {
        return getLong(key, -1L)
    }

    /**
     * SP中读取long
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    fun getLong(key: String?, defaultValue: Long): Long {
        return sp.getLong(key, defaultValue)
    }


    /**
     * SP中写入float类型value
     *
     * @param key   键
     * @param value 值
     */
    fun put(key: String?, value: Float) {
        editor.putFloat(key, value).apply()
    }


    /**
     * SP中读取float
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值-1
     */
    fun getFloat(key: String?): Float {
        return getFloat(key, -1f)
    }


    /**
     * SP中读取float
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    fun getFloat(key: String?, defaultValue: Float): Float {
        return sp.getFloat(key, defaultValue)
    }


    /**
     * SP中写入boolean类型value
     *
     * @param key   键
     * @param value 值
     */
    fun put(key: String?, value: Boolean) {
        editor.putBoolean(key, value)
    }


    /**
     * SP中读取boolean
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值{@code false}
     */
    fun getBoolean(key: String?): Boolean {
        return getBoolean(key, false)
    }


    /**
     * SP中读取boolean
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    fun getBoolean(key: String?, defaultValue: Boolean): Boolean {
        return sp.getBoolean(key, defaultValue)
    }

    /**
     * SP中写入String集合类型value
     *
     * @param key    键
     * @param values 值
     */
    fun put(key: String?, value: Set<String>) {
        editor.putStringSet(key, value).apply()
    }


    /**
     * SP中读取StringSet
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值{@code null}
     */
    fun getStringSet(key: String?): Set<String> {
        return getStringSet(key, null)
    }


    /**
     * SP中读取StringSet
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    fun getStringSet(key: String?, defaultValue: Set<String>?): Set<String> {
        return sp.getStringSet(key, defaultValue)
    }

    /**
     * SP中获取所有键值对
     *
     * @return Map对象
     */
    fun getAll(): Map<String, *> {
        return sp.all
    }

    /**
     * SP中移除该key
     *
     * @param key 键
     */
    fun remove(key: String?) {
        editor.remove(key).apply()
    }


    /**
     * SP中是否存在该key
     *
     * @param key 键
     * @return {@code true}: 存在<br>{@code false}: 不存在
     */
    fun contains(key: String?): Boolean {
        return sp.contains(key)
    }


    /**
     * SP中清除所有数据
     */
    fun clear() {
        editor.clear().apply()
    }


    /**
     * desc:保存对象
     * @param obj 要保存的对象，只能保存实现了serializable的对象
     * modified:
     */
    fun saveObject(key: String?, obj: Any) {
        val str = Gson().toJson(obj, obj.javaClass)
        editor.putString(key, str)
        editor.commit()
    }


    fun getObject(key: String?, clazz: Class<*>): Any?
    {
        val str = sp.getString(key, "")
        if (str != null) {
            return Gson().fromJson(str, clazz)
        }
        return null
    }
}