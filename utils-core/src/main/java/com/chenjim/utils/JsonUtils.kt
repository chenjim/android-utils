package com.chenjim.utils

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * JSON工具类
 * 提供JSON解析、序列化等功能
 *
 * @author chenjim
 * @date 2024/01/01
 */
object JsonUtils {

    /**
     * 判断字符串是否为有效的JSON
     *
     * @param jsonString JSON字符串
     * @return 是否为有效JSON
     */
    @JvmStatic
    fun isValidJson(jsonString: String?): Boolean {
        if (jsonString.isNullOrEmpty()) return false
        
        return try {
            JSONObject(jsonString)
            true
        } catch (e: JSONException) {
            try {
                JSONArray(jsonString)
                true
            } catch (e2: JSONException) {
                false
            }
        }
    }

    /**
     * 判断字符串是否为有效的JSONObject
     *
     * @param jsonString JSON字符串
     * @return 是否为有效JSONObject
     */
    @JvmStatic
    fun isValidJsonObject(jsonString: String?): Boolean {
        if (jsonString.isNullOrEmpty()) return false
        
        return try {
            JSONObject(jsonString)
            true
        } catch (e: JSONException) {
            false
        }
    }

    /**
     * 判断字符串是否为有效的JSONArray
     *
     * @param jsonString JSON字符串
     * @return 是否为有效JSONArray
     */
    @JvmStatic
    fun isValidJsonArray(jsonString: String?): Boolean {
        if (jsonString.isNullOrEmpty()) return false
        
        return try {
            JSONArray(jsonString)
            true
        } catch (e: JSONException) {
            false
        }
    }

    /**
     * 安全获取JSONObject
     *
     * @param jsonString JSON字符串
     * @return JSONObject，解析失败返回null
     */
    @JvmStatic
    fun parseJsonObject(jsonString: String?): JSONObject? {
        if (jsonString.isNullOrEmpty()) return null
        
        return try {
            JSONObject(jsonString)
        } catch (e: JSONException) {
            null
        }
    }

    /**
     * 安全获取JSONArray
     *
     * @param jsonString JSON字符串
     * @return JSONArray，解析失败返回null
     */
    @JvmStatic
    fun parseJsonArray(jsonString: String?): JSONArray? {
        if (jsonString.isNullOrEmpty()) return null
        
        return try {
            JSONArray(jsonString)
        } catch (e: JSONException) {
            null
        }
    }

    /**
     * 安全获取字符串值
     *
     * @param jsonObject JSONObject
     * @param key 键名
     * @param defaultValue 默认值
     * @return 字符串值
     */
    @JvmStatic
    fun getString(jsonObject: JSONObject?, key: String, defaultValue: String = ""): String {
        return try {
            jsonObject?.optString(key, defaultValue) ?: defaultValue
        } catch (e: Exception) {
            defaultValue
        }
    }

    /**
     * 安全获取整数值
     *
     * @param jsonObject JSONObject
     * @param key 键名
     * @param defaultValue 默认值
     * @return 整数值
     */
    @JvmStatic
    fun getInt(jsonObject: JSONObject?, key: String, defaultValue: Int = 0): Int {
        return try {
            jsonObject?.optInt(key, defaultValue) ?: defaultValue
        } catch (e: Exception) {
            defaultValue
        }
    }

    /**
     * 安全获取长整数值
     *
     * @param jsonObject JSONObject
     * @param key 键名
     * @param defaultValue 默认值
     * @return 长整数值
     */
    @JvmStatic
    fun getLong(jsonObject: JSONObject?, key: String, defaultValue: Long = 0L): Long {
        return try {
            jsonObject?.optLong(key, defaultValue) ?: defaultValue
        } catch (e: Exception) {
            defaultValue
        }
    }

    /**
     * 安全获取浮点数值
     *
     * @param jsonObject JSONObject
     * @param key 键名
     * @param defaultValue 默认值
     * @return 浮点数值
     */
    @JvmStatic
    fun getDouble(jsonObject: JSONObject?, key: String, defaultValue: Double = 0.0): Double {
        return try {
            jsonObject?.optDouble(key, defaultValue) ?: defaultValue
        } catch (e: Exception) {
            defaultValue
        }
    }

    /**
     * 安全获取布尔值
     *
     * @param jsonObject JSONObject
     * @param key 键名
     * @param defaultValue 默认值
     * @return 布尔值
     */
    @JvmStatic
    fun getBoolean(jsonObject: JSONObject?, key: String, defaultValue: Boolean = false): Boolean {
        return try {
            jsonObject?.optBoolean(key, defaultValue) ?: defaultValue
        } catch (e: Exception) {
            defaultValue
        }
    }

    /**
     * 安全获取JSONObject
     *
     * @param jsonObject JSONObject
     * @param key 键名
     * @return JSONObject，不存在返回null
     */
    @JvmStatic
    fun getJsonObject(jsonObject: JSONObject?, key: String): JSONObject? {
        return try {
            jsonObject?.optJSONObject(key)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 安全获取JSONArray
     *
     * @param jsonObject JSONObject
     * @param key 键名
     * @return JSONArray，不存在返回null
     */
    @JvmStatic
    fun getJsonArray(jsonObject: JSONObject?, key: String): JSONArray? {
        return try {
            jsonObject?.optJSONArray(key)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 格式化JSON字符串
     *
     * @param jsonString JSON字符串
     * @param indent 缩进空格数
     * @return 格式化后的JSON字符串
     */
    @JvmStatic
    fun formatJson(jsonString: String?, indent: Int = 2): String? {
        if (jsonString.isNullOrEmpty()) return null
        
        return try {
            when {
                isValidJsonObject(jsonString) -> {
                    JSONObject(jsonString).toString(indent)
                }
                isValidJsonArray(jsonString) -> {
                    JSONArray(jsonString).toString(indent)
                }
                else -> null
            }
        } catch (e: JSONException) {
            null
        }
    }

    /**
     * 压缩JSON字符串（移除空格和换行）
     *
     * @param jsonString JSON字符串
     * @return 压缩后的JSON字符串
     */
    @JvmStatic
    fun compactJson(jsonString: String?): String? {
        if (jsonString.isNullOrEmpty()) return null
        
        return try {
            when {
                isValidJsonObject(jsonString) -> {
                    JSONObject(jsonString).toString()
                }
                isValidJsonArray(jsonString) -> {
                    JSONArray(jsonString).toString()
                }
                else -> null
            }
        } catch (e: JSONException) {
            null
        }
    }

    /**
     * 合并两个JSONObject
     *
     * @param target 目标JSONObject
     * @param source 源JSONObject
     * @param overwrite 是否覆盖已存在的键
     * @return 合并后的JSONObject
     */
    @JvmStatic
    fun mergeJsonObject(target: JSONObject?, source: JSONObject?, overwrite: Boolean = true): JSONObject? {
        if (target == null && source == null) return null
        if (target == null) return source
        if (source == null) return target
        
        return try {
            val result = JSONObject(target.toString())
            val keys = source.keys()
            while (keys.hasNext()) {
                val key = keys.next()
                if (overwrite || !result.has(key)) {
                    result.put(key, source.get(key))
                }
            }
            result
        } catch (e: JSONException) {
            target
        }
    }

    /**
     * 从JSONObject中移除指定键
     *
     * @param jsonObject JSONObject
     * @param keys 要移除的键列表
     * @return 移除键后的JSONObject
     */
    @JvmStatic
    fun removeKeys(jsonObject: JSONObject?, vararg keys: String): JSONObject? {
        if (jsonObject == null) return null
        
        return try {
            val result = JSONObject(jsonObject.toString())
            for (key in keys) {
                result.remove(key)
            }
            result
        } catch (e: JSONException) {
            jsonObject
        }
    }

    /**
     * 从JSONObject中只保留指定键
     *
     * @param jsonObject JSONObject
     * @param keys 要保留的键列表
     * @return 只包含指定键的JSONObject
     */
    @JvmStatic
    fun pickKeys(jsonObject: JSONObject?, vararg keys: String): JSONObject? {
        if (jsonObject == null) return null
        
        return try {
            val result = JSONObject()
            for (key in keys) {
                if (jsonObject.has(key)) {
                    result.put(key, jsonObject.get(key))
                }
            }
            result
        } catch (e: JSONException) {
            null
        }
    }

    /**
     * 获取JSONObject的所有键
     *
     * @param jsonObject JSONObject
     * @return 键列表
     */
    @JvmStatic
    fun getKeys(jsonObject: JSONObject?): List<String> {
        if (jsonObject == null) return emptyList()
        
        val keys = mutableListOf<String>()
        val iterator = jsonObject.keys()
        while (iterator.hasNext()) {
            keys.add(iterator.next())
        }
        return keys
    }

    /**
     * 获取JSONArray的大小
     *
     * @param jsonArray JSONArray
     * @return 数组大小
     */
    @JvmStatic
    fun getArraySize(jsonArray: JSONArray?): Int {
        return jsonArray?.length() ?: 0
    }

    /**
     * 安全获取JSONArray中的字符串
     *
     * @param jsonArray JSONArray
     * @param index 索引
     * @param defaultValue 默认值
     * @return 字符串值
     */
    @JvmStatic
    fun getArrayString(jsonArray: JSONArray?, index: Int, defaultValue: String = ""): String {
        return try {
            jsonArray?.optString(index, defaultValue) ?: defaultValue
        } catch (e: Exception) {
            defaultValue
        }
    }

    /**
     * 安全获取JSONArray中的整数
     *
     * @param jsonArray JSONArray
     * @param index 索引
     * @param defaultValue 默认值
     * @return 整数值
     */
    @JvmStatic
    fun getArrayInt(jsonArray: JSONArray?, index: Int, defaultValue: Int = 0): Int {
        return try {
            jsonArray?.optInt(index, defaultValue) ?: defaultValue
        } catch (e: Exception) {
            defaultValue
        }
    }

    /**
     * 安全获取JSONArray中的JSONObject
     *
     * @param jsonArray JSONArray
     * @param index 索引
     * @return JSONObject，不存在返回null
     */
    @JvmStatic
    fun getArrayJsonObject(jsonArray: JSONArray?, index: Int): JSONObject? {
        return try {
            jsonArray?.optJSONObject(index)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 将JSONArray转换为字符串列表
     *
     * @param jsonArray JSONArray
     * @return 字符串列表
     */
    @JvmStatic
    fun jsonArrayToStringList(jsonArray: JSONArray?): List<String> {
        if (jsonArray == null) return emptyList()
        
        val list = mutableListOf<String>()
        for (i in 0 until jsonArray.length()) {
            list.add(getArrayString(jsonArray, i))
        }
        return list
    }

    /**
     * 将字符串列表转换为JSONArray
     *
     * @param list 字符串列表
     * @return JSONArray
     */
    @JvmStatic
    fun stringListToJsonArray(list: List<String>?): JSONArray {
        val jsonArray = JSONArray()
        list?.forEach { jsonArray.put(it) }
        return jsonArray
    }

    /**
     * 将Map转换为JSONObject
     *
     * @param map Map
     * @return JSONObject
     */
    @JvmStatic
    fun mapToJsonObject(map: Map<String, Any>?): JSONObject {
        val jsonObject = JSONObject()
        map?.forEach { (key, value) ->
            try {
                jsonObject.put(key, value)
            } catch (e: JSONException) {
                // 忽略无法序列化的值
            }
        }
        return jsonObject
    }

    /**
     * 将JSONObject转换为Map
     *
     * @param jsonObject JSONObject
     * @return Map
     */
    @JvmStatic
    fun jsonObjectToMap(jsonObject: JSONObject?): Map<String, Any> {
        if (jsonObject == null) return emptyMap()
        
        val map = mutableMapOf<String, Any>()
        val keys = jsonObject.keys()
        while (keys.hasNext()) {
            val key = keys.next()
            try {
                val value = jsonObject.get(key)
                map[key] = value
            } catch (e: JSONException) {
                // 忽略无法获取的值
            }
        }
        return map
    }

    /**
     * 深度复制JSONObject
     *
     * @param jsonObject 源JSONObject
     * @return 复制的JSONObject
     */
    @JvmStatic
    fun deepCopyJsonObject(jsonObject: JSONObject?): JSONObject? {
        if (jsonObject == null) return null
        
        return try {
            JSONObject(jsonObject.toString())
        } catch (e: JSONException) {
            null
        }
    }

    /**
     * 深度复制JSONArray
     *
     * @param jsonArray 源JSONArray
     * @return 复制的JSONArray
     */
    @JvmStatic
    fun deepCopyJsonArray(jsonArray: JSONArray?): JSONArray? {
        if (jsonArray == null) return null
        
        return try {
            JSONArray(jsonArray.toString())
        } catch (e: JSONException) {
            null
        }
    }

    /**
     * 简单的对象序列化为JSON（仅支持基本类型和字符串）
     *
     * @param obj 对象
     * @return JSON字符串
     */
    @JvmStatic
    fun simpleObjectToJson(obj: Any?): String? {
        if (obj == null) return null
        
        return try {
            val jsonObject = JSONObject()
            val clazz = obj.javaClass
            val fields = clazz.declaredFields
            
            for (field in fields) {
                field.isAccessible = true
                val value = field.get(obj)
                
                when (value) {
                    is String, is Number, is Boolean -> {
                        jsonObject.put(field.name, value)
                    }
                    null -> {
                        jsonObject.put(field.name, JSONObject.NULL)
                    }
                }
            }
            
            jsonObject.toString()
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 检查JSON路径是否存在
     *
     * @param jsonObject JSONObject
     * @param path 路径，用点分隔，如"user.profile.name"
     * @return 路径是否存在
     */
    @JvmStatic
    fun hasPath(jsonObject: JSONObject?, path: String): Boolean {
        if (jsonObject == null || path.isEmpty()) return false
        
        return try {
            val keys = path.split(".")
            var current: Any = jsonObject
            
            for (key in keys) {
                if (current is JSONObject) {
                    if (!current.has(key)) return false
                    current = current.get(key)
                } else {
                    return false
                }
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * 根据路径获取值
     *
     * @param jsonObject JSONObject
     * @param path 路径，用点分隔，如"user.profile.name"
     * @param defaultValue 默认值
     * @return 路径对应的值
     */
    @JvmStatic
    fun getValueByPath(jsonObject: JSONObject?, path: String, defaultValue: Any? = null): Any? {
        if (jsonObject == null || path.isEmpty()) return defaultValue
        
        return try {
            val keys = path.split(".")
            var current: Any = jsonObject
            
            for (key in keys) {
                if (current is JSONObject) {
                    if (!current.has(key)) return defaultValue
                    current = current.get(key)
                } else {
                    return defaultValue
                }
            }
            current
        } catch (e: Exception) {
            defaultValue
        }
    }
}