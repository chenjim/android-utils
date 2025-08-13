package com.chenjim.utils

import android.content.Context
import android.graphics.Bitmap
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.BufferedWriter
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.security.DigestInputStream
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/**
 * @description：
 * @fileName: FileUtil
 * @author: jim.chen
 * @date: 2020/4/16
 */
object FileUtils {

    /**
     * 压缩文件
     *
     * @param resFile  需要压缩的文件（夹）
     * @param zipOut   压缩的目的文件
     * @param rootPath 压缩的文件路径
     * @throws FileNotFoundException 找不到文件时抛出
     * @throws IOException           当压缩过程出错时抛出
     */
    @Throws(FileNotFoundException::class, IOException::class)
    private fun zipFile(resFile: File, zipOut: ZipOutputStream, rootPath: String) {
        var rootPath = rootPath
        rootPath = (rootPath + (if (rootPath.trim { it <= ' ' }.isEmpty()) "" else File.separator)
                + resFile.getName())
        rootPath = String(rootPath.toByteArray(charset("8859_1")), charset("GB2312"))
        if (resFile.isDirectory()) {
            val fileList = resFile.listFiles()
            for (file in fileList!!) {
                zipFile(file, zipOut, rootPath)
            }
        } else {
            val buffer = ByteArray(1024)
            val `in` = BufferedInputStream(FileInputStream(resFile), 1024)
            zipOut.putNextEntry(ZipEntry(rootPath))
            var realLength: Int
            while ((`in`.read(buffer).also { realLength = it }) != -1) {
                zipOut.write(buffer, 0, realLength)
            }
            `in`.close()
            zipOut.flush()
            zipOut.closeEntry()
        }
    }

    /**
     * 批量压缩文件（夹）
     *
     * @param resFileList 要压缩的文件（夹）列表
     * @param zipFile     生成的压缩文件
     * @param comment     压缩文件的注释
     * @throws IOException 当压缩过程出错时抛出
     */
    @JvmStatic
    @Throws(IOException::class)
    fun zipFiles(resFileList: MutableCollection<File>, zipFile: File?, comment: String?) {
        val zipout = ZipOutputStream(
            BufferedOutputStream(
                FileOutputStream(
                    zipFile
                ), 1024
            )
        )
        for (resFile in resFileList) {
            zipFile(resFile, zipout, "")
        }
        zipout.setComment(comment)
        zipout.close()
    }


    @JvmStatic
    fun readToByteArray(filePath: String): ByteArray? {
        val file = File(filePath)
        val fileSize = file.length()
        if (fileSize > Int.Companion.MAX_VALUE) {
            println("file too big...")
            return null
        }
        try {
            val fi = FileInputStream(file)
            val buffer = ByteArray(fileSize.toInt())
            var offset = 0
            var numRead = 0
            while (offset < buffer.size
                && (fi.read(buffer, offset, buffer.size - offset).also { numRead = it }) >= 0
            ) {
                offset += numRead
            }
            // 确保所有数据均被读取
            if (offset != buffer.size) {
                throw IOException("Could not completely read file:" + file.getName())
            }
            fi.close()
            return buffer
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    @JvmStatic
    fun saveBitmap(bitmap: Bitmap, imagePath: String): Boolean {
        val file = File(imagePath)
        val parentFile = file.getParentFile()
        if (!parentFile!!.exists()) {
            parentFile.mkdirs()
        }
        try {
            val fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos)
            fos.flush()
            fos.close()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    @JvmStatic
    fun saveBytes(bytes: ByteArray?, filePath: String): Boolean {
        val file = File(filePath)
        val parentFile = file.getParentFile()
        if (!parentFile!!.exists()) {
            parentFile.mkdirs()
        }
        try {
            val fos = FileOutputStream(file)
            fos.write(bytes)
            fos.flush()
            fos.close()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    @Synchronized
    private fun write(file: File?, message: String?, append: Boolean) {
        if (file == null) {
            Log.e("file is null,Could not write:$message")
            return
        }
        try {
            if (!file.exists()) {
                file.createNewFile()
            }
            if (file.canWrite()) {
                val writer = BufferedWriter(FileWriter(file, append))
                writer.write((StringBuffer()).append(message).toString())
                writer.close()
            }
        } catch (ioexception: IOException) {
            Log.e("Could not write:'$message'to file:$file")
        }
    }

    /**
     * Return the MD5 of file.
     *
     * @param file The file.
     * @return the md5 of file
     */
    @JvmStatic
    fun getFileMD5(file: File?): ByteArray? {
        if (file == null) {
            return null
        }
        var dis: DigestInputStream? = null
        try {
            val fis = FileInputStream(file)
            var md = MessageDigest.getInstance("MD5")
            dis = DigestInputStream(fis, md)
            val buffer = ByteArray(1024 * 256)
            while (true) {
                if (dis.read(buffer) <= 0) {
                    break
                }
            }
            md = dis.messageDigest
            return md.digest()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                dis?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return null
    }

    @JvmStatic
    fun getFileMD5Sting(file: File?): String {
        try {
            return String(getFileMD5(file)!!, charset("UTF-8"))
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return String(getFileMD5(file)!!)
    }

    @JvmStatic
    fun fileCopy(src: File?, dest: File?) {
        var fis: FileInputStream? = null
        var fos: FileOutputStream? = null
        try {
            fis = FileInputStream(src)
            fos = FileOutputStream(dest)
            val buffer = ByteArray(1024)
            var length: Int
            while ((fis.read(buffer).also { length = it }) > 0) {
                fos.write(buffer, 0, length)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                fis?.close()
                fos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    @JvmStatic
    fun readAssets(context: Context, path: String): String {
        var ret = ""
        try {
            val inputStream = context.assets.open(path)
            val baos = ByteArrayOutputStream()
            var len = -1
            val buffer = ByteArray(1024)
            while ((inputStream.read(buffer).also { len = it }) != -1) {
                baos.write(buffer, 0, len)
            }
            ret = baos.toString()
            inputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return ret
    }

    /**
     * 读取文件内容为字符串
     *
     * @param filePath 文件路径
     * @param charset 字符编码，默认UTF-8
     * @return 文件内容字符串
     */
    @JvmStatic
    fun readFileToString(filePath: String, charset: String = "UTF-8"): String? {
        return try {
            val file = File(filePath)
            if (!file.exists()) return null
            file.readText(Charsets.UTF_8)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 将字符串写入文件
     *
     * @param filePath 文件路径
     * @param content 要写入的内容
     * @param append 是否追加模式
     * @return 是否成功
     */
    @JvmStatic
    fun writeStringToFile(filePath: String, content: String, append: Boolean = false): Boolean {
        return try {
            val file = File(filePath)
            val parentFile = file.parentFile
            if (parentFile != null && !parentFile.exists()) {
                parentFile.mkdirs()
            }
            if (append) {
                file.appendText(content)
            } else {
                file.writeText(content)
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 获取文件大小
     *
     * @param filePath 文件路径
     * @return 文件大小（字节），文件不存在返回-1
     */
    @JvmStatic
    fun getFileSize(filePath: String): Long {
        val file = File(filePath)
        return if (file.exists() && file.isFile) file.length() else -1
    }

    /**
     * 获取目录大小
     *
     * @param dirPath 目录路径
     * @return 目录大小（字节）
     */
    @JvmStatic
    fun getDirSize(dirPath: String): Long {
        val dir = File(dirPath)
        if (!dir.exists() || !dir.isDirectory) return 0
        
        var size = 0L
        dir.walkTopDown().forEach { file ->
            if (file.isFile) {
                size += file.length()
            }
        }
        return size
    }

    /**
     * 删除文件或目录
     *
     * @param path 文件或目录路径
     * @return 是否成功删除
     */
    @JvmStatic
    fun deleteFileOrDir(path: String): Boolean {
        val file = File(path)
        if (!file.exists()) return true
        
        return if (file.isDirectory) {
            file.deleteRecursively()
        } else {
            file.delete()
        }
    }

    /**
     * 创建目录
     *
     * @param dirPath 目录路径
     * @return 是否成功创建
     */
    @JvmStatic
    fun createDir(dirPath: String): Boolean {
        val dir = File(dirPath)
        return if (!dir.exists()) {
            dir.mkdirs()
        } else {
            dir.isDirectory
        }
    }

    /**
     * 重命名文件或目录
     *
     * @param oldPath 原路径
     * @param newPath 新路径
     * @return 是否成功重命名
     */
    @JvmStatic
    fun renameFile(oldPath: String, newPath: String): Boolean {
        val oldFile = File(oldPath)
        val newFile = File(newPath)
        return oldFile.exists() && oldFile.renameTo(newFile)
    }

    /**
     * 获取文件扩展名
     *
     * @param filePath 文件路径
     * @return 文件扩展名（不包含点号）
     */
    @JvmStatic
    fun getFileExtension(filePath: String): String {
        val file = File(filePath)
        val name = file.name
        val lastDotIndex = name.lastIndexOf('.')
        return if (lastDotIndex > 0 && lastDotIndex < name.length - 1) {
            name.substring(lastDotIndex + 1).lowercase()
        } else {
            ""
        }
    }

    /**
     * 获取不带扩展名的文件名
     *
     * @param filePath 文件路径
     * @return 不带扩展名的文件名
     */
    @JvmStatic
    fun getFileNameWithoutExtension(filePath: String): String {
        val file = File(filePath)
        val name = file.name
        val lastDotIndex = name.lastIndexOf('.')
        return if (lastDotIndex > 0) {
            name.substring(0, lastDotIndex)
        } else {
            name
        }
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath 文件路径
     * @return 是否存在
     */
    @JvmStatic
    fun isFileExists(filePath: String): Boolean {
        return File(filePath).exists()
    }

    /**
     * 判断是否为目录
     *
     * @param path 路径
     * @return 是否为目录
     */
    @JvmStatic
    fun isDirectory(path: String): Boolean {
        return File(path).isDirectory
    }

    /**
     * 判断是否为文件
     *
     * @param path 路径
     * @return 是否为文件
     */
    @JvmStatic
    fun isFile(path: String): Boolean {
        return File(path).isFile
    }

    /**
     * 获取文件最后修改时间
     *
     * @param filePath 文件路径
     * @return 最后修改时间戳，文件不存在返回0
     */
    @JvmStatic
    fun getLastModified(filePath: String): Long {
        val file = File(filePath)
        return if (file.exists()) file.lastModified() else 0
    }

    /**
     * 列出目录下的所有文件
     *
     * @param dirPath 目录路径
     * @param includeSubDir 是否包含子目录
     * @return 文件列表
     */
    @JvmStatic
    fun listFiles(dirPath: String, includeSubDir: Boolean = false): List<File> {
        val dir = File(dirPath)
        if (!dir.exists() || !dir.isDirectory) return emptyList()
        
        return if (includeSubDir) {
            dir.walkTopDown().filter { it.isFile }.toList()
        } else {
            dir.listFiles()?.filter { it.isFile } ?: emptyList()
        }
    }

    /**
     * 根据扩展名过滤文件
     *
     * @param dirPath 目录路径
     * @param extensions 扩展名列表（不包含点号）
     * @param includeSubDir 是否包含子目录
     * @return 过滤后的文件列表
     */
    @JvmStatic
    fun listFilesByExtension(dirPath: String, extensions: List<String>, includeSubDir: Boolean = false): List<File> {
        val files = listFiles(dirPath, includeSubDir)
        val lowerExtensions = extensions.map { it.lowercase() }
        return files.filter { file ->
            val ext = getFileExtension(file.absolutePath)
            ext in lowerExtensions
        }
    }

    /**
     * 清空目录（删除目录下所有文件和子目录，但保留目录本身）
     *
     * @param dirPath 目录路径
     * @return 是否成功清空
     */
    @JvmStatic
    fun clearDirectory(dirPath: String): Boolean {
        val dir = File(dirPath)
        if (!dir.exists() || !dir.isDirectory) return false
        
        return try {
            dir.listFiles()?.forEach { file ->
                if (file.isDirectory) {
                    file.deleteRecursively()
                } else {
                    file.delete()
                }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 移动文件或目录
     *
     * @param sourcePath 源路径
     * @param destPath 目标路径
     * @return 是否成功移动
     */
    @JvmStatic
    fun moveFile(sourcePath: String, destPath: String): Boolean {
        return try {
            val sourceFile = File(sourcePath)
            val destFile = File(destPath)
            
            if (!sourceFile.exists()) return false
            
            // 确保目标目录存在
            destFile.parentFile?.mkdirs()
            
            // 先尝试重命名（同一文件系统下更快）
            if (sourceFile.renameTo(destFile)) {
                true
            } else {
                // 重命名失败，使用复制+删除
                fileCopy(sourceFile, destFile)
                sourceFile.delete()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 计算文件的SHA-256哈希值
     *
     * @param file 文件
     * @return SHA-256哈希值的十六进制字符串
     */
    @JvmStatic
    fun getFileSHA256(file: File?): String? {
        if (file == null || !file.exists()) return null
        
        return try {
            val digest = MessageDigest.getInstance("SHA-256")
            val fis = FileInputStream(file)
            val buffer = ByteArray(8192)
            var bytesRead: Int
            
            while (fis.read(buffer).also { bytesRead = it } != -1) {
                digest.update(buffer, 0, bytesRead)
            }
            fis.close()
            
            digest.digest().joinToString("") { "%02x".format(it) }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}