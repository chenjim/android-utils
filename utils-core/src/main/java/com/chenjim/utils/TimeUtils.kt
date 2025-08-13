package com.chenjim.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * @description：时间工具类
 * @fileName: TimeUtils
 * @author: jim.chen
 * @date: 2020/1/19
 */
object TimeUtils {

    /**
     * 时间戳转换成字符窜
     *
     * @param milSecond
     * @param pattern
     * @return
     */
    @JvmStatic
    fun getCurDate(milSecond: Long, pattern: String?): String =
        SimpleDateFormat(pattern, Locale.getDefault(Locale.Category.FORMAT)).format(Date(milSecond))

    /**
     * @return yyyy-MM-dd HH:mm:ss
     */
    @JvmStatic
    fun getCurDate(): String =
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault(Locale.Category.FORMAT)).format(Date())

    @JvmStatic
    fun getCurDate(format: String?): String =
        SimpleDateFormat(format, Locale.getDefault(Locale.Category.FORMAT)).format(Date())

    /**
     * @return "yyyy_MM_dd_HHmmss_SSS"
     */
    @JvmStatic
    fun getCurDate2(): String =
        SimpleDateFormat("yyyyMMdd_HHmmss_SSS", Locale.getDefault(Locale.Category.FORMAT)).format(Date())

    /**
     * @return "HH:mm:ss"
     */
    @JvmStatic
    fun getCurTime(): String = SimpleDateFormat("HH:mm:ss", Locale.getDefault(Locale.Category.FORMAT)).format(Date())

    @JvmStatic
    fun getCurTime(format: String?): String =
        SimpleDateFormat(format, Locale.getDefault(Locale.Category.FORMAT)).format(Date())

    /**
     * 下个月1号
     *
     * @return [Date]
     */
    @JvmStatic
    fun getNextMonthDate(): Date {
        val calendar = Calendar.getInstance()
        calendar[Calendar.DAY_OF_MONTH] = 1
        calendar.add(Calendar.MONTH, 1)
        return calendar.time
    }

    /**
     * 上个月1号
     *
     * @return [Date]
     */
    @JvmStatic
    fun getLastMonthDate(): Date {
        val calendar = Calendar.getInstance()
        calendar[Calendar.DAY_OF_MONTH] = 1
        calendar.add(Calendar.MONTH, -1)
        return calendar.time
    }

    @JvmStatic
    fun getMonthMaxDay(year: String, mouth: String): Int {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year.toInt())
        calendar[Calendar.MONTH] = mouth.toInt()
        return calendar.getActualMaximum(Calendar.DATE)
    }

    @JvmStatic
    fun getMonthMaxDay(year: Int, mouth: Int): Int {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar[Calendar.MONTH] = mouth
        return calendar.getActualMaximum(Calendar.DATE)
    }

    /**
     * 格式化时间戳为可读字符串
     *
     * @param timestamp 时间戳（毫秒）
     * @return 格式化后的时间字符串
     */
    @JvmStatic
    fun formatTimestamp(timestamp: Long): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date(timestamp))
    }

    /**
     * 获取当前时间戳（毫秒）
     *
     * @return 当前时间戳
     */
    @JvmStatic
    fun getCurrentTimestamp(): Long {
        return System.currentTimeMillis()
    }

    /**
     * 计算两个时间戳之间的差值（毫秒）
     *
     * @param startTime 开始时间戳
     * @param endTime 结束时间戳
     * @return 时间差（毫秒）
     */
    @JvmStatic
    fun getTimeDifference(startTime: Long, endTime: Long): Long {
        return endTime - startTime
    }

    /**
     * 判断是否为今天
     *
     * @param timestamp 时间戳
     * @return 是否为今天
     */
    @JvmStatic
    fun isToday(timestamp: Long): Boolean {
        val today = Calendar.getInstance()
        val target = Calendar.getInstance().apply { timeInMillis = timestamp }

        return today.get(Calendar.YEAR) == target.get(Calendar.YEAR) &&
                today.get(Calendar.DAY_OF_YEAR) == target.get(Calendar.DAY_OF_YEAR)
    }

    /**
     * 判断是否为昨天
     *
     * @param timestamp 时间戳
     * @return 是否为昨天
     */
    @JvmStatic
    fun isYesterday(timestamp: Long): Boolean {
        val yesterday = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -1)
        }
        val target = Calendar.getInstance().apply { timeInMillis = timestamp }

        return yesterday.get(Calendar.YEAR) == target.get(Calendar.YEAR) &&
                yesterday.get(Calendar.DAY_OF_YEAR) == target.get(Calendar.DAY_OF_YEAR)
    }

    /**
     * 判断是否为本周
     *
     * @param timestamp 时间戳
     * @return 是否为本周
     */
    @JvmStatic
    fun isThisWeek(timestamp: Long): Boolean {
        val today = Calendar.getInstance()
        val target = Calendar.getInstance().apply { timeInMillis = timestamp }

        return today.get(Calendar.YEAR) == target.get(Calendar.YEAR) &&
                today.get(Calendar.WEEK_OF_YEAR) == target.get(Calendar.WEEK_OF_YEAR)
    }

    /**
     * 判断是否为本月
     *
     * @param timestamp 时间戳
     * @return 是否为本月
     */
    @JvmStatic
    fun isThisMonth(timestamp: Long): Boolean {
        val today = Calendar.getInstance()
        val target = Calendar.getInstance().apply { timeInMillis = timestamp }

        return today.get(Calendar.YEAR) == target.get(Calendar.YEAR) &&
                today.get(Calendar.MONTH) == target.get(Calendar.MONTH)
    }

    /**
     * 判断是否为本年
     *
     * @param timestamp 时间戳
     * @return 是否为本年
     */
    @JvmStatic
    fun isThisYear(timestamp: Long): Boolean {
        val today = Calendar.getInstance()
        val target = Calendar.getInstance().apply { timeInMillis = timestamp }

        return today.get(Calendar.YEAR) == target.get(Calendar.YEAR)
    }

    /**
     * 获取友好的时间显示（如：刚刚、1分钟前、1小时前等）
     *
     * @param timestamp 时间戳
     * @return 友好的时间显示字符串
     */
    @JvmStatic
    fun getFriendlyTime(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp

        return when {
            diff < 0 -> "未来时间"
            diff < 60 * 1000 -> "刚刚"
            diff < 60 * 60 * 1000 -> "${diff / (60 * 1000)}分钟前"
            diff < 24 * 60 * 60 * 1000 -> "${diff / (60 * 60 * 1000)}小时前"
            diff < 7 * 24 * 60 * 60 * 1000 -> "${diff / (24 * 60 * 60 * 1000)}天前"
            diff < 30 * 24 * 60 * 60 * 1000 -> "${diff / (7 * 24 * 60 * 60 * 1000)}周前"
            diff < 365 * 24 * 60 * 60 * 1000 -> "${diff / (30 * 24 * 60 * 60 * 1000)}个月前"
            else -> "${diff / (365 * 24 * 60 * 60 * 1000)}年前"
        }
    }

    /**
     * 格式化持续时间（毫秒）为可读字符串
     *
     * @param durationMs 持续时间（毫秒）
     * @return 格式化后的字符串，如"1小时30分钟"
     */
    @JvmStatic
    fun formatDuration(durationMs: Long): String {
        val seconds = durationMs / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        return when {
            days > 0 -> "${days}天${hours % 24}小时${minutes % 60}分钟"
            hours > 0 -> "${hours}小时${minutes % 60}分钟"
            minutes > 0 -> "${minutes}分钟${seconds % 60}秒"
            else -> "${seconds}秒"
        }
    }

    /**
     * 获取指定日期的开始时间（00:00:00）
     *
     * @param timestamp 时间戳
     * @return 当天开始时间的时间戳
     */
    @JvmStatic
    fun getStartOfDay(timestamp: Long): Long {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = timestamp
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.timeInMillis
    }

    /**
     * 获取指定日期的结束时间（23:59:59.999）
     *
     * @param timestamp 时间戳
     * @return 当天结束时间的时间戳
     */
    @JvmStatic
    fun getEndOfDay(timestamp: Long): Long {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = timestamp
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }
        return calendar.timeInMillis
    }

    /**
     * 获取本周开始时间（周一00:00:00）
     *
     * @return 本周开始时间的时间戳
     */
    @JvmStatic
    fun getStartOfWeek(): Long {
        val calendar = Calendar.getInstance().apply {
            firstDayOfWeek = Calendar.MONDAY
            set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.timeInMillis
    }

    /**
     * 获取本月开始时间（1号00:00:00）
     *
     * @return 本月开始时间的时间戳
     */
    @JvmStatic
    fun getStartOfMonth(): Long {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.timeInMillis
    }

    /**
     * 获取本年开始时间（1月1日00:00:00）
     *
     * @return 本年开始时间的时间戳
     */
    @JvmStatic
    fun getStartOfYear(): Long {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.MONTH, Calendar.JANUARY)
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.timeInMillis
    }

    /**
     * 计算年龄
     *
     * @param birthTimestamp 出生日期时间戳
     * @return 年龄
     */
    @JvmStatic
    fun calculateAge(birthTimestamp: Long): Int {
        val birth = Calendar.getInstance().apply { timeInMillis = birthTimestamp }
        val now = Calendar.getInstance()
        
        var age = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR)
        
        if (now.get(Calendar.DAY_OF_YEAR) < birth.get(Calendar.DAY_OF_YEAR)) {
            age--
        }
        
        return age
    }

    /**
     * 添加指定天数
     *
     * @param timestamp 原始时间戳
     * @param days 要添加的天数（可以为负数）
     * @return 新的时间戳
     */
    @JvmStatic
    fun addDays(timestamp: Long, days: Int): Long {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = timestamp
            add(Calendar.DAY_OF_YEAR, days)
        }
        return calendar.timeInMillis
    }

    /**
     * 添加指定小时数
     *
     * @param timestamp 原始时间戳
     * @param hours 要添加的小时数（可以为负数）
     * @return 新的时间戳
     */
    @JvmStatic
    fun addHours(timestamp: Long, hours: Int): Long {
        return timestamp + hours * 60 * 60 * 1000L
    }

    /**
     * 添加指定分钟数
     *
     * @param timestamp 原始时间戳
     * @param minutes 要添加的分钟数（可以为负数）
     * @return 新的时间戳
     */
    @JvmStatic
    fun addMinutes(timestamp: Long, minutes: Int): Long {
        return timestamp + minutes * 60 * 1000L
    }

    /**
     * 判断是否为闰年
     *
     * @param year 年份
     * @return 是否为闰年
     */
    @JvmStatic
    fun isLeapYear(year: Int): Boolean {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
    }

    /**
     * 获取两个日期之间的天数差
     *
     * @param startTimestamp 开始时间戳
     * @param endTimestamp 结束时间戳
     * @return 天数差
     */
    @JvmStatic
    fun getDaysBetween(startTimestamp: Long, endTimestamp: Long): Int {
        val startDay = getStartOfDay(startTimestamp)
        val endDay = getStartOfDay(endTimestamp)
        return ((endDay - startDay) / (24 * 60 * 60 * 1000)).toInt()
    }
}