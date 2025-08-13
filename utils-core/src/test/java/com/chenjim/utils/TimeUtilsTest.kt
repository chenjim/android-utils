package com.chenjim.utils

import org.junit.Assert.*
import org.junit.Test
import java.util.*

/**
 * TimeUtils工具类的单元测试
 */
class TimeUtilsTest {

    @Test
    fun testGetCurDate() {
        val currentTime = TimeUtils.getCurDate()
        assertNotNull("当前时间字符串不应为null", currentTime)
        assertTrue("当前时间字符串应不为空", currentTime.isNotEmpty())
        
        // 检查格式是否正确（默认格式应该是 yyyy-MM-dd HH:mm:ss）
        assertTrue("时间格式应正确", currentTime.matches(Regex("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")))
    }

    @Test
    fun testGetCurDateWithFormat() {
        val format = "yyyy/MM/dd"
        val currentTime = TimeUtils.getCurDate(format)
        assertNotNull("指定格式的当前时间字符串不应为null", currentTime)
        assertTrue("指定格式的时间字符串应不为空", currentTime.isNotEmpty())
        
        // 检查格式是否正确
        assertTrue("时间格式应正确", currentTime.matches(Regex("\\d{4}/\\d{2}/\\d{2}")))
    }

    @Test
    fun testGetCurDateWithTimestamp() {
        val timestamp = 1609459200000L // 2021-01-01 00:00:00 UTC
        val format = "yyyy-MM-dd HH:mm:ss"
        
        val formattedTime = TimeUtils.getCurDate(timestamp, format)
        assertNotNull("格式化时间不应为null", formattedTime)
        assertTrue("格式化时间应不为空", formattedTime.isNotEmpty())
        
        // 由于时区差异，我们只检查年份
        assertTrue("应包含正确的年份", formattedTime.contains("2021") || formattedTime.contains("2020"))
    }

    @Test
    fun testFormatTimestamp() {
        val timestamp = System.currentTimeMillis()
        val formattedTime = TimeUtils.formatTimestamp(timestamp)
        
        assertNotNull("默认格式化时间不应为null", formattedTime)
        assertTrue("默认格式化时间应不为空", formattedTime.isNotEmpty())
        assertTrue("默认格式应正确", formattedTime.matches(Regex("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")))
    }

    @Test
    fun testGetCurrentTimestamp() {
        val timestamp = TimeUtils.getCurrentTimestamp()
        assertTrue("当前时间戳应大于0", timestamp > 0)
        
        // 验证时间戳是否合理（应该接近当前时间）
        val currentTime = System.currentTimeMillis()
        val difference = Math.abs(currentTime - timestamp)
        assertTrue("时间戳应该接近当前时间", difference < 1000) // 1秒内的差异
    }

    @Test
    fun testGetCurTime() {
        val currentTime = TimeUtils.getCurTime()
        assertNotNull("当前时间不应为null", currentTime)
        assertTrue("当前时间应不为空", currentTime.isNotEmpty())
        
        // 检查格式是否正确（HH:mm:ss）
        assertTrue("时间格式应正确", currentTime.matches(Regex("\\d{2}:\\d{2}:\\d{2}")))
    }

    @Test
    fun testGetCurTimeWithFormat() {
        val format = "HH-mm-ss"
        val currentTime = TimeUtils.getCurTime(format)
        assertNotNull("指定格式的当前时间不应为null", currentTime)
        assertTrue("指定格式的时间应不为空", currentTime.isNotEmpty())
        
        // 检查格式是否正确
        assertTrue("时间格式应正确", currentTime.matches(Regex("\\d{2}-\\d{2}-\\d{2}")))
    }

    @Test
    fun testGetTimeDifference() {
        val startTime = System.currentTimeMillis()
        Thread.sleep(100) // 等待100毫秒
        val endTime = System.currentTimeMillis()
        
        val difference = TimeUtils.getTimeDifference(startTime, endTime)
        assertTrue("时间差应大于等于100毫秒", difference >= 100)
        assertTrue("时间差应小于200毫秒", difference < 200)
    }

    @Test
    fun testGetTimeDifferenceNegative() {
        val futureTime = System.currentTimeMillis() + 1000
        val currentTime = System.currentTimeMillis()
        
        val difference = TimeUtils.getTimeDifference(futureTime, currentTime)
        assertTrue("未来时间到当前时间的差值应为负数", difference < 0)
    }

    @Test
    fun testIsToday() {
        val now = System.currentTimeMillis()
        assertTrue("当前时间应该是今天", TimeUtils.isToday(now))
        
        val yesterday = now - 24 * 60 * 60 * 1000L
        assertFalse("昨天不应该是今天", TimeUtils.isToday(yesterday))
        
        val tomorrow = now + 24 * 60 * 60 * 1000L
        assertFalse("明天不应该是今天", TimeUtils.isToday(tomorrow))
    }

    @Test
    fun testIsYesterday() {
        val yesterday = System.currentTimeMillis() - 24 * 60 * 60 * 1000L
        assertTrue("昨天应该被识别为昨天", TimeUtils.isYesterday(yesterday))
        
        val now = System.currentTimeMillis()
        assertFalse("今天不应该被识别为昨天", TimeUtils.isYesterday(now))
        
        val tomorrow = System.currentTimeMillis() + 24 * 60 * 60 * 1000L
        assertFalse("明天不应该被识别为昨天", TimeUtils.isYesterday(tomorrow))
    }

    @Test
    fun testGetCurDate2() {
        val dateString = TimeUtils.getCurDate2()
        assertNotNull("getCurDate2结果不应为null", dateString)
        assertTrue("getCurDate2结果应不为空", dateString.isNotEmpty())
        
        // 检查格式是否正确（yyyyMMdd_HHmmss_SSS）
        assertTrue("日期格式应正确", dateString.matches(Regex("\\d{8}_\\d{6}_\\d{3}")))
    }

    @Test
    fun testGetMonthMaxDay() {
        // 测试2月的最大天数（非闰年）
        val feb2021 = TimeUtils.getMonthMaxDay(2021, 1) // 月份从0开始
        assertEquals("2021年2月应该有28天", 28, feb2021)
        
        // 测试闰年2月
        val feb2020 = TimeUtils.getMonthMaxDay(2020, 1)
        assertEquals("2020年2月应该有29天", 29, feb2020)
        
        // 测试字符串版本
        val feb2021Str = TimeUtils.getMonthMaxDay("2021", "1")
        assertEquals("字符串版本2021年2月应该有28天", 28, feb2021Str)
    }

    @Test
    fun testGetNextMonthDate() {
        val nextMonth = TimeUtils.getNextMonthDate()
        assertNotNull("下个月日期不应为null", nextMonth)
        
        val calendar = Calendar.getInstance()
        calendar.time = nextMonth
        assertEquals("下个月日期应该是1号", 1, calendar.get(Calendar.DAY_OF_MONTH))
        
        // 验证是下个月
        val currentCalendar = Calendar.getInstance()
        val expectedMonth = if (currentCalendar.get(Calendar.MONTH) == 11) 0 else currentCalendar.get(Calendar.MONTH) + 1
        val expectedYear = if (currentCalendar.get(Calendar.MONTH) == 11) currentCalendar.get(Calendar.YEAR) + 1 else currentCalendar.get(Calendar.YEAR)
        
        assertEquals("应该是下个月", expectedMonth, calendar.get(Calendar.MONTH))
        assertEquals("年份应该正确", expectedYear, calendar.get(Calendar.YEAR))
    }

    @Test
    fun testGetLastMonthDate() {
        val lastMonth = TimeUtils.getLastMonthDate()
        assertNotNull("上个月日期不应为null", lastMonth)
        
        val calendar = Calendar.getInstance()
        calendar.time = lastMonth
        assertEquals("上个月日期应该是1号", 1, calendar.get(Calendar.DAY_OF_MONTH))
        
        // 验证是上个月
        val currentCalendar = Calendar.getInstance()
        val expectedMonth = if (currentCalendar.get(Calendar.MONTH) == 0) 11 else currentCalendar.get(Calendar.MONTH) - 1
        val expectedYear = if (currentCalendar.get(Calendar.MONTH) == 0) currentCalendar.get(Calendar.YEAR) - 1 else currentCalendar.get(Calendar.YEAR)
        
        assertEquals("应该是上个月", expectedMonth, calendar.get(Calendar.MONTH))
        assertEquals("年份应该正确", expectedYear, calendar.get(Calendar.YEAR))
    }

    @Test
    fun testDifferentFormats() {
        val timestamp = System.currentTimeMillis()
        
        // 测试不同的时间格式
        val formats = arrayOf(
            "yyyy-MM-dd",
            "MM/dd/yyyy",
            "dd-MM-yyyy HH:mm",
            "yyyy年MM月dd日",
            "HH:mm:ss"
        )
        
        for (format in formats) {
            val formatted = TimeUtils.getCurDate(timestamp, format)
            assertNotNull("格式 $format 不应返回null", formatted)
            assertTrue("格式 $format 应不为空", formatted.isNotEmpty())
        }
    }
}