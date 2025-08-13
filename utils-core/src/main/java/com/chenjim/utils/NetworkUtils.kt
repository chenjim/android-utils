package com.chenjim.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.os.Build
import android.telephony.TelephonyManager
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.URL
import java.util.Collections
import java.util.regex.Pattern

/**
 * 网络工具类
 * 提供网络状态检测、IP地址获取等功能
 *
 * @author chenjim
 * @date 2024/01/01
 */
object NetworkUtils {

    /**
     * 检查网络是否可用
     *
     * @param context 上下文
     * @return 网络是否可用
     */
    @JvmStatic
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            ?: return false

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                    capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo
            networkInfo?.isConnected == true
        }
    }

    /**
     * 检查WiFi是否连接
     *
     * @param context 上下文
     * @return WiFi是否连接
     */
    @JvmStatic
    fun isWifiConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            ?: return false

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            networkInfo?.isConnected == true
        }
    }

    /**
     * 检查移动网络是否连接
     *
     * @param context 上下文
     * @return 移动网络是否连接
     */
    @JvmStatic
    fun isMobileConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            ?: return false

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
            networkInfo?.isConnected == true
        }
    }

    /**
     * 获取网络类型
     *
     * @param context 上下文
     * @return 网络类型字符串
     */
    @JvmStatic
    fun getNetworkType(context: Context): String {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            ?: return "UNKNOWN"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return "NONE"
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return "UNKNOWN"

            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "WIFI"
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    getMobileNetworkType(context)
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> "ETHERNET"
                else -> "UNKNOWN"
            }
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo ?: return "NONE"
            return when (networkInfo.type) {
                ConnectivityManager.TYPE_WIFI -> "WIFI"
                ConnectivityManager.TYPE_MOBILE -> getMobileNetworkType(context)
                ConnectivityManager.TYPE_ETHERNET -> "ETHERNET"
                else -> "UNKNOWN"
            }
        }
    }

    /**
     * 获取移动网络类型详情
     *
     * @param context 上下文
     * @return 移动网络类型
     */
    @JvmStatic
    fun getMobileNetworkType(context: Context): String {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
            ?: return "MOBILE"

        return when (telephonyManager.networkType) {
            TelephonyManager.NETWORK_TYPE_GPRS,
            TelephonyManager.NETWORK_TYPE_EDGE,
            TelephonyManager.NETWORK_TYPE_CDMA,
            TelephonyManager.NETWORK_TYPE_1xRTT,
            TelephonyManager.NETWORK_TYPE_IDEN -> "2G"

            TelephonyManager.NETWORK_TYPE_UMTS,
            TelephonyManager.NETWORK_TYPE_EVDO_0,
            TelephonyManager.NETWORK_TYPE_EVDO_A,
            TelephonyManager.NETWORK_TYPE_HSDPA,
            TelephonyManager.NETWORK_TYPE_HSUPA,
            TelephonyManager.NETWORK_TYPE_HSPA,
            TelephonyManager.NETWORK_TYPE_EVDO_B,
            TelephonyManager.NETWORK_TYPE_EHRPD,
            TelephonyManager.NETWORK_TYPE_HSPAP -> "3G"

            TelephonyManager.NETWORK_TYPE_LTE -> "4G"

            TelephonyManager.NETWORK_TYPE_NR -> "5G"

            else -> "MOBILE"
        }
    }

    /**
     * 获取WiFi的SSID
     *
     * @param context 上下文
     * @return WiFi SSID，未连接返回null
     */
    @JvmStatic
    fun getWifiSSID(context: Context): String? {
        if (!isWifiConnected(context)) return null

        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager
            ?: return null

        val wifiInfo = wifiManager.connectionInfo ?: return null
        var ssid = wifiInfo.ssid

        // 移除引号
        if (ssid.startsWith("\"") && ssid.endsWith("\"")) {
            ssid = ssid.substring(1, ssid.length - 1)
        }

        return if (ssid == "<unknown ssid>") null else ssid
    }

    /**
     * 获取本机IP地址
     *
     * @param useIPv4 是否使用IPv4，默认true
     * @return IP地址字符串
     */
    @JvmStatic
    fun getLocalIPAddress(useIPv4: Boolean = true): String? {
        try {
            val interfaces = Collections.list(NetworkInterface.getNetworkInterfaces())
            for (networkInterface in interfaces) {
                val addresses = Collections.list(networkInterface.inetAddresses)
                for (address in addresses) {
                    if (!address.isLoopbackAddress) {
                        val hostAddress = address.hostAddress
                        val isIPv4 = hostAddress?.indexOf(':') == -1

                        if (useIPv4) {
                            if (isIPv4) return hostAddress
                        } else {
                            if (!isIPv4) {
                                val delim = hostAddress?.indexOf('%')
                                return if (delim != null && delim != -1) {
                                    hostAddress.substring(0, delim).uppercase()
                                } else {
                                    hostAddress?.uppercase()
                                }
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 获取外网IP地址（需要网络请求）
     * 注意：此方法会进行网络请求，应在后台线程调用
     *
     * @return 外网IP地址
     */
    @JvmStatic
    fun getExternalIPAddress(): String? {
        return try {
            val url = URL("https://api.ipify.org")
            val connection = url.openConnection()
            connection.connectTimeout = 5000
            connection.readTimeout = 5000
            connection.getInputStream().bufferedReader().use { it.readText().trim() }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 验证IP地址格式
     *
     * @param ip IP地址字符串
     * @return 是否为有效的IP地址
     */
    @JvmStatic
    fun isValidIPAddress(ip: String?): Boolean {
        return isValidIPv4(ip) || isValidIPv6(ip)
    }

    /**
     * 验证IPv4地址格式
     *
     * @param ip IPv4地址字符串
     * @return 是否为有效的IPv4地址
     */
    @JvmStatic
    fun isValidIPv4(ip: String?): Boolean {
        if (ip.isNullOrEmpty()) return false

        val pattern = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$"
        return Pattern.matches(pattern, ip)
    }

    /**
     * 验证IPv6地址格式
     *
     * @param ip IPv6地址字符串
     * @return 是否为有效的IPv6地址
     */
    @JvmStatic
    fun isValidIPv6(ip: String?): Boolean {
        if (ip.isNullOrEmpty()) return false

        return try {
            InetAddress.getByName(ip) is java.net.Inet6Address
        } catch (e: Exception) {
            false
        }
    }

    /**
     * 验证URL格式
     *
     * @param url URL字符串
     * @return 是否为有效的URL
     */
    @JvmStatic
    fun isValidURL(url: String?): Boolean {
        if (url.isNullOrEmpty()) return false

        return try {
            URL(url)
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * 验证域名格式
     *
     * @param domain 域名字符串
     * @return 是否为有效的域名
     */
    @JvmStatic
    fun isValidDomain(domain: String?): Boolean {
        if (domain.isNullOrEmpty()) return false

        val pattern = "^[a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.[a-zA-Z]{2,}$"
        return Pattern.matches(pattern, domain)
    }

    /**
     * 检查主机是否可达（需要网络请求）
     * 注意：此方法会进行网络请求，应在后台线程调用
     *
     * @param host 主机地址
     * @param timeout 超时时间（毫秒），默认5000
     * @return 主机是否可达
     */
    @JvmStatic
    fun isHostReachable(host: String, timeout: Int = 5000): Boolean {
        return try {
            val address = InetAddress.getByName(host)
            address.isReachable(timeout)
        } catch (e: Exception) {
            false
        }
    }

    /**
     * 获取网络信号强度（WiFi）
     *
     * @param context 上下文
     * @return 信号强度等级（0-4），-1表示获取失败
     */
    @JvmStatic
    fun getWifiSignalLevel(context: Context): Int {
        if (!isWifiConnected(context)) return -1

        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager
            ?: return -1

        val wifiInfo = wifiManager.connectionInfo ?: return -1
        return WifiManager.calculateSignalLevel(wifiInfo.rssi, 5)
    }

    /**
     * 获取网络运营商名称
     *
     * @param context 上下文
     * @return 运营商名称
     */
    @JvmStatic
    fun getNetworkOperatorName(context: Context): String? {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
            ?: return null

        return telephonyManager.networkOperatorName?.takeIf { it.isNotEmpty() }
    }

    /**
     * 判断是否为内网IP
     *
     * @param ip IP地址
     * @return 是否为内网IP
     */
    @JvmStatic
    fun isInternalIP(ip: String?): Boolean {
        if (!isValidIPv4(ip)) return false

        val parts = ip!!.split(".")
        val a = parts[0].toInt()
        val b = parts[1].toInt()

        return when (a) {
            10 -> true
            172 -> b in 16..31
            192 -> b == 168
            else -> false
        }
    }

    /**
     * 获取MAC地址（需要权限）
     * 注意：Android 6.0+获取到的可能是固定值
     *
     * @param context 上下文
     * @return MAC地址
     */
    @JvmStatic
    fun getMacAddress(context: Context): String? {
        return try {
            val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager
            val wifiInfo = wifiManager?.connectionInfo
            wifiInfo?.macAddress
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 格式化网络速度
     *
     * @param bytesPerSecond 每秒字节数
     * @return 格式化后的速度字符串
     */
    @JvmStatic
    fun formatNetworkSpeed(bytesPerSecond: Long): String {
        return when {
            bytesPerSecond >= 1024 * 1024 -> String.format("%.1f MB/s", bytesPerSecond / (1024.0 * 1024.0))
            bytesPerSecond >= 1024 -> String.format("%.1f KB/s", bytesPerSecond / 1024.0)
            else -> "${bytesPerSecond} B/s"
        }
    }

    /**
     * 格式化流量大小
     *
     * @param bytes 字节数
     * @return 格式化后的流量字符串
     */
    @JvmStatic
    fun formatTrafficSize(bytes: Long): String {
        return NumUtil.formatFileSize(bytes)
    }
}