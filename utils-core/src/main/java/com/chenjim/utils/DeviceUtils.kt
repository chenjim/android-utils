package com.chenjim.utils

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.DisplayMetrics
import android.view.WindowManager
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.util.Locale
import java.util.UUID

/**
 * 设备信息工具类
 * 提供设备硬件信息、系统信息等功能
 *
 * @author chenjim
 * @date 2024/01/01
 */
object DeviceUtils {

    /**
     * 获取设备型号
     *
     * @return 设备型号
     */
    @JvmStatic
    fun getDeviceModel(): String {
        return Build.MODEL ?: "Unknown"
    }

    /**
     * 获取设备品牌
     *
     * @return 设备品牌
     */
    @JvmStatic
    fun getDeviceBrand(): String {
        return Build.BRAND ?: "Unknown"
    }

    /**
     * 获取设备制造商
     *
     * @return 设备制造商
     */
    @JvmStatic
    fun getDeviceManufacturer(): String {
        return Build.MANUFACTURER ?: "Unknown"
    }

    /**
     * 获取Android版本号
     *
     * @return Android版本号
     */
    @JvmStatic
    fun getAndroidVersion(): String {
        return Build.VERSION.RELEASE ?: "Unknown"
    }

    /**
     * 获取Android API级别
     *
     * @return API级别
     */
    @JvmStatic
    fun getAndroidSDKVersion(): Int {
        return Build.VERSION.SDK_INT
    }

    /**
     * 获取设备唯一标识符
     * 注意：Android 8.0+可能返回固定值
     *
     * @param context 上下文
     * @return 设备ID
     */
    @SuppressLint("HardwareIds")
    @JvmStatic
    fun getDeviceId(context: Context): String {
        return try {
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
                ?: UUID.randomUUID().toString()
        } catch (e: Exception) {
            UUID.randomUUID().toString()
        }
    }

    /**
     * 获取IMEI（需要权限）
     * 注意：Android 10+已废弃
     *
     * @param context 上下文
     * @return IMEI
     */
    @SuppressLint("HardwareIds")
    @JvmStatic
    fun getIMEI(context: Context): String? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Android 10+ 不再支持获取IMEI
                null
            } else {
                val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
                @Suppress("DEPRECATION")
                telephonyManager?.deviceId
            }
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 获取序列号
     *
     * @return 序列号
     */
    @SuppressLint("HardwareIds")
    @JvmStatic
    fun getSerialNumber(): String? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Build.getSerial()
            } else {
                @Suppress("DEPRECATION")
                Build.SERIAL
            }
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 获取屏幕宽度（像素）
     *
     * @param context 上下文
     * @return 屏幕宽度
     */
    @JvmStatic
    fun getScreenWidth(context: Context): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }

    /**
     * 获取屏幕高度（像素）
     *
     * @param context 上下文
     * @return 屏幕高度
     */
    @JvmStatic
    fun getScreenHeight(context: Context): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    /**
     * 获取屏幕密度
     *
     * @param context 上下文
     * @return 屏幕密度
     */
    @JvmStatic
    fun getScreenDensity(context: Context): Float {
        val displayMetrics = context.resources.displayMetrics
        return displayMetrics.density
    }

    /**
     * 获取屏幕DPI
     *
     * @param context 上下文
     * @return 屏幕DPI
     */
    @JvmStatic
    fun getScreenDPI(context: Context): Int {
        val displayMetrics = context.resources.displayMetrics
        return displayMetrics.densityDpi
    }

    /**
     * 获取状态栏高度
     *
     * @param context 上下文
     * @return 状态栏高度（像素）
     */
    @JvmStatic
    fun getStatusBarHeight(context: Context): Int {
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            context.resources.getDimensionPixelSize(resourceId)
        } else {
            0
        }
    }

    /**
     * 获取导航栏高度
     *
     * @param context 上下文
     * @return 导航栏高度（像素）
     */
    @JvmStatic
    fun getNavigationBarHeight(context: Context): Int {
        val resourceId = context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            context.resources.getDimensionPixelSize(resourceId)
        } else {
            0
        }
    }

    /**
     * 判断是否为平板设备
     *
     * @param context 上下文
     * @return 是否为平板
     */
    @JvmStatic
    fun isTablet(context: Context): Boolean {
        return (context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }

    /**
     * 判断是否为手机设备
     *
     * @param context 上下文
     * @return 是否为手机
     */
    @JvmStatic
    fun isPhone(context: Context): Boolean {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
        return telephonyManager?.phoneType != TelephonyManager.PHONE_TYPE_NONE
    }

    /**
     * 获取CPU架构
     *
     * @return CPU架构
     */
    @JvmStatic
    fun getCPUArchitecture(): String {
        return Build.SUPPORTED_ABIS.firstOrNull() ?: "Unknown"
    }

    /**
     * 获取所有支持的CPU架构
     *
     * @return CPU架构列表
     */
    @JvmStatic
    fun getSupportedABIs(): Array<String> {
        return Build.SUPPORTED_ABIS
    }

    /**
     * 获取CPU核心数
     *
     * @return CPU核心数
     */
    @JvmStatic
    fun getCPUCoreCount(): Int {
        return Runtime.getRuntime().availableProcessors()
    }

    /**
     * 获取CPU频率信息
     *
     * @return CPU频率信息
     */
    @JvmStatic
    fun getCPUFrequency(): String {
        return try {
            val reader = BufferedReader(FileReader("/proc/cpuinfo"))
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                if (line!!.contains("cpu MHz")) {
                    reader.close()
                    return line!!.split(":")[1].trim() + " MHz"
                }
            }
            reader.close()
            "Unknown"
        } catch (e: IOException) {
            "Unknown"
        }
    }

    /**
     * 获取总内存大小
     *
     * @param context 上下文
     * @return 总内存大小（字节）
     */
    @JvmStatic
    fun getTotalMemory(context: Context): Long {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        return memoryInfo.totalMem
    }

    /**
     * 获取可用内存大小
     *
     * @param context 上下文
     * @return 可用内存大小（字节）
     */
    @JvmStatic
    fun getAvailableMemory(context: Context): Long {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        return memoryInfo.availMem
    }

    /**
     * 获取已使用内存大小
     *
     * @param context 上下文
     * @return 已使用内存大小（字节）
     */
    @JvmStatic
    fun getUsedMemory(context: Context): Long {
        return getTotalMemory(context) - getAvailableMemory(context)
    }

    /**
     * 获取内存使用率
     *
     * @param context 上下文
     * @return 内存使用率（0-1）
     */
    @JvmStatic
    fun getMemoryUsageRatio(context: Context): Float {
        val total = getTotalMemory(context)
        return if (total > 0) {
            getUsedMemory(context).toFloat() / total
        } else {
            0f
        }
    }

    /**
     * 判断内存是否不足
     *
     * @param context 上下文
     * @return 内存是否不足
     */
    @JvmStatic
    fun isLowMemory(context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        return memoryInfo.lowMemory
    }

    /**
     * 获取内部存储总空间
     *
     * @return 内部存储总空间（字节）
     */
    @JvmStatic
    fun getInternalStorageTotal(): Long {
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        return stat.blockCountLong * stat.blockSizeLong
    }

    /**
     * 获取内部存储可用空间
     *
     * @return 内部存储可用空间（字节）
     */
    @JvmStatic
    fun getInternalStorageAvailable(): Long {
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        return stat.availableBlocksLong * stat.blockSizeLong
    }

    /**
     * 获取外部存储总空间
     *
     * @return 外部存储总空间（字节），不可用返回0
     */
    @JvmStatic
    fun getExternalStorageTotal(): Long {
        return if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            val path = Environment.getExternalStorageDirectory()
            val stat = StatFs(path.path)
            stat.blockCountLong * stat.blockSizeLong
        } else {
            0L
        }
    }

    /**
     * 获取外部存储可用空间
     *
     * @return 外部存储可用空间（字节），不可用返回0
     */
    @JvmStatic
    fun getExternalStorageAvailable(): Long {
        return if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            val path = Environment.getExternalStorageDirectory()
            val stat = StatFs(path.path)
            stat.availableBlocksLong * stat.blockSizeLong
        } else {
            0L
        }
    }

    /**
     * 判断外部存储是否可用
     *
     * @return 外部存储是否可用
     */
    @JvmStatic
    fun isExternalStorageAvailable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    /**
     * 获取系统语言
     *
     * @return 系统语言
     */
    @JvmStatic
    fun getSystemLanguage(): String {
        return Locale.getDefault().language
    }

    /**
     * 获取系统国家/地区
     *
     * @return 系统国家/地区
     */
    @JvmStatic
    fun getSystemCountry(): String {
        return Locale.getDefault().country
    }

    /**
     * 获取系统时区
     *
     * @return 系统时区
     */
    @JvmStatic
    fun getSystemTimeZone(): String {
        return java.util.TimeZone.getDefault().id
    }

    /**
     * 判断设备是否已Root
     *
     * @return 是否已Root
     */
    @JvmStatic
    fun isDeviceRooted(): Boolean {
        // 检查常见的Root文件
        val rootPaths = arrayOf(
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/su"
        )

        for (path in rootPaths) {
            if (File(path).exists()) {
                return true
            }
        }

        // 检查su命令
        return try {
            val process = Runtime.getRuntime().exec("which su")
            val reader = BufferedReader(java.io.InputStreamReader(process.inputStream))
            reader.readLine() != null
        } catch (e: Exception) {
            false
        }
    }

    /**
     * 判断是否为模拟器
     *
     * @return 是否为模拟器
     */
    @JvmStatic
    fun isEmulator(): Boolean {
        return (Build.FINGERPRINT.startsWith("generic") ||
                Build.FINGERPRINT.lowercase().contains("vbox") ||
                Build.FINGERPRINT.lowercase().contains("test-keys") ||
                Build.MODEL.contains("google_sdk") ||
                Build.MODEL.contains("Emulator") ||
                Build.MODEL.contains("Android SDK built for x86") ||
                Build.MANUFACTURER.contains("Genymotion") ||
                (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")) ||
                "google_sdk" == Build.PRODUCT)
    }

    /**
     * 获取设备信息摘要
     *
     * @param context 上下文
     * @return 设备信息字符串
     */
    @JvmStatic
    fun getDeviceInfo(context: Context): String {
        return buildString {
            appendLine("设备品牌: ${getDeviceBrand()}")
            appendLine("设备型号: ${getDeviceModel()}")
            appendLine("制造商: ${getDeviceManufacturer()}")
            appendLine("Android版本: ${getAndroidVersion()}")
            appendLine("API级别: ${getAndroidSDKVersion()}")
            appendLine("CPU架构: ${getCPUArchitecture()}")
            appendLine("CPU核心数: ${getCPUCoreCount()}")
            appendLine("屏幕尺寸: ${getScreenWidth(context)}x${getScreenHeight(context)}")
            appendLine("屏幕密度: ${getScreenDensity(context)}")
            appendLine("总内存: ${NumUtil.formatFileSize(getTotalMemory(context))}")
            appendLine("可用内存: ${NumUtil.formatFileSize(getAvailableMemory(context))}")
            appendLine("内部存储: ${NumUtil.formatFileSize(getInternalStorageAvailable())}/${NumUtil.formatFileSize(getInternalStorageTotal())}")
            appendLine("系统语言: ${getSystemLanguage()}")
            appendLine("时区: ${getSystemTimeZone()}")
            appendLine("是否Root: ${if (isDeviceRooted()) "是" else "否"}")
            append("是否模拟器: ${if (isEmulator()) "是" else "否"}")
        }
    }

    /**
     * 检查权限是否已授予
     *
     * @param context 上下文
     * @param permission 权限名称
     * @return 权限是否已授予
     */
    @JvmStatic
    fun hasPermission(context: Context, permission: String): Boolean {
        return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * 获取应用版本名称
     *
     * @param context 上下文
     * @return 版本名称
     */
    @JvmStatic
    fun getAppVersionName(context: Context): String? {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 获取应用版本号
     *
     * @param context 上下文
     * @return 版本号
     */
    @JvmStatic
    fun getAppVersionCode(context: Context): Long {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.longVersionCode
            } else {
                @Suppress("DEPRECATION")
                packageInfo.versionCode.toLong()
            }
        } catch (e: Exception) {
            0L
        }
    }
}