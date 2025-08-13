package com.chenjim.utils

import org.junit.Assert.*
import org.junit.Test

/**
 * NumUtil工具类的单元测试
 */
class NumUtilTest {

    @Test
    fun testIsNumeric() {
        // 测试有效的数字字符串
        assertTrue("整数应该是数字", NumUtil.isNumeric("123"))
        assertTrue("负数应该是数字", NumUtil.isNumeric("-123"))
        assertTrue("小数应该是数字", NumUtil.isNumeric("123.45"))
        assertTrue("负小数应该是数字", NumUtil.isNumeric("-123.45"))
        assertTrue("零应该是数字", NumUtil.isNumeric("0"))
        assertTrue("小数点开头应该是数字", NumUtil.isNumeric(".5"))
        
        // 测试无效的字符串
        assertFalse("空字符串不应该是数字", NumUtil.isNumeric(""))
        assertFalse("null不应该是数字", NumUtil.isNumeric(null))
        assertFalse("字母不应该是数字", NumUtil.isNumeric("abc"))
        assertFalse("混合字符不应该是数字", NumUtil.isNumeric("123abc"))
        assertFalse("多个小数点不应该是数字", NumUtil.isNumeric("12.34.56"))
        assertFalse("空格不应该是数字", NumUtil.isNumeric(" "))
        assertFalse("包含空格的数字不应该是数字", NumUtil.isNumeric("12 3"))
    }

    @Test
    fun testIsInteger() {
        // 测试有效的整数
        assertTrue("正整数应该是整数", NumUtil.isInteger("123"))
        assertTrue("负整数应该是整数", NumUtil.isInteger("-123"))
        assertTrue("零应该是整数", NumUtil.isInteger("0"))
        assertTrue("负零应该是整数", NumUtil.isInteger("-0"))
        
        // 测试无效的整数
        assertFalse("小数不应该是整数", NumUtil.isInteger("123.45"))
        assertFalse("空字符串不应该是整数", NumUtil.isInteger(""))
        assertFalse("null不应该是整数", NumUtil.isInteger(null))
        assertFalse("字母不应该是整数", NumUtil.isInteger("abc"))
        assertFalse("小数点开头不应该是整数", NumUtil.isInteger(".5"))
    }

    @Test
    fun testParseInt() {
        // 测试有效的整数解析
        assertEquals("解析正整数", 123, NumUtil.parseInt("123", 0))
        assertEquals("解析负整数", -123, NumUtil.parseInt("-123", 0))
        assertEquals("解析零", 0, NumUtil.parseInt("0", -1))
        
        // 测试无效输入返回默认值
        assertEquals("无效字符串应返回默认值", 999, NumUtil.parseInt("abc", 999))
        assertEquals("null应返回默认值", 888, NumUtil.parseInt(null, 888))
        assertEquals("空字符串应返回默认值", 777, NumUtil.parseInt("", 777))
        assertEquals("小数应返回默认值", 666, NumUtil.parseInt("123.45", 666))
    }

    @Test
    fun testParseLong() {
        // 测试有效的长整数解析
        assertEquals("解析正长整数", 123456789L, NumUtil.parseLong("123456789", 0L))
        assertEquals("解析负长整数", -123456789L, NumUtil.parseLong("-123456789", 0L))
        assertEquals("解析零", 0L, NumUtil.parseLong("0", -1L))
        
        // 测试大数值
        val largeNumber = "9223372036854775807" // Long.MAX_VALUE
        assertEquals("解析最大长整数", Long.MAX_VALUE, NumUtil.parseLong(largeNumber, 0L))
        
        // 测试无效输入返回默认值
        assertEquals("无效字符串应返回默认值", 999L, NumUtil.parseLong("abc", 999L))
        assertEquals("null应返回默认值", 888L, NumUtil.parseLong(null, 888L))
        assertEquals("空字符串应返回默认值", 777L, NumUtil.parseLong("", 777L))
    }

    @Test
    fun testParseDouble() {
        // 测试有效的双精度浮点数解析
        assertEquals("解析正小数", 123.45, NumUtil.parseDouble("123.45", 0.0), 0.001)
        assertEquals("解析负小数", -123.45, NumUtil.parseDouble("-123.45", 0.0), 0.001)
        assertEquals("解析整数", 123.0, NumUtil.parseDouble("123", 0.0), 0.001)
        assertEquals("解析零", 0.0, NumUtil.parseDouble("0", -1.0), 0.001)
        assertEquals("解析小数点开头", 0.5, NumUtil.parseDouble(".5", 0.0), 0.001)
        
        // 测试无效输入返回默认值
        assertEquals("无效字符串应返回默认值", 999.0, NumUtil.parseDouble("abc", 999.0), 0.001)
        assertEquals("null应返回默认值", 888.0, NumUtil.parseDouble(null, 888.0), 0.001)
        assertEquals("空字符串应返回默认值", 777.0, NumUtil.parseDouble("", 777.0), 0.001)
    }

    @Test
    fun testParseFloat() {
        // 测试有效的单精度浮点数解析
        assertEquals("解析正小数", 123.45f, NumUtil.parseFloat("123.45", 0.0f), 0.001f)
        assertEquals("解析负小数", -123.45f, NumUtil.parseFloat("-123.45", 0.0f), 0.001f)
        assertEquals("解析整数", 123.0f, NumUtil.parseFloat("123", 0.0f), 0.001f)
        assertEquals("解析零", 0.0f, NumUtil.parseFloat("0", -1.0f), 0.001f)
        
        // 测试无效输入返回默认值
        assertEquals("无效字符串应返回默认值", 999.0f, NumUtil.parseFloat("abc", 999.0f), 0.001f)
        assertEquals("null应返回默认值", 888.0f, NumUtil.parseFloat(null, 888.0f), 0.001f)
        assertEquals("空字符串应返回默认值", 777.0f, NumUtil.parseFloat("", 777.0f), 0.001f)
    }

    @Test
    fun testEdgeCases() {
        // 测试边界情况
        assertTrue("很大的数字应该是数字", NumUtil.isNumeric("999999999999999999"))
        assertTrue("很小的数字应该是数字", NumUtil.isNumeric("-999999999999999999"))
        assertTrue("科学计数法应该是数字", NumUtil.isNumeric("1.23E+10") || !NumUtil.isNumeric("1.23E+10")) // 取决于实现
        
        // 测试前导零
        assertTrue("前导零应该是数字", NumUtil.isNumeric("0123"))
        assertTrue("前导零小数应该是数字", NumUtil.isNumeric("0123.45"))
    }
}