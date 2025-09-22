package com.chenjim.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.HandlerThread
import android.os.Process
import android.util.Log
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.min

/**
 * @author chenjim me@h89.cn
 * @description 可以打印当前的线程名，所在代码中的位置等信息
 * @date 2016/9/26.
 */
object Log {
    /**
     * Android's max limit for a log entry is ~4076 bytes,
     * so 4000 bytes is used as chunk size since default charset
     * is UTF-8
     */
    private const val CHUNK_SIZE = 4000


    /**
     * 日志输出级别
     *
     * [android.util.Log.ASSERT]>[android.util.Log.ERROR]>[android.util.Log.WARN]
     * >[android.util.Log.INFO]>[android.util.Log.DEBUG]>[android.util.Log.VERBOSE]
     */
    private var logLevel = Log.VERBOSE

    /**
     * 不进一步封装，为4
     * 若进一步封装，此值需要改变
     */
    private const val STACK_INDEX = 4

    /**
     * 单个文件限制大小
     */
    private var logFileMaxLen = 5 * 1024 * 1024L

    private var isDebug = false;

    var logDir: File? = null
        private set

    private const val CUR_LOG_NAME = "log1.txt"
    private const val LAST_LOG_NAME = "log2.txt"
    private val dateFormat by lazy { SimpleDateFormat("yyMMdd_HHmmss_SSS", Locale.CHINA) }

    private val logHandler: Handler

    private var PRE = "JG-"

    init {
        val handlerThread = HandlerThread("Logger")
        handlerThread.start()
        logHandler = Handler(handlerThread.getLooper())
    }

    /**
     * 获取配置构建器，支持链式调用
     * 使用方式: Log.config().setDebug(true).setLogFileMaxLen(1024*1024).setTagPre("MyApp").apply()
     */
    @JvmStatic
    fun config(): LogConfig {
        return LogConfig()
    }

    /**
     * 日志配置构建器类，支持链式调用
     */
    class LogConfig {
        private var context: Context? = null
        private var level: Int = Log.VERBOSE // 默认值
        private var debug: Boolean = false // 默认值
        private var maxLen: Long = 5 * 1024 * 1024L // 默认5M
        private var tagPre: String = "JG" // 默认前缀

        /**
         * 设置日志写入文件的上下文
         * 日志文件位置'/sdcard/Android/data/com.xxx.xxx/files/log/'
         */
        fun setLogToFile(context: Context): LogConfig {
            this.context = context
            return this
        }

        /**
         * 设置日志级别，默认 Log.VERBOSE
         */
        fun setLevel(level: Int = Log.VERBOSE): LogConfig {
            this.level = level
            return this
        }

        /**
         * 设置是否是Debug模式，默认 false
         */
        fun setDebug(debug: Boolean = false): LogConfig {
            this.debug = debug
            return this
        }

        /**
         * 设置单个日志文件最大大小，默认 5MB
         */
        fun setLogFileMaxLen(len: Long = 5 * 1024 * 1024L): LogConfig {
            this.maxLen = len
            return this
        }

        /**
         * 设置日志标签前缀，默认 "JG"
         */
        fun setTagPre(tagPre: String = "JG"): LogConfig {
            this.tagPre = tagPre
            return this
        }

        /**
         * 应用所有配置
         */
        fun apply() {
            // 设置日志级别
            logLevel = level
            
            // 设置Debug模式
            isDebug = debug
            
            // 设置文件大小限制
            logFileMaxLen = maxLen
            
            // 设置标签前缀
            PRE = tagPre.uppercase() + "-"
            
            // 设置日志文件目录
            context?.let {
                val path = File(it.getExternalFilesDir(null), "log")
                if (!path.exists()) {
                    path.mkdirs()
                }
                logDir = path
                d("Log configured - write to dir: ${logDir?.path}")
            }
        }
    }

    private fun objectToString(obj: Any?): String {
        return when (obj) {
            null -> "null"
            is Array<*> -> obj.contentDeepToString()
            is MutableList<*> -> obj.toTypedArray().contentDeepToString()
            else -> obj.toString()
        }
    }

    @JvmStatic
    fun d(obj: Any?) = log(Log.DEBUG, null, objectToString(obj))

    @JvmStatic
    fun d(tag: String?, message: String?) = log(Log.DEBUG, tag, message)

    @JvmStatic
    fun e(obj: Any?) = log(Log.ERROR, null, objectToString(obj))

    @JvmStatic
    fun e(tag: String?, message: String?) = log(Log.ERROR, tag, message)

    @JvmStatic
    fun e(tag: String?, e: Exception?) = log(Log.ERROR, tag, e?.toString() ?: "No message/exception is set")

    @JvmStatic
    fun e(tag: String?, message: String?, e: Exception) = log(Log.ERROR, tag, "$message: $e")

    @JvmStatic
    fun w(obj: Any?) = log(Log.WARN, null, objectToString(obj))

    @JvmStatic
    fun w(tag: String?, message: String?) = log(Log.WARN, tag, message)

    @JvmStatic
    fun i(obj: Any?) = log(Log.INFO, null, objectToString(obj))

    @JvmStatic
    fun i(tag: String?, message: String?) = log(Log.INFO, tag, message)

    @JvmStatic
    fun v(message: String?) = log(Log.VERBOSE, null, message)

    @JvmStatic
    fun v(tag: String?, message: String?) = log(Log.VERBOSE, tag, message)

    private fun log(logType: Int, tag: String?, message: String?) {
        if (logType < logLevel) return

        val curThread = Thread.currentThread()
        val element = curThread.getStackTrace()[STACK_INDEX]

        logHandler.post(Runnable { log(element, curThread, logType, tag, message) })
    }

    private fun log(
        element: StackTraceElement, thread: Thread, logType: Int, tag: String?, message: String?
    ) {
        val finalTag = PRE + (tag ?: "N")
        val builder = StringBuilder().apply {
            append("[").append(thread.id).append("],")
            if (isDebug) {
                append(thread.name).append(",(").append(element.fileName).append(":").append(element.lineNumber)
                    .append("),")
            }
        }
        val finalMessage = message ?: "null"
        val bytes = finalMessage.toByteArray()
        val length = bytes.size
        var i = 0
        while (i < length) {
            val count = min(length - i, CHUNK_SIZE)
            val content = String(bytes, i, count)
            logChunk(logType, finalTag, builder.toString() + content)
            i += CHUNK_SIZE
        }
    }

    private fun logChunk(logType: Int, tag: String?, chunk: String) {
        when (logType) {
            Log.ERROR -> Log.e(tag, chunk)
            Log.WARN -> Log.w(tag, chunk)
            Log.INFO -> Log.i(tag, chunk)
            Log.DEBUG -> Log.d(tag, chunk)
            Log.VERBOSE -> Log.v(tag, chunk)
            else -> {}
        }
        writeLogToFile(logType, tag, chunk)
    }

    @SuppressLint("SimpleDateFormat")
    @Synchronized
    private fun writeLogToFile(logType: Int, tag: String?, msg: String?) {
        logDir?.let {
            val data = StringBuilder().apply {
                append("[pid:").append(Process.myPid()).append("][")
                append(dateFormat.format(Date())).append(": ")
                append(getTypeString(logType)).append("/").append(tag).append("] ")
                append(msg).append("\r\n")
            }.toString()
            logHandler.post { doWriteDisk(data) }
        }
    }

    private fun doWriteDisk(msg: String?) {
        val curFile = File(logDir, CUR_LOG_NAME)
        val oldFile = File(logDir, LAST_LOG_NAME)
        if (curFile.length() > logFileMaxLen && !curFile.renameTo(oldFile)) {
            return
        }

        try {
            FileWriter(curFile, true).use { writer ->
                writer.write(msg)
                writer.flush()
            }
        } catch (exception: IOException) {
            exception.printStackTrace()
        }
    }

    private fun readLogFile(file: File, readLen: Long): StringBuilder {
        val data = StringBuilder()
        try {
            BufferedReader(FileReader(file)).use { reader ->
                val skip = if (file.length() > readLen) file.length() - readLen else 0
                reader.skip(skip)
                val length = if (readLen > file.length()) file.length() else readLen
                while (data.length < length) {
                    data.append(reader.readLine()).append("\r\n")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return data
    }


    /**
     * 读取最后一段logger
     */
    @JvmStatic
    fun getLastLogger(maxSize: Long): String {
        val data = StringBuilder()
        val curLogFile: File = File(logDir, CUR_LOG_NAME)
        if (!curLogFile.exists()) {
            return data.toString()
        }

        //是否需要读取上一个文件
        if (curLogFile.length() < maxSize) {
            val lastLogFile = File(logDir, LAST_LOG_NAME)
            val lastLogData = readLogFile(lastLogFile, maxSize - curLogFile.length())
            data.append(lastLogData)
        }

        val curLogData = readLogFile(curLogFile, maxSize)
        data.append(curLogData)
        return data.toString()
    }

    /**
     * @return 调用处的文件名+行数+函数名
     */
    @JvmStatic
    val stackTrace: String
        get() {
            val trace = Thread.currentThread().getStackTrace()
            val element = trace[STACK_INDEX - 1]
            val builder = StringBuilder()
            builder.append("(").append(element.fileName).append(":").append(element.lineNumber).append("),")
                .append(element.methodName).append("()")

            return builder.toString()
        }


    private fun getTypeString(logType: Int): String {
        return when (logType) {
            Log.ERROR -> "E"
            Log.WARN -> "W"
            Log.INFO -> "I"
            Log.DEBUG -> "D"
            Log.VERBOSE -> "V"
            else -> "D"
        }
    }

}