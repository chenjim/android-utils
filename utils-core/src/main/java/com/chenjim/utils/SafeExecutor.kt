package com.chenjim.utils

import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * 安全执行器工具类
 * 提供安全的代码执行方法，自动处理异常
 * 优化版本：移除Android依赖，增加异步执行、延迟执行、重试等功能
 */
object SafeExecutor {

    private val executor: ScheduledExecutorService = Executors.newScheduledThreadPool(
        Runtime.getRuntime().availableProcessors().coerceAtLeast(2)
    )

    /**
     * 安全执行代码块，捕获所有异常
     * @param block 要执行的代码块
     * @param onError 异常处理回调，可选
     */
    @JvmStatic
    inline fun safe(block: () -> Unit, noinline onError: ((Throwable) -> Unit)? = null) {
        try {
            block()
        } catch (e: Throwable) {
            onError?.invoke(e) ?: handleDefaultError(e)
        }
    }

    /**
     * 安全执行代码块并返回结果
     * @param defaultValue 异常时返回的默认值
     * @param block 要执行的代码块
     * @param onError 异常处理回调，可选
     * @return 执行结果或默认值
     */
    @JvmStatic
    inline fun <T> safeCall(defaultValue: T, block: () -> T, noinline onError: ((Throwable) -> Unit)? = null): T {
        return try {
            block()
        } catch (e: Throwable) {
            onError?.invoke(e) ?: handleDefaultError(e)
            defaultValue
        }
    }

    /**
     * 安全执行代码块并返回可空结果
     * @param block 要执行的代码块
     * @param onError 异常处理回调，可选
     * @return 执行结果或null
     */
    @JvmStatic
    inline fun <T> safeCallOrNull(block: () -> T, noinline onError: ((Throwable) -> Unit)? = null): T? {
        return try {
            block()
        } catch (e: Throwable) {
            onError?.invoke(e) ?: handleDefaultError(e)
            null
        }
    }

    /**
     * 在后台线程安全执行代码块
     * @param block 要执行的代码块
     * @param onError 异常处理回调，可选
     * @return Future对象
     */
    @JvmStatic
    fun safeAsync(block: () -> Unit, onError: ((Throwable) -> Unit)? = null): Future<*> {
        return executor.submit {
            safe(block, onError)
        }
    }

    /**
     * 在后台线程安全执行代码块并返回结果
     * @param block 要执行的代码块
     * @param onError 异常处理回调，可选
     * @return Future对象
     */
    @JvmStatic
    fun <T> safeAsyncCall(block: () -> T, onError: ((Throwable) -> Unit)? = null): Future<T?> {
        return executor.submit(Callable {
            safeCallOrNull(block, onError)
        })
    }

    /**
     * 延迟执行代码块
     * @param delayMs 延迟时间（毫秒）
     * @param block 要执行的代码块
     * @param onError 异常处理回调，可选
     * @return Future对象
     */
    @JvmStatic
    fun safeDelay(delayMs: Long, block: () -> Unit, onError: ((Throwable) -> Unit)? = null): Future<*> {
        return executor.schedule({
            safe(block, onError)
        }, delayMs, TimeUnit.MILLISECONDS)
    }

    /**
     * 重试执行代码块
     * @param maxRetries 最大重试次数
     * @param retryDelayMs 重试间隔（毫秒）
     * @param block 要执行的代码块
     * @param onError 异常处理回调，可选
     * @return 是否执行成功
     */
    @JvmStatic
    fun safeRetry(
        maxRetries: Int = 3,
        retryDelayMs: Long = 1000,
        block: () -> Unit,
        onError: ((Throwable, Int) -> Unit)? = null
    ): Boolean {
        repeat(maxRetries + 1) { attempt ->
            try {
                block()
                return true
            } catch (e: Throwable) {
                onError?.invoke(e, attempt) ?: handleDefaultError(e)
                if (attempt < maxRetries) {
                    try {
                        Thread.sleep(retryDelayMs)
                    } catch (ie: InterruptedException) {
                        Thread.currentThread().interrupt()
                        return false
                    }
                }
            }
        }
        return false
    }

    /**
     * 带超时的安全执行
     * @param timeoutMs 超时时间（毫秒）
     * @param block 要执行的代码块
     * @param onError 异常处理回调，可选
     * @param onTimeout 超时处理回调，可选
     * @return Future<Boolean> 是否在超时前完成
     */
    fun safeTimeout(
        timeoutMs: Long,
        block: () -> Unit,
        onError: ((Throwable) -> Unit)? = null,
        onTimeout: (() -> Unit)? = null
    ): Future<Boolean> {
        return executor.submit(Callable {
            val future = executor.submit {
                safe(block, onError)
            }

            try {
                future.get(timeoutMs, TimeUnit.MILLISECONDS)
                true
            } catch (e: TimeoutException) {
                future.cancel(true)
                onTimeout?.invoke()
                false
            } catch (e: Throwable) {
                onError?.invoke(e) ?: handleDefaultError(e)
                false
            }
        })
    }

    /**
     * 兼容原有API - 安全执行操作
     * @param tag 日志标签
     * @param operation 操作描述
     * @param action 要执行的操作
     */
    @JvmStatic
    inline fun execute(tag: String, operation: String, action: () -> Unit) {
        safe(action) { e ->
            println("[$tag] Error during $operation: ${e.message}")
        }
    }

    /**
     * 兼容原有API - 安全执行操作并返回结果
     * @param tag 日志标签
     * @param operation 操作描述
     * @param defaultValue 异常时的默认返回值
     * @param action 要执行的操作
     * @return 操作结果或默认值
     */
    @JvmStatic
    inline fun <T> executeWithResult(tag: String, operation: String, defaultValue: T, action: () -> T): T {
        return safeCall(defaultValue, action) { e ->
            println("[$tag] Error during $operation: ${e.message}")
        }
    }

    /**
     * 默认错误处理
     */
    @JvmStatic
    fun handleDefaultError(e: Throwable) {
        println("SafeExecutor: Unhandled error - ${e.message}")
        e.printStackTrace()
    }

    /**
     * 带超时的执行
     *
     * @param timeoutMs 超时时间（毫秒）
     * @param action 要执行的操作
     * @param onTimeout 超时回调
     * @return 是否在超时前完成
     */
    @JvmStatic
    fun executeWithTimeout(
        timeoutMs: Long,
        action: () -> Unit,
        onTimeout: (() -> Unit)? = null
    ): Boolean {
        return try {
            val future = executor.submit {
                action()
            }
            
            future.get(timeoutMs, TimeUnit.MILLISECONDS)
            true
        } catch (e: TimeoutException) {
            onTimeout?.invoke()
            false
        } catch (e: Exception) {
            handleDefaultError(e)
            false
        }
    }

    /**
     * 带返回值的异步执行
     *
     * @param action 要执行的操作
     * @param onSuccess 成功回调
     * @param onError 错误回调
     */
    @JvmStatic
    fun <T> executeAsyncWithResult(
        action: () -> T,
        onSuccess: ((T) -> Unit)? = null,
        onError: ((Exception) -> Unit)? = null
    ) {
        executor.submit {
            try {
                val result = action()
                onSuccess?.invoke(result)
            } catch (e: Exception) {
                handleDefaultError(e)
                onError?.invoke(e)
            }
        }
    }

    /**
     * 条件执行
     *
     * @param condition 执行条件
     * @param action 要执行的操作
     * @param onConditionFalse 条件不满足时的回调
     */
    @JvmStatic
    fun executeIf(
        condition: () -> Boolean,
        action: () -> Unit,
        onConditionFalse: (() -> Unit)? = null
    ) {
        try {
            if (condition()) {
                action()
            } else {
                onConditionFalse?.invoke()
            }
        } catch (e: Exception) {
            handleDefaultError(e)
        }
    }

    /**
     * 批量执行
     *
     * @param actions 要执行的操作列表
     * @param stopOnError 遇到错误是否停止
     * @param onProgress 进度回调
     * @return 成功执行的数量
     */
    @JvmStatic
    fun executeBatch(
        actions: List<() -> Unit>,
        stopOnError: Boolean = false,
        onProgress: ((Int, Int) -> Unit)? = null
    ): Int {
        var successCount = 0
        
        for (i in actions.indices) {
            try {
                actions[i]()
                successCount++
                onProgress?.invoke(i + 1, actions.size)
            } catch (e: Exception) {
                handleDefaultError(e)
                if (stopOnError) break
            }
        }
        
        return successCount
    }

    /**
     * 并行执行
     *
     * @param actions 要执行的操作列表
     * @param onComplete 完成回调
     */
    fun executeParallel(
        actions: List<() -> Unit>,
        onComplete: ((Int) -> Unit)? = null
    ) {
        val futures = actions.map { action ->
            executor.submit {
                try {
                    action()
                    true
                } catch (e: Exception) {
                    handleDefaultError(e)
                    false
                }
            }
        }
        
        executor.submit {
            val successCount = futures.count { future ->
                try {
                    future.get() as Boolean
                } catch (e: Exception) {
                    false
                }
            }
            onComplete?.invoke(successCount)
        }
    }

    /**
     * 循环执行直到条件满足
     *
     * @param condition 停止条件
     * @param action 要执行的操作
     * @param intervalMs 执行间隔（毫秒）
     * @param maxAttempts 最大尝试次数
     * @return 是否成功满足条件
     */
    fun executeUntil(
        condition: () -> Boolean,
        action: () -> Unit,
        intervalMs: Long = 1000,
        maxAttempts: Int = 10
    ): Boolean {
        repeat(maxAttempts) {
            try {
                action()
                if (condition()) {
                    return true
                }
                Thread.sleep(intervalMs)
            } catch (e: Exception) {
                handleDefaultError(e)
            }
        }
        return false
    }

    /**
     * 定时执行
     *
     * @param intervalMs 执行间隔（毫秒）
     * @param action 要执行的操作
     * @return 可用于取消的Future
     */
    fun executeScheduled(
        intervalMs: Long,
        action: () -> Unit
    ): Future<*> {
        return executor.scheduleAtFixedRate({
            try {
                action()
            } catch (e: Exception) {
                handleDefaultError(e)
            }
        }, 0, intervalMs, TimeUnit.MILLISECONDS)
    }

    /**
     * 链式执行
     *
     * @param actions 要按顺序执行的操作列表
     * @param onComplete 完成回调
     * @param onError 错误回调
     */
    fun executeChain(
        actions: List<() -> Unit>,
        onComplete: (() -> Unit)? = null,
        onError: ((Exception) -> Unit)? = null
    ) {
        executor.submit {
            try {
                actions.forEach { it() }
                onComplete?.invoke()
            } catch (e: Exception) {
                handleDefaultError(e)
                onError?.invoke(e)
            }
        }
    }

    /**
     * 关闭执行器
     */
    @JvmStatic
    fun shutdown() {
        executor.shutdown()
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow()
            }
        } catch (e: InterruptedException) {
            executor.shutdownNow()
            Thread.currentThread().interrupt()
        }
    }
}