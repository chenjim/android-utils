package com.chenjim.utils

import android.util.Base64
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.Mac
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.xor

/**
 * 加密工具类
 * 提供常用的加密、解密、哈希等功能
 *
 * @author chenjim
 * @date 2024/01/01
 */
object CryptoUtils {

    private const val AES_TRANSFORMATION = "AES/CBC/PKCS5Padding"
    private const val AES_KEY_LENGTH = 256
    private const val IV_LENGTH = 16

    /**
     * MD5加密
     *
     * @param input 输入字符串
     * @return MD5哈希值（32位小写）
     */
    @JvmStatic
    fun md5(input: String): String {
        return try {
            val md = MessageDigest.getInstance("MD5")
            val digest = md.digest(input.toByteArray())
            bytesToHex(digest)
        } catch (e: NoSuchAlgorithmException) {
            ""
        }
    }

    /**
     * SHA-1加密
     *
     * @param input 输入字符串
     * @return SHA-1哈希值（40位小写）
     */
    @JvmStatic
    fun sha1(input: String): String {
        return try {
            val md = MessageDigest.getInstance("SHA-1")
            val digest = md.digest(input.toByteArray())
            bytesToHex(digest)
        } catch (e: NoSuchAlgorithmException) {
            ""
        }
    }

    /**
     * SHA-256加密
     *
     * @param input 输入字符串
     * @return SHA-256哈希值（64位小写）
     */
    @JvmStatic
    fun sha256(input: String): String {
        return try {
            val md = MessageDigest.getInstance("SHA-256")
            val digest = md.digest(input.toByteArray())
            bytesToHex(digest)
        } catch (e: NoSuchAlgorithmException) {
            ""
        }
    }

    /**
     * SHA-512加密
     *
     * @param input 输入字符串
     * @return SHA-512哈希值（128位小写）
     */
    @JvmStatic
    fun sha512(input: String): String {
        return try {
            val md = MessageDigest.getInstance("SHA-512")
            val digest = md.digest(input.toByteArray())
            bytesToHex(digest)
        } catch (e: NoSuchAlgorithmException) {
            ""
        }
    }

    /**
     * HMAC-SHA256加密
     *
     * @param data 数据
     * @param key 密钥
     * @return HMAC-SHA256哈希值
     */
    @JvmStatic
    fun hmacSha256(data: String, key: String): String {
        return try {
            val mac = Mac.getInstance("HmacSHA256")
            val secretKeySpec = SecretKeySpec(key.toByteArray(), "HmacSHA256")
            mac.init(secretKeySpec)
            val digest = mac.doFinal(data.toByteArray())
            bytesToHex(digest)
        } catch (e: Exception) {
            ""
        }
    }

    /**
     * Base64编码
     *
     * @param input 输入字符串
     * @return Base64编码结果
     */
    @JvmStatic
    fun base64Encode(input: String): String {
        return Base64.encodeToString(input.toByteArray(), Base64.NO_WRAP)
    }

    /**
     * Base64编码（字节数组）
     *
     * @param input 输入字节数组
     * @return Base64编码结果
     */
    @JvmStatic
    fun base64Encode(input: ByteArray): String {
        return Base64.encodeToString(input, Base64.NO_WRAP)
    }

    /**
     * Base64解码
     *
     * @param input Base64编码的字符串
     * @return 解码后的字符串
     */
    @JvmStatic
    fun base64Decode(input: String): String? {
        return try {
            val decoded = Base64.decode(input, Base64.NO_WRAP)
            String(decoded)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Base64解码为字节数组
     *
     * @param input Base64编码的字符串
     * @return 解码后的字节数组
     */
    @JvmStatic
    fun base64DecodeToBytes(input: String): ByteArray? {
        return try {
            Base64.decode(input, Base64.NO_WRAP)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 生成AES密钥
     *
     * @return Base64编码的AES密钥
     */
    @JvmStatic
    fun generateAESKey(): String {
        return try {
            val keyGenerator = KeyGenerator.getInstance("AES")
            keyGenerator.init(AES_KEY_LENGTH)
            val secretKey = keyGenerator.generateKey()
            base64Encode(secretKey.encoded)
        } catch (e: Exception) {
            ""
        }
    }

    /**
     * 生成随机IV
     *
     * @return Base64编码的IV
     */
    @JvmStatic
    fun generateIV(): String {
        val iv = ByteArray(IV_LENGTH)
        SecureRandom().nextBytes(iv)
        return base64Encode(iv)
    }

    /**
     * AES加密
     *
     * @param plainText 明文
     * @param key Base64编码的密钥
     * @param iv Base64编码的IV，为null时自动生成
     * @return 加密结果，格式为"iv:cipherText"（Base64编码）
     */
    @JvmStatic
    fun aesEncrypt(plainText: String, key: String, iv: String? = null): String? {
        return try {
            val keyBytes = base64DecodeToBytes(key) ?: return null
            val ivBytes = if (iv != null) {
                base64DecodeToBytes(iv) ?: return null
            } else {
                ByteArray(IV_LENGTH).also { SecureRandom().nextBytes(it) }
            }

            val secretKeySpec = SecretKeySpec(keyBytes, "AES")
            val ivParameterSpec = IvParameterSpec(ivBytes)
            val cipher = Cipher.getInstance(AES_TRANSFORMATION)
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec)

            val cipherText = cipher.doFinal(plainText.toByteArray())
            val ivBase64 = base64Encode(ivBytes)
            val cipherBase64 = base64Encode(cipherText)

            "$ivBase64:$cipherBase64"
        } catch (e: Exception) {
            null
        }
    }

    /**
     * AES解密
     *
     * @param encryptedData 加密数据，格式为"iv:cipherText"（Base64编码）
     * @param key Base64编码的密钥
     * @return 解密后的明文
     */
    @JvmStatic
    fun aesDecrypt(encryptedData: String, key: String): String? {
        return try {
            val parts = encryptedData.split(":")
            if (parts.size != 2) return null

            val keyBytes = base64DecodeToBytes(key) ?: return null
            val ivBytes = base64DecodeToBytes(parts[0]) ?: return null
            val cipherBytes = base64DecodeToBytes(parts[1]) ?: return null

            val secretKeySpec = SecretKeySpec(keyBytes, "AES")
            val ivParameterSpec = IvParameterSpec(ivBytes)
            val cipher = Cipher.getInstance(AES_TRANSFORMATION)
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec)

            val plainText = cipher.doFinal(cipherBytes)
            String(plainText)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 简单XOR加密/解密
     *
     * @param data 数据
     * @param key 密钥
     * @return 加密/解密结果
     */
    @JvmStatic
    fun xorCrypt(data: String, key: String): String {
        if (key.isEmpty()) return data

        val dataBytes = data.toByteArray()
        val keyBytes = key.toByteArray()
        val result = ByteArray(dataBytes.size)

        for (i in dataBytes.indices) {
            result[i] = dataBytes[i] xor keyBytes[i % keyBytes.size]
        }

        return String(result)
    }

    /**
     * 简单XOR加密/解密（Base64输出）
     *
     * @param data 数据
     * @param key 密钥
     * @return Base64编码的加密/解密结果
     */
    @JvmStatic
    fun xorCryptBase64(data: String, key: String): String {
        if (key.isEmpty()) return base64Encode(data)

        val dataBytes = data.toByteArray()
        val keyBytes = key.toByteArray()
        val result = ByteArray(dataBytes.size)

        for (i in dataBytes.indices) {
            result[i] = dataBytes[i] xor keyBytes[i % keyBytes.size]
        }

        return base64Encode(result)
    }

    /**
     * 凯撒密码加密
     *
     * @param text 明文
     * @param shift 偏移量
     * @return 密文
     */
    @JvmStatic
    fun caesarEncrypt(text: String, shift: Int): String {
        val result = StringBuilder()
        val normalizedShift = shift % 26

        for (char in text) {
            when {
                char.isUpperCase() -> {
                    val shifted = ((char - 'A' + normalizedShift) % 26 + 26) % 26
                    result.append('A' + shifted)
                }
                char.isLowerCase() -> {
                    val shifted = ((char - 'a' + normalizedShift) % 26 + 26) % 26
                    result.append('a' + shifted)
                }
                else -> result.append(char)
            }
        }

        return result.toString()
    }

    /**
     * 凯撒密码解密
     *
     * @param text 密文
     * @param shift 偏移量
     * @return 明文
     */
    @JvmStatic
    fun caesarDecrypt(text: String, shift: Int): String {
        return caesarEncrypt(text, -shift)
    }

    /**
     * 生成随机盐值
     *
     * @param length 盐值长度
     * @return Base64编码的盐值
     */
    @JvmStatic
    fun generateSalt(length: Int = 16): String {
        val salt = ByteArray(length)
        SecureRandom().nextBytes(salt)
        return base64Encode(salt)
    }

    /**
     * 带盐值的密码哈希
     *
     * @param password 密码
     * @param salt 盐值（Base64编码），为null时自动生成
     * @return 格式为"salt:hash"的字符串
     */
    @JvmStatic
    fun hashPassword(password: String, salt: String? = null): String {
        val actualSalt = salt ?: generateSalt()
        val saltBytes = base64DecodeToBytes(actualSalt) ?: return ""
        val passwordBytes = password.toByteArray()
        val combined = saltBytes + passwordBytes
        val hash = sha256(String(combined))
        return "$actualSalt:$hash"
    }

    /**
     * 验证密码
     *
     * @param password 待验证的密码
     * @param hashedPassword 存储的哈希密码（格式为"salt:hash"）
     * @return 密码是否正确
     */
    @JvmStatic
    fun verifyPassword(password: String, hashedPassword: String): Boolean {
        val parts = hashedPassword.split(":")
        if (parts.size != 2) return false

        val salt = parts[0]
        val expectedHash = parts[1]
        val actualHashedPassword = hashPassword(password, salt)
        val actualHash = actualHashedPassword.split(":")[1]

        return expectedHash == actualHash
    }

    /**
     * 字节数组转十六进制字符串
     *
     * @param bytes 字节数组
     * @return 十六进制字符串（小写）
     */
    @JvmStatic
    fun bytesToHex(bytes: ByteArray): String {
        val hexChars = "0123456789abcdef"
        val result = StringBuilder(bytes.size * 2)
        for (byte in bytes) {
            val i = byte.toInt()
            result.append(hexChars[i shr 4 and 0x0f])
            result.append(hexChars[i and 0x0f])
        }
        return result.toString()
    }

    /**
     * 十六进制字符串转字节数组
     *
     * @param hex 十六进制字符串
     * @return 字节数组
     */
    @JvmStatic
    fun hexToBytes(hex: String): ByteArray? {
        return try {
            val cleanHex = hex.replace(" ", "")
            if (cleanHex.length % 2 != 0) return null

            val result = ByteArray(cleanHex.length / 2)
            for (i in cleanHex.indices step 2) {
                val byte = cleanHex.substring(i, i + 2).toInt(16).toByte()
                result[i / 2] = byte
            }
            result
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 生成随机字符串
     *
     * @param length 长度
     * @param includeNumbers 是否包含数字
     * @param includeUppercase 是否包含大写字母
     * @param includeLowercase 是否包含小写字母
     * @param includeSymbols 是否包含符号
     * @return 随机字符串
     */
    @JvmStatic
    fun generateRandomString(
        length: Int,
        includeNumbers: Boolean = true,
        includeUppercase: Boolean = true,
        includeLowercase: Boolean = true,
        includeSymbols: Boolean = false
    ): String {
        val chars = buildString {
            if (includeNumbers) append("0123456789")
            if (includeUppercase) append("ABCDEFGHIJKLMNOPQRSTUVWXYZ")
            if (includeLowercase) append("abcdefghijklmnopqrstuvwxyz")
            if (includeSymbols) append("!@#$%^&*()_+-=[]{}|;:,.<>?")
        }

        if (chars.isEmpty()) return ""

        val random = SecureRandom()
        return (1..length)
            .map { chars[random.nextInt(chars.length)] }
            .joinToString("")
    }

    /**
     * 计算文件哈希值
     *
     * @param filePath 文件路径
     * @param algorithm 哈希算法（MD5, SHA-1, SHA-256等）
     * @return 哈希值
     */
    @JvmStatic
    fun calculateFileHash(filePath: String, algorithm: String = "SHA-256"): String? {
        return try {
            val file = java.io.File(filePath)
            if (!file.exists()) return null

            val digest = MessageDigest.getInstance(algorithm)
            file.inputStream().use { inputStream ->
                val buffer = ByteArray(8192)
                var bytesRead: Int
                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    digest.update(buffer, 0, bytesRead)
                }
            }
            bytesToHex(digest.digest())
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 简单的字符串混淆
     *
     * @param input 输入字符串
     * @param key 混淆密钥
     * @return 混淆后的字符串
     */
    @JvmStatic
    fun obfuscateString(input: String, key: String = "default"): String {
        val keyHash = md5(key)
        return xorCryptBase64(input, keyHash)
    }

    /**
     * 简单的字符串反混淆
     *
     * @param obfuscated 混淆后的字符串
     * @param key 混淆密钥
     * @return 原始字符串
     */
    @JvmStatic
    fun deobfuscateString(obfuscated: String, key: String = "default"): String? {
        return try {
            val keyHash = md5(key)
            val decoded = base64DecodeToBytes(obfuscated) ?: return null
            val keyBytes = keyHash.toByteArray()
            val result = ByteArray(decoded.size)

            for (i in decoded.indices) {
                result[i] = decoded[i] xor keyBytes[i % keyBytes.size]
            }

            String(result)
        } catch (e: Exception) {
            null
        }
    }
}