package com.chenjim.utils

import java.util.regex.Pattern
import java.text.SimpleDateFormat
import java.util.*

/**
 * 数据验证工具类
 * 提供各种常用的数据格式验证功能
 *
 * @author ChenJim
 * @date 2024/01/01
 */
object ValidationUtils {
    
    // 正则表达式常量
    private const val EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    private const val PHONE_PATTERN = "^1[3-9]\\d{9}$"
    private const val ID_CARD_PATTERN = "^[1-9]\\d{5}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[\\dXx]$"
    private const val URL_PATTERN = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$"
    private const val IP_PATTERN = "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$"
    private const val MAC_PATTERN = "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$"
    private const val CHINESE_PATTERN = "^[\\u4e00-\\u9fa5]+$"
    private const val ENGLISH_PATTERN = "^[a-zA-Z]+$"
    private const val NUMBER_PATTERN = "^\\d+$"
    private const val DECIMAL_PATTERN = "^\\d+(\\.\\d+)?$"
    private const val PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d@$!%*?&]{8,}$"
    private const val BANK_CARD_PATTERN = "^[1-9]\\d{12,19}$"
    private const val POSTAL_CODE_PATTERN = "^[1-9]\\d{5}$"
    private const val QQ_PATTERN = "^[1-9]\\d{4,10}$"
    private const val WECHAT_PATTERN = "^[a-zA-Z][a-zA-Z0-9_-]{5,19}$"
    
    /**
     * 验证邮箱格式
     *
     * @param email 邮箱地址
     * @return 是否有效
     */
    @JvmStatic
    fun isValidEmail(email: String?): Boolean {
        return !email.isNullOrBlank() && Pattern.matches(EMAIL_PATTERN, email)
    }
    
    /**
     * 验证手机号格式（中国大陆）
     *
     * @param phone 手机号
     * @return 是否有效
     */
    @JvmStatic
    fun isValidPhone(phone: String?): Boolean {
        return !phone.isNullOrBlank() && Pattern.matches(PHONE_PATTERN, phone)
    }
    
    /**
     * 验证身份证号格式（中国大陆）
     *
     * @param idCard 身份证号
     * @return 是否有效
     */
    @JvmStatic
    fun isValidIdCard(idCard: String?): Boolean {
        if (idCard.isNullOrBlank() || !Pattern.matches(ID_CARD_PATTERN, idCard)) {
            return false
        }
        
        // 验证校验位
        return validateIdCardChecksum(idCard)
    }
    
    /**
     * 验证身份证校验位
     *
     * @param idCard 身份证号
     * @return 是否有效
     */
    private fun validateIdCardChecksum(idCard: String): Boolean {
        val weights = intArrayOf(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2)
        val checkCodes = charArrayOf('1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2')
        
        var sum = 0
        for (i in 0..16) {
            sum += (idCard[i] - '0') * weights[i]
        }
        
        val checkCode = checkCodes[sum % 11]
        return idCard[17].uppercaseChar() == checkCode
    }
    
    /**
     * 验证URL格式
     *
     * @param url URL地址
     * @return 是否有效
     */
    @JvmStatic
    fun isValidUrl(url: String?): Boolean {
        return !url.isNullOrBlank() && Pattern.matches(URL_PATTERN, url)
    }
    
    /**
     * 验证IP地址格式
     *
     * @param ip IP地址
     * @return 是否有效
     */
    @JvmStatic
    fun isValidIp(ip: String?): Boolean {
        return !ip.isNullOrBlank() && Pattern.matches(IP_PATTERN, ip)
    }
    
    /**
     * 验证MAC地址格式
     *
     * @param mac MAC地址
     * @return 是否有效
     */
    @JvmStatic
    fun isValidMac(mac: String?): Boolean {
        return !mac.isNullOrBlank() && Pattern.matches(MAC_PATTERN, mac)
    }
    
    /**
     * 验证是否为纯中文
     *
     * @param text 文本
     * @return 是否为纯中文
     */
    @JvmStatic
    fun isChinese(text: String?): Boolean {
        return !text.isNullOrBlank() && Pattern.matches(CHINESE_PATTERN, text)
    }
    
    /**
     * 验证是否为纯英文
     *
     * @param text 文本
     * @return 是否为纯英文
     */
    @JvmStatic
    fun isEnglish(text: String?): Boolean {
        return !text.isNullOrBlank() && Pattern.matches(ENGLISH_PATTERN, text)
    }
    
    /**
     * 验证是否为纯数字
     *
     * @param text 文本
     * @return 是否为纯数字
     */
    @JvmStatic
    fun isNumber(text: String?): Boolean {
        return !text.isNullOrBlank() && Pattern.matches(NUMBER_PATTERN, text)
    }
    
    /**
     * 验证是否为小数
     *
     * @param text 文本
     * @return 是否为小数
     */
    @JvmStatic
    fun isDecimal(text: String?): Boolean {
        return !text.isNullOrBlank() && Pattern.matches(DECIMAL_PATTERN, text)
    }
    
    /**
     * 验证密码强度（至少8位，包含大小写字母和数字）
     *
     * @param password 密码
     * @return 是否符合要求
     */
    @JvmStatic
    fun isStrongPassword(password: String?): Boolean {
        return !password.isNullOrBlank() && Pattern.matches(PASSWORD_PATTERN, password)
    }
    
    /**
     * 验证银行卡号格式
     *
     * @param bankCard 银行卡号
     * @return 是否有效
     */
    @JvmStatic
    fun isValidBankCard(bankCard: String?): Boolean {
        if (bankCard.isNullOrBlank() || !Pattern.matches(BANK_CARD_PATTERN, bankCard)) {
            return false
        }
        
        // 使用Luhn算法验证
        return validateLuhn(bankCard)
    }
    
    /**
     * Luhn算法验证
     *
     * @param number 数字字符串
     * @return 是否有效
     */
    private fun validateLuhn(number: String): Boolean {
        var sum = 0
        var alternate = false
        
        for (i in number.length - 1 downTo 0) {
            var n = number[i] - '0'
            
            if (alternate) {
                n *= 2
                if (n > 9) {
                    n = n % 10 + 1
                }
            }
            
            sum += n
            alternate = !alternate
        }
        
        return sum % 10 == 0
    }
    
    /**
     * 验证邮政编码格式
     *
     * @param postalCode 邮政编码
     * @return 是否有效
     */
    @JvmStatic
    fun isValidPostalCode(postalCode: String?): Boolean {
        return !postalCode.isNullOrBlank() && Pattern.matches(POSTAL_CODE_PATTERN, postalCode)
    }
    
    /**
     * 验证QQ号格式
     *
     * @param qq QQ号
     * @return 是否有效
     */
    @JvmStatic
    fun isValidQQ(qq: String?): Boolean {
        return !qq.isNullOrBlank() && Pattern.matches(QQ_PATTERN, qq)
    }
    
    /**
     * 验证微信号格式
     *
     * @param wechat 微信号
     * @return 是否有效
     */
    @JvmStatic
    fun isValidWechat(wechat: String?): Boolean {
        return !wechat.isNullOrBlank() && Pattern.matches(WECHAT_PATTERN, wechat)
    }
    
    /**
     * 验证日期格式
     *
     * @param date 日期字符串
     * @param format 日期格式
     * @return 是否有效
     */
    @JvmStatic
    fun isValidDate(date: String?, format: String = "yyyy-MM-dd"): Boolean {
        if (date.isNullOrBlank()) return false
        
        return try {
            val sdf = SimpleDateFormat(format, Locale.getDefault())
            sdf.isLenient = false
            sdf.parse(date)
            true
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * 验证时间格式
     *
     * @param time 时间字符串
     * @param format 时间格式
     * @return 是否有效
     */
    @JvmStatic
    fun isValidTime(time: String?, format: String = "HH:mm:ss"): Boolean {
        return isValidDate(time, format)
    }
    
    /**
     * 验证年龄范围
     *
     * @param age 年龄
     * @param minAge 最小年龄
     * @param maxAge 最大年龄
     * @return 是否在范围内
     */
    @JvmStatic
    fun isValidAge(age: Int, minAge: Int = 0, maxAge: Int = 150): Boolean {
        return age in minAge..maxAge
    }
    
    /**
     * 验证字符串长度范围
     *
     * @param text 文本
     * @param minLength 最小长度
     * @param maxLength 最大长度
     * @return 是否在范围内
     */
    @JvmStatic
    fun isValidLength(text: String?, minLength: Int = 0, maxLength: Int = Int.MAX_VALUE): Boolean {
        val length = text?.length ?: 0
        return length in minLength..maxLength
    }
    
    /**
     * 验证数值范围
     *
     * @param value 数值
     * @param min 最小值
     * @param max 最大值
     * @return 是否在范围内
     */
    @JvmStatic
    fun isInRange(value: Double, min: Double = Double.MIN_VALUE, max: Double = Double.MAX_VALUE): Boolean {
        return value in min..max
    }
    
    /**
     * 验证是否包含特殊字符
     *
     * @param text 文本
     * @param allowedChars 允许的特殊字符
     * @return 是否只包含允许的字符
     */
    @JvmStatic
    fun containsOnlyAllowedChars(text: String?, allowedChars: String = ""): Boolean {
        if (text.isNullOrBlank()) return true
        
        val pattern = "^[a-zA-Z0-9\\u4e00-\\u9fa5${Pattern.quote(allowedChars)}]+$"
        return Pattern.matches(pattern, text)
    }
    
    /**
     * 验证是否为有效的文件名
     *
     * @param fileName 文件名
     * @return 是否有效
     */
    @JvmStatic
    fun isValidFileName(fileName: String?): Boolean {
        if (fileName.isNullOrBlank()) return false
        
        val invalidChars = charArrayOf('/', '\\', ':', '*', '?', '"', '<', '>', '|')
        return !fileName.any { it in invalidChars }
    }
    
    /**
     * 验证是否为有效的文件扩展名
     *
     * @param extension 扩展名
     * @param allowedExtensions 允许的扩展名列表
     * @return 是否有效
     */
    @JvmStatic
    fun isValidFileExtension(extension: String?, allowedExtensions: List<String>): Boolean {
        if (extension.isNullOrBlank()) return false
        
        val normalizedExtension = extension.lowercase().removePrefix(".")
        val normalizedAllowed = allowedExtensions.map { it.lowercase().removePrefix(".") }
        
        return normalizedExtension in normalizedAllowed
    }
    
    /**
     * 验证颜色值格式（十六进制）
     *
     * @param color 颜色值
     * @return 是否有效
     */
    @JvmStatic
    fun isValidHexColor(color: String?): Boolean {
        if (color.isNullOrBlank()) return false
        
        val pattern = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$"
        return Pattern.matches(pattern, color)
    }
    
    /**
     * 验证版本号格式（如：1.0.0）
     *
     * @param version 版本号
     * @return 是否有效
     */
    @JvmStatic
    fun isValidVersion(version: String?): Boolean {
        if (version.isNullOrBlank()) return false
        
        val pattern = "^\\d+(\\.\\d+)*$"
        return Pattern.matches(pattern, version)
    }
    
    /**
     * 验证JSON格式
     *
     * @param json JSON字符串
     * @return 是否有效
     */
    @JvmStatic
    fun isValidJson(json: String?): Boolean {
        if (json.isNullOrBlank()) return false
        
        return try {
            // 这里可以使用具体的JSON库进行验证
            // 简单验证：检查是否以{}或[]开始和结束
            val trimmed = json.trim()
            (trimmed.startsWith("{") && trimmed.endsWith("}")) ||
            (trimmed.startsWith("[") && trimmed.endsWith("]"))
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * 验证Base64格式
     *
     * @param base64 Base64字符串
     * @return 是否有效
     */
    @JvmStatic
    fun isValidBase64(base64: String?): Boolean {
        if (base64.isNullOrBlank()) return false
        
        val pattern = "^[A-Za-z0-9+/]*={0,2}$"
        return Pattern.matches(pattern, base64) && base64.length % 4 == 0
    }
    
    /**
     * 验证经纬度坐标
     *
     * @param latitude 纬度
     * @param longitude 经度
     * @return 是否有效
     */
    @JvmStatic
    fun isValidCoordinate(latitude: Double, longitude: Double): Boolean {
        return latitude in -90.0..90.0 && longitude in -180.0..180.0
    }
    
    /**
     * 验证车牌号格式（中国大陆）
     *
     * @param plateNumber 车牌号
     * @return 是否有效
     */
    @JvmStatic
    fun isValidPlateNumber(plateNumber: String?): Boolean {
        if (plateNumber.isNullOrBlank()) return false
        
        // 普通车牌：省份简称+字母+5位数字或字母
        val normalPattern = "^[\\u4e00-\\u9fa5][A-Z][A-Z0-9]{5}$"
        // 新能源车牌：省份简称+字母+6位数字或字母
        val newEnergyPattern = "^[\\u4e00-\\u9fa5][A-Z][A-Z0-9]{6}$"
        
        return Pattern.matches(normalPattern, plateNumber) || 
               Pattern.matches(newEnergyPattern, plateNumber)
    }
    
    /**
     * 验证统一社会信用代码
     *
     * @param creditCode 统一社会信用代码
     * @return 是否有效
     */
    @JvmStatic
    fun isValidCreditCode(creditCode: String?): Boolean {
        if (creditCode.isNullOrBlank() || creditCode.length != 18) return false
        
        val pattern = "^[0-9A-HJ-NPQRTUWXY]{2}\\d{6}[0-9A-HJ-NPQRTUWXY]{10}$"
        return Pattern.matches(pattern, creditCode)
    }
    
    /**
     * 验证多个条件（AND逻辑）
     *
     * @param validators 验证器列表
     * @return 是否全部通过
     */
    @JvmStatic
    fun validateAll(vararg validators: () -> Boolean): Boolean {
        return validators.all { it() }
    }
    
    /**
     * 验证多个条件（OR逻辑）
     *
     * @param validators 验证器列表
     * @return 是否至少一个通过
     */
    @JvmStatic
    fun validateAny(vararg validators: () -> Boolean): Boolean {
        return validators.any { it() }
    }
    
    /**
     * 验证结果类
     */
    data class ValidationResult(
        val isValid: Boolean,
        val errorMessage: String? = null,
        val errorCode: String? = null
    )
    
    /**
     * 验证器建造者类
     */
    class ValidatorBuilder {
        private val validators = mutableListOf<Pair<() -> Boolean, String>>()
        
        fun email(email: String?, errorMessage: String = "邮箱格式不正确") = apply {
            validators.add({ isValidEmail(email) } to errorMessage)
        }
        
        fun phone(phone: String?, errorMessage: String = "手机号格式不正确") = apply {
            validators.add({ isValidPhone(phone) } to errorMessage)
        }
        
        fun idCard(idCard: String?, errorMessage: String = "身份证号格式不正确") = apply {
            validators.add({ isValidIdCard(idCard) } to errorMessage)
        }
        
        fun length(text: String?, min: Int, max: Int, errorMessage: String = "长度不符合要求") = apply {
            validators.add({ isValidLength(text, min, max) } to errorMessage)
        }
        
        fun notEmpty(text: String?, errorMessage: String = "不能为空") = apply {
            validators.add({ !text.isNullOrBlank() } to errorMessage)
        }
        
        fun custom(validator: () -> Boolean, errorMessage: String) = apply {
            validators.add(validator to errorMessage)
        }
        
        fun validate(): ValidationResult {
            validators.forEach { (validator, errorMessage) ->
                if (!validator()) {
                    return ValidationResult(false, errorMessage)
                }
            }
            return ValidationResult(true)
        }
    }
    
    /**
     * 创建验证器建造者
     *
     * @return 验证器建造者
     */
    @JvmStatic
    fun validator(): ValidatorBuilder {
        return ValidatorBuilder()
    }
}