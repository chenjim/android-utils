package com.chenjim.utils

import org.junit.Assert.*
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * SafeExecutor工具类的单元测试
 */
class SafeExecutorTest {

    @Test
    fun testExecuteWithoutException() {
        var executed = false
        
        SafeExecutor.execute("TEST", "basic execution") {
            executed = true
        }
        
        assertTrue("代码块应该被执行", executed)
    }

    @Test
    fun testExecuteWithException() {
        var executed = false
        var exceptionCaught = false
        
        SafeExecutor.execute("TEST", "test with exception") {
            executed = true
            throw RuntimeException("测试异常")
        }
        
        assertTrue("代码块应该被执行", executed)
        // SafeExecutor应该捕获异常，不让它传播出来
    }

    @Test
    fun testExecuteWithExceptionHandler() {
        var executed = false
        var exceptionHandled = false
        val testException = RuntimeException("测试异常")
        
        SafeExecutor.safe(
            block = {
                executed = true
                throw testException
            },
            onError = { exception ->
                exceptionHandled = true
                assertEquals("异常应该是我们抛出的异常", testException, exception)
            }
        )
        
        assertTrue("代码块应该被执行", executed)
        assertTrue("异常处理器应该被调用", exceptionHandled)
    }

    @Test
    fun testExecuteWithReturnValue() {
        val result = SafeExecutor.executeWithResult(
            "TEST",
            "test with return value",
            "默认值"
        ) {
            "测试结果"
        }
        
        assertEquals("应该返回正确的结果", "测试结果", result)
    }

    @Test
    fun testExecuteWithReturnValueAndException() {
        val defaultValue = "默认值"
        
        val result = SafeExecutor.executeWithResult(
            "TEST",
            "test with exception",
            defaultValue
        ) {
            throw RuntimeException("测试异常")
            "不应该返回这个值"
        }
        
        assertEquals("异常时应该返回默认值", defaultValue, result)
    }

    @Test
    fun testExecuteWithReturnValueAndExceptionHandler() {
        val defaultValue = "默认值"
        var exceptionHandled = false
        val testException = RuntimeException("测试异常")
        
        val result = SafeExecutor.safeCall(
            defaultValue = defaultValue,
            block = {
                throw testException
                "不应该返回这个值"
            }
        )
        
        assertEquals("异常时应该返回默认值", defaultValue, result)
    }

    @Test
    fun testSafeAsync() {
        var executed = false
        val future = SafeExecutor.safeAsync(
            block = {
                Thread.sleep(100)
                executed = true
            }
        )
        
        // 等待异步执行完成
        future.get()
        assertTrue("异步操作应该被执行", executed)
    }

    @Test
    fun testSafeAsyncWithException() {
        var errorHandled = false
        val future = SafeExecutor.safeAsync(
            block = { throw RuntimeException("测试异常") },
            onError = { errorHandled = true }
        )
        
        // 等待异步执行完成
        future.get()
        assertTrue("异步操作的异常应该被处理", errorHandled)
    }

    @Test
    fun testSafeDelay() {
        var executed = false
        val startTime = System.currentTimeMillis()
        
        val future = SafeExecutor.safeDelay(200, {
            executed = true
        })
        
        // 等待延迟执行完成
        future.get()
        val endTime = System.currentTimeMillis()
        
        assertTrue("延迟异步操作应该被执行", executed)
        assertTrue("延迟时间应该大于等于200ms", endTime - startTime >= 200)
    }

    @Test
    fun testSafeTimeout() {
        var executed = false
        
        val future = SafeExecutor.safeTimeout(500, {
            Thread.sleep(100) // 短暂睡眠，应该在超时前完成
            executed = true
        })
        
        val result = future.get()
        assertTrue("代码块应该被执行", executed)
        assertTrue("超时执行应该成功", result)
    }

    @Test
    fun testSafeTimeoutExceeded() {
        var executed = false
        
        val future = SafeExecutor.safeTimeout(100, {
            Thread.sleep(200) // 睡眠时间超过超时时间
            executed = true
        })
        
        val result = future.get()
        // 注意：由于超时，executed 可能为 false
        assertFalse("超时执行应该失败", result)
    }

    @Test
    fun testSafeRetry() {
        var attemptCount = 0
        
        val result = SafeExecutor.safeRetry(
            maxRetries = 3,
            retryDelayMs = 1000,
            block = {
                attemptCount++
                if (attemptCount < 3) {
                    throw RuntimeException("第 $attemptCount 次尝试失败")
                }
            }
        )
        
        assertEquals("应该尝试3次", 3, attemptCount)
        assertTrue("重试执行应该成功", result)
    }

    @Test
    fun testSafeRetryAllFailed() {
        var attemptCount = 0
        
        val result = SafeExecutor.safeRetry(
            maxRetries = 2,
            retryDelayMs = 1000,
            block = {
                attemptCount++
                throw RuntimeException("第 $attemptCount 次尝试失败")
            }
        )
        
        assertEquals("应该尝试3次（初始+2次重试）", 3, attemptCount)
        assertFalse("重试执行应该失败", result)
    }

    @Test
    fun testSafeRetryDelay() {
        var attemptCount = 0
        val startTime = System.currentTimeMillis()
        
        val result = SafeExecutor.safeRetry(
            maxRetries = 2,
            retryDelayMs = 100,
            block = {
                attemptCount++
                if (attemptCount < 2) {
                    throw RuntimeException("第 $attemptCount 次尝试失败")
                }
            }
        )
        
        val endTime = System.currentTimeMillis()
        val duration = endTime - startTime
        
        assertEquals("应该尝试2次", 2, attemptCount)
        assertTrue("重试执行应该成功", result)
        assertTrue("重试延迟应该至少100毫秒", duration >= 100)
    }

    @Test
    fun testNestedSafeExecution() {
        var outerExecuted = false
        var innerExecuted = false
        
        SafeExecutor.execute("TEST", "nested execution") {
            outerExecuted = true
            SafeExecutor.execute("TEST", "inner execution") {
                innerExecuted = true
                throw RuntimeException("内部异常")
            }
            // 外部执行应该继续
        }
        
        assertTrue("外部代码块应该被执行", outerExecuted)
        assertTrue("内部代码块应该被执行", innerExecuted)
    }

    @Test
    fun testConcurrentExecution() {
        val latch = CountDownLatch(10)
        val results = mutableListOf<Int>()
        
        repeat(10) { index ->
            SafeExecutor.safeAsync(
                block = {
                    synchronized(results) {
                        results.add(index)
                    }
                    latch.countDown()
                }
            )
        }
        
        assertTrue("所有并发任务应该在合理时间内完成", latch.await(2, TimeUnit.SECONDS))
        assertEquals("应该执行10个任务", 10, results.size)
    }
}