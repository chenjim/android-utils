package com.chenjim.utils

import java.security.MessageDigest
import java.util.regex.Pattern
import kotlin.random.Random

/**
 * 字符串工具类
 * 提供常用的字符串处理功能
 *
 * @author chenjim
 * @date 2024/01/01
 */
object StringUtils {

    /**
     * 判断字符串是否为空或null
     *
     * @param str 要判断的字符串
     * @return 是否为空
     */
    @JvmStatic
    fun isEmpty(str: String?): Boolean {
        return str.isNullOrEmpty()
    }

    /**
     * 判断字符串是否不为空
     *
     * @param str 要判断的字符串
     * @return 是否不为空
     */
    @JvmStatic
    fun isNotEmpty(str: String?): Boolean {
        return !str.isNullOrEmpty()
    }

    /**
     * 判断字符串是否为空白（null、空字符串或只包含空白字符）
     *
     * @param str 要判断的字符串
     * @return 是否为空白
     */
    @JvmStatic
    fun isBlank(str: String?): Boolean {
        return str.isNullOrBlank()
    }

    /**
     * 判断字符串是否不为空白
     *
     * @param str 要判断的字符串
     * @return 是否不为空白
     */
    @JvmStatic
    fun isNotBlank(str: String?): Boolean {
        return !str.isNullOrBlank()
    }

    /**
     * 安全地获取字符串，null时返回空字符串
     *
     * @param str 原字符串
     * @return 非null的字符串
     */
    @JvmStatic
    fun safe(str: String?): String {
        return str ?: ""
    }

    /**
     * 安全地获取字符串，null时返回默认值
     *
     * @param str 原字符串
     * @param defaultValue 默认值
     * @return 非null的字符串
     */
    @JvmStatic
    fun safe(str: String?, defaultValue: String): String {
        return str ?: defaultValue
    }

    /**
     * 截取字符串，超出长度时添加省略号
     *
     * @param str 原字符串
     * @param maxLength 最大长度
     * @param ellipsis 省略号，默认为"..."
     * @return 截取后的字符串
     */
    @JvmStatic
    fun truncate(str: String?, maxLength: Int, ellipsis: String = "..."): String {
        if (str.isNullOrEmpty() || str.length <= maxLength) {
            return str ?: ""
        }
        return str.substring(0, maxLength - ellipsis.length) + ellipsis
    }

    /**
     * 反转字符串
     *
     * @param str 要反转的字符串
     * @return 反转后的字符串
     */
    @JvmStatic
    fun reverse(str: String?): String {
        return str?.reversed() ?: ""
    }

    /**
     * 统计字符串中某个字符的出现次数
     *
     * @param str 原字符串
     * @param char 要统计的字符
     * @return 出现次数
     */
    @JvmStatic
    fun countChar(str: String?, char: Char): Int {
        return str?.count { it == char } ?: 0
    }

    /**
     * 统计字符串中某个子字符串的出现次数
     *
     * @param str 原字符串
     * @param substring 要统计的子字符串
     * @param ignoreCase 是否忽略大小写
     * @return 出现次数
     */
    @JvmStatic
    fun countSubstring(str: String?, substring: String, ignoreCase: Boolean = false): Int {
        if (str.isNullOrEmpty() || substring.isEmpty()) return 0
        
        val source = if (ignoreCase) str.lowercase() else str
        val target = if (ignoreCase) substring.lowercase() else substring
        
        var count = 0
        var index = 0
        while (source.indexOf(target, index).also { index = it } != -1) {
            count++
            index += target.length
        }
        return count
    }

    /**
     * 移除字符串中的所有空白字符
     *
     * @param str 原字符串
     * @return 移除空白字符后的字符串
     */
    @JvmStatic
    fun removeWhitespace(str: String?): String {
        return str?.replace("\\s".toRegex(), "") ?: ""
    }

    /**
     * 移除字符串中的HTML标签
     *
     * @param str 包含HTML的字符串
     * @return 移除HTML标签后的字符串
     */
    @JvmStatic
    fun removeHtmlTags(str: String?): String {
        return str?.replace("<[^>]*>".toRegex(), "") ?: ""
    }

    /**
     * 将字符串转换为驼峰命名法
     *
     * @param str 原字符串
     * @param delimiter 分隔符，默认为下划线
     * @return 驼峰命名的字符串
     */
    @JvmStatic
    fun toCamelCase(str: String?, delimiter: String = "_"): String {
        if (str.isNullOrEmpty()) return ""
        
        return str.split(delimiter)
            .mapIndexed { index, word ->
                if (index == 0) {
                    word.lowercase()
                } else {
                    word.lowercase().replaceFirstChar { it.uppercase() }
                }
            }
            .joinToString("")
    }

    /**
     * 将驼峰命名转换为下划线命名
     *
     * @param str 驼峰命名的字符串
     * @return 下划线命名的字符串
     */
    @JvmStatic
    fun toSnakeCase(str: String?): String {
        if (str.isNullOrEmpty()) return ""
        
        return str.replace("([a-z])([A-Z])".toRegex(), "$1_$2").lowercase()
    }

    /**
     * 首字母大写
     *
     * @param str 原字符串
     * @return 首字母大写的字符串
     */
    @JvmStatic
    fun capitalize(str: String?): String {
        return str?.replaceFirstChar { it.uppercase() } ?: ""
    }

    /**
     * 首字母小写
     *
     * @param str 原字符串
     * @return 首字母小写的字符串
     */
    @JvmStatic
    fun uncapitalize(str: String?): String {
        return str?.replaceFirstChar { it.lowercase() } ?: ""
    }

    /**
     * 判断字符串是否只包含数字
     *
     * @param str 要判断的字符串
     * @return 是否只包含数字
     */
    @JvmStatic
    fun isNumeric(str: String?): Boolean {
        return str?.all { it.isDigit() } == true && str.isNotEmpty()
    }

    /**
     * 判断字符串是否只包含字母
     *
     * @param str 要判断的字符串
     * @return 是否只包含字母
     */
    @JvmStatic
    fun isAlpha(str: String?): Boolean {
        return str?.all { it.isLetter() } == true && str.isNotEmpty()
    }

    /**
     * 判断字符串是否只包含字母和数字
     *
     * @param str 要判断的字符串
     * @return 是否只包含字母和数字
     */
    @JvmStatic
    fun isAlphanumeric(str: String?): Boolean {
        return str?.all { it.isLetterOrDigit() } == true && str.isNotEmpty()
    }

    /**
     * 生成随机字符串
     *
     * @param length 字符串长度
     * @param chars 字符集，默认为字母和数字
     * @return 随机字符串
     */
    @JvmStatic
    fun randomString(length: Int, chars: String = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"): String {
        return (1..length)
            .map { chars[Random.nextInt(chars.length)] }
            .joinToString("")
    }

    /**
     * 计算字符串的MD5哈希值
     *
     * @param str 要计算的字符串
     * @return MD5哈希值的十六进制字符串
     */
    @JvmStatic
    fun md5(str: String?): String {
        if (str.isNullOrEmpty()) return ""
        
        return try {
            val digest = MessageDigest.getInstance("MD5")
            val bytes = digest.digest(str.toByteArray())
            bytes.joinToString("") { "%02x".format(it) }
        } catch (e: Exception) {
            ""
        }
    }

    /**
     * 计算字符串的SHA-256哈希值
     *
     * @param str 要计算的字符串
     * @return SHA-256哈希值的十六进制字符串
     */
    @JvmStatic
    fun sha256(str: String?): String {
        if (str.isNullOrEmpty()) return ""
        
        return try {
            val digest = MessageDigest.getInstance("SHA-256")
            val bytes = digest.digest(str.toByteArray())
            bytes.joinToString("") { "%02x".format(it) }
        } catch (e: Exception) {
            ""
        }
    }

    /**
     * 验证邮箱格式
     *
     * @param email 邮箱地址
     * @return 是否为有效邮箱格式
     */
    @JvmStatic
    fun isValidEmail(email: String?): Boolean {
        if (email.isNullOrEmpty()) return false
        
        val pattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        return Pattern.matches(pattern, email)
    }

    /**
     * 验证手机号格式（中国大陆）
     *
     * @param phone 手机号
     * @return 是否为有效手机号格式
     */
    @JvmStatic
    fun isValidPhone(phone: String?): Boolean {
        if (phone.isNullOrEmpty()) return false
        
        val pattern = "^1[3-9]\\d{9}$"
        return Pattern.matches(pattern, phone)
    }

    /**
     * 验证身份证号格式（中国大陆）
     *
     * @param idCard 身份证号
     * @return 是否为有效身份证号格式
     */
    @JvmStatic
    fun isValidIdCard(idCard: String?): Boolean {
        if (idCard.isNullOrEmpty()) return false
        
        val pattern = "^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$"
        return Pattern.matches(pattern, idCard)
    }

    /**
     * 掩码处理敏感信息
     *
     * @param str 原字符串
     * @param startVisible 开始显示的字符数
     * @param endVisible 结束显示的字符数
     * @param maskChar 掩码字符，默认为*
     * @return 掩码处理后的字符串
     */
    @JvmStatic
    fun mask(str: String?, startVisible: Int = 3, endVisible: Int = 4, maskChar: Char = '*'): String {
        if (str.isNullOrEmpty() || str.length <= startVisible + endVisible) {
            return str ?: ""
        }
        
        val start = str.substring(0, startVisible)
        val end = str.substring(str.length - endVisible)
        val maskLength = str.length - startVisible - endVisible
        val mask = maskChar.toString().repeat(maskLength)
        
        return start + mask + end
    }

    /**
     * 格式化文件大小
     *
     * @param bytes 字节数
     * @return 格式化后的文件大小字符串
     */
    @JvmStatic
    fun formatFileSize(bytes: Long): String {
        return NumUtil.formatFileSize(bytes)
    }

    /**
     * 将字符串按指定长度分割成列表
     *
     * @param str 原字符串
     * @param chunkSize 每段的长度
     * @return 分割后的字符串列表
     */
    @JvmStatic
    fun chunk(str: String?, chunkSize: Int): List<String> {
        if (str.isNullOrEmpty() || chunkSize <= 0) return emptyList()
        
        return str.chunked(chunkSize)
    }

    /**
     * 重复字符串指定次数
     *
     * @param str 要重复的字符串
     * @param times 重复次数
     * @return 重复后的字符串
     */
    @JvmStatic
    fun repeat(str: String?, times: Int): String {
        if (str.isNullOrEmpty() || times <= 0) return ""
        return str.repeat(times)
    }

    /**
     * 左填充字符串到指定长度
     *
     * @param str 原字符串
     * @param length 目标长度
     * @param padChar 填充字符，默认为空格
     * @return 填充后的字符串
     */
    @JvmStatic
    fun padStart(str: String?, length: Int, padChar: Char = ' '): String {
        return (str ?: "").padStart(length, padChar)
    }

    /**
     * 右填充字符串到指定长度
     *
     * @param str 原字符串
     * @param length 目标长度
     * @param padChar 填充字符，默认为空格
     * @return 填充后的字符串
     */
    @JvmStatic
    fun padEnd(str: String?, length: Int, padChar: Char = ' '): String {
        return (str ?: "").padEnd(length, padChar)
    }
}