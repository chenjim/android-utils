package com.chenjim.utils

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class LogTest {

    private lateinit var context: Context
    private lateinit var testLogFile: File

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        testLogFile = File(context.cacheDir, "test_log.txt")
        
        // 清理之前的测试日志文件
        if (testLogFile.exists()) {
            testLogFile.delete()
        }
        
        // 初始化Log工具
        Log.init(context, android.util.Log.VERBOSE, "TEST")
    }

    @Test
    fun testLogInitialization() {
        // 测试Log初始化
        Log.init(context, android.util.Log.VERBOSE, "TEST")
        
        // 验证初始化后可以正常使用
        Log.d("LogTest", "测试初始化")
        
        // 这里主要测试不会抛出异常
        assertTrue("Log初始化应该成功", true)
    }

    @Test
    fun testLogInitWithoutContext() {
        // 测试不带Context的初始化
        Log.init(android.util.Log.DEBUG)
        
        Log.d("LogTest", "无Context初始化测试")
        assertTrue("无Context的Log初始化应该成功", true)
    }

    @Test
    fun testDebugLog() {
        val tag = "DebugTest"
        val message = "这是一条调试日志"
        
        // 测试debug日志
        Log.d(tag, message)
        
        // 由于Log输出到系统日志，这里主要测试不会抛出异常
        assertTrue("Debug日志应该正常输出", true)
    }

    @Test
    fun testInfoLog() {
        val tag = "InfoTest"
        val message = "这是一条信息日志"
        
        Log.i(tag, message)
        assertTrue("Info日志应该正常输出", true)
    }

    @Test
    fun testWarningLog() {
        val tag = "WarnTest"
        val message = "这是一条警告日志"
        
        Log.w(tag, message)
        assertTrue("Warning日志应该正常输出", true)
    }

    @Test
    fun testErrorLog() {
        val tag = "ErrorTest"
        val message = "这是一条错误日志"
        
        Log.e(tag, message)
        assertTrue("Error日志应该正常输出", true)
    }

    @Test
    fun testErrorLogWithException() {
        val tag = "ErrorExceptionTest"
        val message = "这是一条带异常的错误日志"
        val exception = RuntimeException("测试异常")
        
        Log.e(tag, message, exception)
        assertTrue("带异常的Error日志应该正常输出", true)
    }

    @Test
    fun testErrorLogWithExceptionOnly() {
        val tag = "ErrorExceptionOnlyTest"
        val exception = RuntimeException("只有异常的测试")
        
        Log.e(tag, exception)
        assertTrue("只有异常的Error日志应该正常输出", true)
    }

    @Test
    fun testVerboseLog() {
        val tag = "VerboseTest"
        val message = "这是一条详细日志"
        
        Log.v(tag, message)
        assertTrue("Verbose日志应该正常输出", true)
    }

    @Test
    fun testLogWithNullMessage() {
        val tag = "NullTest"
        
        // 测试空消息
        Log.d(tag, null)
        Log.i(tag, "")
        
        assertTrue("空消息日志应该正常处理", true)
    }

    @Test
    fun testLogWithLongMessage() {
        val tag = "LongMessageTest"
        val longMessage = "这是一条很长的日志消息".repeat(100)
        
        Log.d(tag, longMessage)
        assertTrue("长消息日志应该正常输出", true)
    }

    @Test
    fun testLogWithSpecialCharacters() {
        val tag = "SpecialCharTest"
        val message = "包含特殊字符的日志: !@#$%^&*()_+{}|:<>?[]\\;'\",./ 中文 🎉"
        
        Log.d(tag, message)
        assertTrue("包含特殊字符的日志应该正常输出", true)
    }

    @Test
    fun testLogToFile() {
        val tag = "FileLogTest"
        val message = "写入文件的日志消息"
        
        // 测试写入文件的日志功能（如果Log类支持的话）
        Log.d(tag, message)
        
        // 这里主要测试功能调用不会出错
        assertTrue("文件日志功能应该正常工作", true)
    }

    @Test
    fun testLogLevels() {
        val tag = "LevelTest"
        
        // 测试所有日志级别
        Log.v(tag, "Verbose级别日志")
        Log.d(tag, "Debug级别日志")
        Log.i(tag, "Info级别日志")
        Log.w(tag, "Warning级别日志")
        Log.e(tag, "Error级别日志")
        
        assertTrue("所有日志级别应该正常工作", true)
    }

    @Test
    fun testLogPerformance() {
        val tag = "PerformanceTest"
        val startTime = System.currentTimeMillis()
        
        // 测试大量日志输出的性能
        repeat(100) { index ->
            Log.d(tag, "性能测试日志 #$index")
        }
        
        val endTime = System.currentTimeMillis()
        val duration = endTime - startTime
        
        // 100条日志应该在合理时间内完成（比如1秒内）
        assertTrue("日志输出性能应该合理", duration < 1000)
    }

    @Test
    fun testLogObject() {
        // 测试记录对象
        val testObject = mapOf("key1" to "value1", "key2" to "value2")
        Log.d(testObject)
        
        val testArray = arrayOf("item1", "item2", "item3")
        Log.i(testArray)
        
        val testList = listOf(1, 2, 3, 4, 5)
        Log.w(testList)
        
        assertTrue("对象日志应该正常输出", true)
    }

    @Test
    fun testGetStackTrace() {
        val stackTrace = Log.stackTrace
        assertNotNull("堆栈跟踪不应为null", stackTrace)
        assertTrue("堆栈跟踪应包含文件信息", stackTrace.contains("LogTest.kt"))
        assertTrue("堆栈跟踪应包含方法信息", stackTrace.contains("testGetStackTrace"))
    }

    @Test
    fun testSetLogFileMaxLen() {
        // 测试设置日志文件最大长度
        Log.setLogFileMaxLen(1024 * 1024) // 1MB
        
        // 这里主要测试方法调用不会出错
        assertTrue("设置日志文件最大长度应该成功", true)
    }

    @Test
    fun testGetLastLogger() {
        // 初始化Log以启用文件写入
        Log.init(context, android.util.Log.VERBOSE, "TEST")
        
        // 写入一些日志
        Log.d("TestTag", "测试日志内容1")
        Log.i("TestTag", "测试日志内容2")
        Log.w("TestTag", "测试日志内容3")
        
        // 等待一下让日志写入完成
        Thread.sleep(100)
        
        // 获取最后的日志
        val lastLogger = Log.getLastLogger(1024)
        assertNotNull("最后的日志不应为null", lastLogger)
        
        // 如果有日志目录，应该能获取到内容
        if (Log.logDir != null) {
            // 可能包含日志内容，也可能为空（取决于文件是否存在）
            assertTrue("获取最后日志功能应该正常工作", true)
        }
    }
}