package com.chenjim.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * SharedPreferences工具类
 * 提供便捷的数据存储和读取功能
 *
 * @author ChenJim
 * @date 2024/01/01
 */
object PreferenceUtils {
    
    private const val DEFAULT_PREF_NAME = "app_preferences"
    private var defaultPreferences: SharedPreferences? = null
    
    /**
     * 初始化默认SharedPreferences
     *
     * @param context 上下文
     * @param prefName 偏好设置名称
     */
    @JvmStatic
    fun init(context: Context, prefName: String = DEFAULT_PREF_NAME) {
        defaultPreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
    }
    
    /**
     * 获取指定名称的SharedPreferences
     *
     * @param context 上下文
     * @param prefName 偏好设置名称
     * @return SharedPreferences实例
     */
    @JvmStatic
    fun getPreferences(context: Context, prefName: String): SharedPreferences {
        return context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
    }
    
    /**
     * 保存字符串
     *
     * @param key 键
     * @param value 值
     * @param preferences 可选的SharedPreferences实例
     */
    @JvmStatic
    fun putString(key: String, value: String?, preferences: SharedPreferences? = null) {
        val prefs = preferences ?: defaultPreferences ?: return
        prefs.edit().putString(key, value).apply()
    }
    
    /**
     * 获取字符串
     *
     * @param key 键
     * @param defaultValue 默认值
     * @param preferences 可选的SharedPreferences实例
     * @return 字符串值
     */
    @JvmStatic
    fun getString(key: String, defaultValue: String? = null, preferences: SharedPreferences? = null): String? {
        val prefs = preferences ?: defaultPreferences ?: return defaultValue
        return prefs.getString(key, defaultValue)
    }
    
    /**
     * 保存整数
     *
     * @param key 键
     * @param value 值
     * @param preferences 可选的SharedPreferences实例
     */
    @JvmStatic
    fun putInt(key: String, value: Int, preferences: SharedPreferences? = null) {
        val prefs = preferences ?: defaultPreferences ?: return
        prefs.edit().putInt(key, value).apply()
    }
    
    /**
     * 获取整数
     *
     * @param key 键
     * @param defaultValue 默认值
     * @param preferences 可选的SharedPreferences实例
     * @return 整数值
     */
    @JvmStatic
    fun getInt(key: String, defaultValue: Int = 0, preferences: SharedPreferences? = null): Int {
        val prefs = preferences ?: defaultPreferences ?: return defaultValue
        return prefs.getInt(key, defaultValue)
    }
    
    /**
     * 保存长整数
     *
     * @param key 键
     * @param value 值
     * @param preferences 可选的SharedPreferences实例
     */
    @JvmStatic
    fun putLong(key: String, value: Long, preferences: SharedPreferences? = null) {
        val prefs = preferences ?: defaultPreferences ?: return
        prefs.edit().putLong(key, value).apply()
    }
    
    /**
     * 获取长整数
     *
     * @param key 键
     * @param defaultValue 默认值
     * @param preferences 可选的SharedPreferences实例
     * @return 长整数值
     */
    @JvmStatic
    fun getLong(key: String, defaultValue: Long = 0L, preferences: SharedPreferences? = null): Long {
        val prefs = preferences ?: defaultPreferences ?: return defaultValue
        return prefs.getLong(key, defaultValue)
    }
    
    /**
     * 保存浮点数
     *
     * @param key 键
     * @param value 值
     * @param preferences 可选的SharedPreferences实例
     */
    @JvmStatic
    fun putFloat(key: String, value: Float, preferences: SharedPreferences? = null) {
        val prefs = preferences ?: defaultPreferences ?: return
        prefs.edit().putFloat(key, value).apply()
    }
    
    /**
     * 获取浮点数
     *
     * @param key 键
     * @param defaultValue 默认值
     * @param preferences 可选的SharedPreferences实例
     * @return 浮点数值
     */
    @JvmStatic
    fun getFloat(key: String, defaultValue: Float = 0f, preferences: SharedPreferences? = null): Float {
        val prefs = preferences ?: defaultPreferences ?: return defaultValue
        return prefs.getFloat(key, defaultValue)
    }
    
    /**
     * 保存布尔值
     *
     * @param key 键
     * @param value 值
     * @param preferences 可选的SharedPreferences实例
     */
    @JvmStatic
    fun putBoolean(key: String, value: Boolean, preferences: SharedPreferences? = null) {
        val prefs = preferences ?: defaultPreferences ?: return
        prefs.edit().putBoolean(key, value).apply()
    }
    
    /**
     * 获取布尔值
     *
     * @param key 键
     * @param defaultValue 默认值
     * @param preferences 可选的SharedPreferences实例
     * @return 布尔值
     */
    @JvmStatic
    fun getBoolean(key: String, defaultValue: Boolean = false, preferences: SharedPreferences? = null): Boolean {
        val prefs = preferences ?: defaultPreferences ?: return defaultValue
        return prefs.getBoolean(key, defaultValue)
    }
    
    /**
     * 保存字符串集合
     *
     * @param key 键
     * @param value 值
     * @param preferences 可选的SharedPreferences实例
     */
    @JvmStatic
    fun putStringSet(key: String, value: Set<String>?, preferences: SharedPreferences? = null) {
        val prefs = preferences ?: defaultPreferences ?: return
        prefs.edit().putStringSet(key, value).apply()
    }
    
    /**
     * 获取字符串集合
     *
     * @param key 键
     * @param defaultValue 默认值
     * @param preferences 可选的SharedPreferences实例
     * @return 字符串集合
     */
    @JvmStatic
    fun getStringSet(key: String, defaultValue: Set<String>? = null, preferences: SharedPreferences? = null): Set<String>? {
        val prefs = preferences ?: defaultPreferences ?: return defaultValue
        return prefs.getStringSet(key, defaultValue)
    }
    
    /**
     * 检查键是否存在
     *
     * @param key 键
     * @param preferences 可选的SharedPreferences实例
     * @return 是否存在
     */
    @JvmStatic
    fun contains(key: String, preferences: SharedPreferences? = null): Boolean {
        val prefs = preferences ?: defaultPreferences ?: return false
        return prefs.contains(key)
    }
    
    /**
     * 删除指定键
     *
     * @param key 键
     * @param preferences 可选的SharedPreferences实例
     */
    @JvmStatic
    fun remove(key: String, preferences: SharedPreferences? = null) {
        val prefs = preferences ?: defaultPreferences ?: return
        prefs.edit().remove(key).apply()
    }
    
    /**
     * 删除多个键
     *
     * @param keys 键列表
     * @param preferences 可选的SharedPreferences实例
     */
    @JvmStatic
    fun remove(keys: List<String>, preferences: SharedPreferences? = null) {
        val prefs = preferences ?: defaultPreferences ?: return
        val editor = prefs.edit()
        keys.forEach { editor.remove(it) }
        editor.apply()
    }
    
    /**
     * 清空所有数据
     *
     * @param preferences 可选的SharedPreferences实例
     */
    @JvmStatic
    fun clear(preferences: SharedPreferences? = null) {
        val prefs = preferences ?: defaultPreferences ?: return
        prefs.edit().clear().apply()
    }
    
    /**
     * 获取所有键
     *
     * @param preferences 可选的SharedPreferences实例
     * @return 键集合
     */
    @JvmStatic
    fun getAllKeys(preferences: SharedPreferences? = null): Set<String> {
        val prefs = preferences ?: defaultPreferences ?: return emptySet()
        return prefs.all.keys
    }
    
    /**
     * 获取所有数据
     *
     * @param preferences 可选的SharedPreferences实例
     * @return 所有数据的Map
     */
    @JvmStatic
    fun getAll(preferences: SharedPreferences? = null): Map<String, *> {
        val prefs = preferences ?: defaultPreferences ?: return emptyMap<String, Any>()
        return prefs.all
    }
    
    /**
     * 批量操作
     *
     * @param preferences 可选的SharedPreferences实例
     * @param action 批量操作
     */
    @JvmStatic
    fun batch(preferences: SharedPreferences? = null, action: SharedPreferences.Editor.() -> Unit) {
        val prefs = preferences ?: defaultPreferences ?: return
        val editor = prefs.edit()
        editor.action()
        editor.apply()
    }
    
    /**
     * 同步提交（立即写入磁盘）
     *
     * @param preferences 可选的SharedPreferences实例
     * @param action 操作
     * @return 是否成功
     */
    @JvmStatic
    fun commit(preferences: SharedPreferences? = null, action: SharedPreferences.Editor.() -> Unit): Boolean {
        val prefs = preferences ?: defaultPreferences ?: return false
        val editor = prefs.edit()
        editor.action()
        return editor.commit()
    }
    
    /**
     * 注册监听器
     *
     * @param listener 监听器
     * @param preferences 可选的SharedPreferences实例
     */
    @JvmStatic
    fun registerListener(
        listener: SharedPreferences.OnSharedPreferenceChangeListener,
        preferences: SharedPreferences? = null
    ) {
        val prefs = preferences ?: defaultPreferences ?: return
        prefs.registerOnSharedPreferenceChangeListener(listener)
    }
    
    /**
     * 取消注册监听器
     *
     * @param listener 监听器
     * @param preferences 可选的SharedPreferences实例
     */
    @JvmStatic
    fun unregisterListener(
        listener: SharedPreferences.OnSharedPreferenceChangeListener,
        preferences: SharedPreferences? = null
    ) {
        val prefs = preferences ?: defaultPreferences ?: return
        prefs.unregisterOnSharedPreferenceChangeListener(listener)
    }
    

    
    /**
     * 获取数据大小（估算）
     *
     * @param preferences 可选的SharedPreferences实例
     * @return 数据大小（字节）
     */
    @JvmStatic
    fun getDataSize(preferences: SharedPreferences? = null): Long {
        val prefs = preferences ?: defaultPreferences ?: return 0L
        
        var size = 0L
        prefs.all.forEach { (key, value) ->
            size += key.length * 2 // 字符串按UTF-16计算
            size += when (value) {
                is String -> value.length * 2
                is Int -> 4
                is Long -> 8
                is Float -> 4
                is Boolean -> 1
                is Set<*> -> value.sumOf { (it?.toString()?.length ?: 0) * 2 }
                else -> value?.toString()?.length?.times(2) ?: 0
            }
        }
        
        return size
    }
}