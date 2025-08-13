package com.chenjim.utils

import android.util.Log
import kotlin.math.*

/**
 * @author chenjim  me@h89.cn
 * @version 1.0
 * @description 数字处理工具类
 * @date 2020/12/15
 */
object NumUtil {

    /**
     * 安全解析Float，失败时返回0f
     *
     * @param text 要解析的字符串
     * @return 解析结果，失败时返回0f
     */
    @JvmStatic
    fun parseFloat(text: String?): Float {
        var result = 0f
        if (text == null || text.isEmpty()) {
            return result
        }
        try {
            result = text.toFloat()
        } catch (e: NumberFormatException) {
            Log.e("NumUtil", "parseFloat: $text," + e.message)
        }
        return result
    }

    /**
     * 安全解析Float，失败时返回默认值
     *
     * @param text 要解析的字符串
     * @param defaultValue 默认值
     * @return 解析结果，失败时返回默认值
     */
    @JvmStatic
    fun parseFloat(text: String?, defaultValue: Float): Float {
        if (text == null || text.isEmpty()) {
            return defaultValue
        }
        try {
            return text.toFloat()
        } catch (e: NumberFormatException) {
            try {
                Log.e("NumUtil", "parseFloat: $text," + e.message)
            } catch (ignored: Exception) {
                // Ignore logging errors in test environment
            }
        }
        return defaultValue
    }

    /**
     * 安全解析Int，失败时返回0
     *
     * @param text 要解析的字符串
     * @return 解析结果，失败时返回0
     */
    @JvmStatic
    fun parseInt(text: String?): Int {
        var result = 0
        if (text == null || text.isEmpty()) {
            return result
        }
        try {
            result = text.toInt()
        } catch (e: NumberFormatException) {
            try {
                Log.e("NumUtil", "parseInt: $text," + e.message)
            } catch (ignored: Exception) {
                // Ignore logging errors in test environment
            }
        }
        return result
    }

    /**
     * 安全解析Int，失败时返回默认值
     *
     * @param text 要解析的字符串
     * @param defaultValue 默认值
     * @return 解析结果，失败时返回默认值
     */
    @JvmStatic
    fun parseInt(text: String?, defaultValue: Int): Int {
        if (text == null || text.isEmpty()) {
            return defaultValue
        }
        try {
            return text.toInt()
        } catch (e: NumberFormatException) {
            try {
                Log.e("NumUtil", "parseInt: $text," + e.message)
            } catch (ignored: Exception) {
                // Ignore logging errors in test environment
            }
        }
        return defaultValue
    }

    /**
     * 安全解析Double，失败时返回0.0
     *
     * @param text 要解析的字符串
     * @return 解析结果，失败时返回0.0
     */
    @JvmStatic
    fun parseDouble(text: String?): Double {
        var result = 0.0
        if (text == null || text.isEmpty()) {
            return result
        }
        try {
            result = text.toDouble()
        } catch (e: NumberFormatException) {
            try {
                Log.e("NumUtil", "parseDouble: $text," + e.message)
            } catch (ignored: Exception) {
                // Ignore logging errors in test environment
            }
        }
        return result
    }

    /**
     * 安全解析Double，失败时返回默认值
     *
     * @param text 要解析的字符串
     * @param defaultValue 默认值
     * @return 解析结果，失败时返回默认值
     */
    @JvmStatic
    fun parseDouble(text: String?, defaultValue: Double): Double {
        if (text == null || text.isEmpty()) {
            return defaultValue
        }
        try {
            return text.toDouble()
        } catch (e: NumberFormatException) {
            try {
                Log.e("NumUtil", "parseDouble: $text," + e.message)
            } catch (ignored: Exception) {
                // Ignore logging errors in test environment
            }
        }
        return defaultValue
    }

    /**
     * 安全解析Long，失败时返回0L
     *
     * @param text 要解析的字符串
     * @return 解析结果，失败时返回0L
     */
    @JvmStatic
    fun parseLong(text: String?): Long {
        var result = 0L
        if (text == null || text.isEmpty()) {
            return result
        }
        try {
            result = text.toLong()
        } catch (e: NumberFormatException) {
            try {
                Log.e("NumUtil", "parseLong: $text," + e.message)
            } catch (ignored: Exception) {
                // Ignore logging errors in test environment
            }
        }
        return result
    }

    /**
     * 安全解析Long，失败时返回默认值
     *
     * @param text 要解析的字符串
     * @param defaultValue 默认值
     * @return 解析结果，失败时返回默认值
     */
    @JvmStatic
    fun parseLong(text: String?, defaultValue: Long): Long {
        if (text == null || text.isEmpty()) {
            return defaultValue
        }
        try {
            return text.toLong()
        } catch (e: NumberFormatException) {
            try {
                Log.e("NumUtil", "parseLong: $text," + e.message)
            } catch (ignored: Exception) {
                // Ignore logging errors in test environment
            }
        }
        return defaultValue
    }

    /**
     * 格式化数字，保留指定小数位数
     *
     * @param number 要格式化的数字
     * @param decimalPlaces 小数位数
     * @return 格式化后的字符串
     */
    @JvmStatic
    fun formatDecimal(number: Double, decimalPlaces: Int): String {
        return String.format("%.${decimalPlaces}f", number)
    }

    /**
     * 判断字符串是否为数字
     *
     * @param text 要判断的字符串
     * @return 是否为数字
     */
    @JvmStatic
    fun isNumeric(text: String?): Boolean {
        if (text == null || text.isEmpty()) {
            return false
        }
        return try {
            text.toDouble()
            true
        } catch (e: NumberFormatException) {
            false
        }
    }

    /**
     * 判断字符串是否为整数
     *
     * @param text 要判断的字符串
     * @return 是否为整数
     */
    @JvmStatic
    fun isInteger(text: String?): Boolean {
        if (text == null || text.isEmpty()) {
            return false
        }
        return try {
            text.toInt()
            true
        } catch (e: NumberFormatException) {
            false
        }
    }

    /**
     * 限制数值在指定范围内
     *
     * @param value 要限制的值
     * @param min 最小值
     * @param max 最大值
     * @return 限制后的值
     */
    @JvmStatic
    fun clamp(value: Int, min: Int, max: Int): Int {
        return when {
            value < min -> min
            value > max -> max
            else -> value
        }
    }

    /**
     * 限制数值在指定范围内
     *
     * @param value 要限制的值
     * @param min 最小值
     * @param max 最大值
     * @return 限制后的值
     */
    @JvmStatic
    fun clamp(value: Float, min: Float, max: Float): Float {
        return when {
            value < min -> min
            value > max -> max
            else -> value
        }
    }

    /**
     * 限制数值在指定范围内
     *
     * @param value 要限制的值
     * @param min 最小值
     * @param max 最大值
     * @return 限制后的值
     */
    @JvmStatic
    fun clamp(value: Double, min: Double, max: Double): Double {
        return when {
            value < min -> min
            value > max -> max
            else -> value
        }
    }

    /**
     * 将数字转换为百分比字符串
     *
     * @param value 数值（0.0-1.0）
     * @param decimalPlaces 小数位数
     * @return 百分比字符串，如"50.00%"
     */
    @JvmStatic
    fun toPercentage(value: Double, decimalPlaces: Int = 2): String {
        return formatDecimal(value * 100, decimalPlaces) + "%"
    }

    /**
     * 将百分比字符串转换为数值
     *
     * @param percentage 百分比字符串，如"50%"或"50.5%"
     * @return 数值（0.0-1.0），解析失败返回0.0
     */
    @JvmStatic
    fun fromPercentage(percentage: String?): Double {
        if (percentage.isNullOrEmpty()) return 0.0
        val cleanStr = percentage.replace("%", "").trim()
        return parseDouble(cleanStr) / 100.0
    }

    /**
     * 数字格式化为带单位的字符串（K, M, B等）
     *
     * @param number 要格式化的数字
     * @param decimalPlaces 小数位数
     * @return 格式化后的字符串
     */
    @JvmStatic
    fun formatWithUnit(number: Long, decimalPlaces: Int = 1): String {
        return when {
            number >= 1_000_000_000 -> formatDecimal(number / 1_000_000_000.0, decimalPlaces) + "B"
            number >= 1_000_000 -> formatDecimal(number / 1_000_000.0, decimalPlaces) + "M"
            number >= 1_000 -> formatDecimal(number / 1_000.0, decimalPlaces) + "K"
            else -> number.toString()
        }
    }

    /**
     * 计算两个数的最大公约数
     *
     * @param a 第一个数
     * @param b 第二个数
     * @return 最大公约数
     */
    @JvmStatic
    fun gcd(a: Int, b: Int): Int {
        return if (b == 0) a else gcd(b, a % b)
    }

    /**
     * 计算两个数的最小公倍数
     *
     * @param a 第一个数
     * @param b 第二个数
     * @return 最小公倍数
     */
    @JvmStatic
    fun lcm(a: Int, b: Int): Int {
        return (a * b) / gcd(a, b)
    }

    /**
     * 判断是否为质数
     *
     * @param number 要判断的数
     * @return 是否为质数
     */
    @JvmStatic
    fun isPrime(number: Int): Boolean {
        if (number < 2) return false
        if (number == 2) return true
        if (number % 2 == 0) return false
        
        val sqrt = kotlin.math.sqrt(number.toDouble()).toInt()
        for (i in 3..sqrt step 2) {
            if (number % i == 0) return false
        }
        return true
    }

    /**
     * 计算阶乘
     *
     * @param n 要计算阶乘的数
     * @return 阶乘结果
     */
    @JvmStatic
    fun factorial(n: Int): Long {
        if (n < 0) throw IllegalArgumentException("Factorial is not defined for negative numbers")
        if (n <= 1) return 1
        var result = 1L
        for (i in 2..n) {
            result *= i
        }
        return result
    }

    /**
     * 将字节数转换为可读的文件大小字符串
     *
     * @param bytes 字节数
     * @param decimalPlaces 小数位数
     * @return 格式化后的文件大小字符串
     */
    @JvmStatic
    fun formatFileSize(bytes: Long, decimalPlaces: Int = 2): String {
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        var size = bytes.toDouble()
        var unitIndex = 0
        
        while (size >= 1024 && unitIndex < units.size - 1) {
            size /= 1024
            unitIndex++
        }
        
        return if (unitIndex == 0) {
            "${bytes}B"
        } else {
            formatDecimal(size, decimalPlaces) + units[unitIndex]
        }
    }

    /**
     * 生成指定范围内的随机整数
     *
     * @param min 最小值（包含）
     * @param max 最大值（包含）
     * @return 随机整数
     */
    @JvmStatic
    fun randomInt(min: Int, max: Int): Int {
        return (min..max).random()
    }

    /**
     * 生成指定范围内的随机浮点数
     *
     * @param min 最小值（包含）
     * @param max 最大值（不包含）
     * @return 随机浮点数
     */
    @JvmStatic
    fun randomDouble(min: Double, max: Double): Double {
        return min + (max - min) * kotlin.random.Random.nextDouble()
    }

    /**
     * 四舍五入到指定小数位数
     *
     * @param value 要四舍五入的值
     * @param decimalPlaces 小数位数
     * @return 四舍五入后的值
     */
    @JvmStatic
    fun round(value: Double, decimalPlaces: Int): Double {
        val factor = 10.0.pow(decimalPlaces.toDouble())
        return kotlin.math.round(value * factor) / factor
    }
}