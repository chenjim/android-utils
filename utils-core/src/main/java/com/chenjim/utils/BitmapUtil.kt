package com.chenjim.utils

import android.graphics.*
import java.io.ByteArrayOutputStream
import kotlin.math.min

/**
 * Bitmap工具类
 * 提供图片处理相关的工具方法
 * 
 * @author jim.chen
 * @since 2017/5/16
 */
object BitmapUtil {

    /**
     * 将文件解码为指定尺寸范围内的Bitmap
     *
     * @param filePath 文件路径
     * @param maxWidth 最大宽度
     * @param maxHeight 最大高度
     * @return 解码后的Bitmap，失败返回null
     */
    @JvmStatic
    @Synchronized
    fun decodeFromFile(filePath: String?, maxWidth: Int, maxHeight: Int): Bitmap? {
        if (filePath.isNullOrEmpty()) return null
        
        return BitmapFactory.Options().run {
            // 首先获取图片尺寸
            inPreferredConfig = Bitmap.Config.RGB_565
            inJustDecodeBounds = true
            BitmapFactory.decodeFile(filePath, this)
            
            // 计算采样率
            inSampleSize = calculateInSampleSize(this, maxWidth, maxHeight)
            
            // 解码图片
            inJustDecodeBounds = false
            BitmapFactory.decodeFile(filePath, this)
        }
    }

    /**
     * 将字节数组解码为指定尺寸范围内的Bitmap
     *
     * @param data 字节数组
     * @param maxWidth 最大宽度
     * @param maxHeight 最大高度
     * @return 解码后的Bitmap，失败返回null
     */
    @JvmStatic
    @Synchronized
    fun decodeFromBytes(data: ByteArray?, maxWidth: Int, maxHeight: Int): Bitmap? {
        if (data == null || data.isEmpty()) return null
        
        return BitmapFactory.Options().run {
            // 首先获取图片尺寸
            inJustDecodeBounds = true
            BitmapFactory.decodeByteArray(data, 0, data.size, this)
            
            // 计算采样率
            inSampleSize = calculateInSampleSize(this, maxWidth, maxHeight)
            
            // 解码图片
            inJustDecodeBounds = false
            BitmapFactory.decodeByteArray(data, 0, data.size, this)
        }
    }

    /**
     * 将Bitmap转换为灰度图
     *
     * @param bmpOriginal 原始Bitmap
     * @return 灰度图Bitmap
     */
    @JvmStatic
    fun toGrayscale(bmpOriginal: Bitmap): Bitmap {
        val width = bmpOriginal.width
        val height = bmpOriginal.height

        val bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        val canvas = Canvas(bmpGrayscale)
        val paint = Paint().apply {
            colorFilter = ColorMatrixColorFilter(ColorMatrix().apply {
                setSaturation(0f)
            })
        }
        canvas.drawBitmap(bmpOriginal, 0f, 0f, paint)
        return bmpGrayscale
    }

    /**
     * 计算图片采样率
     *
     * @param options BitmapFactory.Options
     * @param reqWidth 目标宽度
     * @param reqHeight 目标高度
     * @return 采样率
     */
    @JvmStatic
    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val (height, width) = options.outHeight to options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    /**
     * 缩放Bitmap到指定尺寸
     *
     * @param bitmap 原始Bitmap
     * @param newWidth 新宽度
     * @param newHeight 新高度
     * @return 缩放后的Bitmap，原始Bitmap为null时返回null
     */
    @JvmStatic
    fun scaleBitmap(bitmap: Bitmap?, newWidth: Int, newHeight: Int): Bitmap? {
        return bitmap?.let { Bitmap.createScaledBitmap(it, newWidth, newHeight, true) }
    }

    /**
     * 按比例缩放Bitmap
     *
     * @param bitmap 原始Bitmap
     * @param scale 缩放比例
     * @return 缩放后的Bitmap，原始Bitmap为null时返回null
     */
    @JvmStatic
    fun scaleBitmap(bitmap: Bitmap?, scale: Float): Bitmap? {
        return bitmap?.let {
            val newWidth = (it.width * scale).toInt()
            val newHeight = (it.height * scale).toInt()
            scaleBitmap(it, newWidth, newHeight)
        }
    }

    /**
     * 旋转Bitmap
     *
     * @param bitmap 原始Bitmap
     * @param degrees 旋转角度
     * @return 旋转后的Bitmap，原始Bitmap为null时返回null
     */
    @JvmStatic
    fun rotateBitmap(bitmap: Bitmap?, degrees: Float): Bitmap? {
        return bitmap?.let {
            val matrix = Matrix().apply { postRotate(degrees) }
            Bitmap.createBitmap(it, 0, 0, it.width, it.height, matrix, true)
        }
    }

    /**
     * 翻转Bitmap
     *
     * @param bitmap 原始Bitmap
     * @param horizontal 是否水平翻转
     * @param vertical 是否垂直翻转
     * @return 翻转后的Bitmap，原始Bitmap为null时返回null
     */
    @JvmStatic
    fun flipBitmap(bitmap: Bitmap?, horizontal: Boolean, vertical: Boolean): Bitmap? {
        return bitmap?.let {
            val matrix = Matrix().apply {
                preScale(
                    if (horizontal) -1f else 1f,
                    if (vertical) -1f else 1f
                )
            }
            Bitmap.createBitmap(it, 0, 0, it.width, it.height, matrix, true)
        }
    }

    /**
     * 裁剪Bitmap为圆形
     *
     * @param bitmap 原始Bitmap
     * @return 圆形Bitmap，原始Bitmap为null时返回null
     */
    @JvmStatic
    fun cropToCircle(bitmap: Bitmap?): Bitmap? {
        return bitmap?.let {
            val size = min(it.width, it.height)
            val output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(output)
            
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            val rect = Rect(0, 0, size, size)
            val rectF = RectF(rect)
            
            // 绘制圆形遮罩
            canvas.drawOval(rectF, paint)
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            
            // 绘制原图
            val x = (it.width - size) / 2
            val y = (it.height - size) / 2
            canvas.drawBitmap(it, Rect(x, y, x + size, y + size), rect, paint)
            
            output
        }
    }

    /**
     * 裁剪Bitmap为圆角矩形
     *
     * @param bitmap 原始Bitmap
     * @param radius 圆角半径
     * @return 圆角矩形Bitmap，原始Bitmap为null时返回null
     */
    @JvmStatic
    fun cropToRoundRect(bitmap: Bitmap?, radius: Float): Bitmap? {
        return bitmap?.let {
            val output = Bitmap.createBitmap(it.width, it.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(output)
            
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            val rect = Rect(0, 0, it.width, it.height)
            val rectF = RectF(rect)
            
            // 绘制圆角矩形遮罩
            canvas.drawRoundRect(rectF, radius, radius, paint)
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            
            // 绘制原图
            canvas.drawBitmap(it, rect, rect, paint)
            
            output
        }
    }

    /**
     * 为Bitmap添加边框
     *
     * @param bitmap 原始Bitmap
     * @param borderWidth 边框宽度
     * @param borderColor 边框颜色
     * @return 带边框的Bitmap，原始Bitmap为null时返回null
     */
    @JvmStatic
    fun addBorder(bitmap: Bitmap?, borderWidth: Int, borderColor: Int): Bitmap? {
        return bitmap?.let {
            val width = it.width + 2 * borderWidth
            val height = it.height + 2 * borderWidth
            val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            
            Canvas(output).apply {
                drawColor(borderColor)
                drawBitmap(it, borderWidth.toFloat(), borderWidth.toFloat(), null)
            }
            
            output
        }
    }

    /**
     * 调整Bitmap亮度
     *
     * @param bitmap 原始Bitmap
     * @param brightness 亮度值 (-255 到 255)
     * @return 调整亮度后的Bitmap，原始Bitmap为null时返回null
     */
    @JvmStatic
    fun adjustBrightness(bitmap: Bitmap?, brightness: Int): Bitmap? {
        return bitmap?.let {
            val output = Bitmap.createBitmap(it.width, it.height, it.config ?: Bitmap.Config.ARGB_8888)
            val canvas = Canvas(output)
            
            val paint = Paint().apply {
                val colorMatrix = ColorMatrix(floatArrayOf(
                    1f, 0f, 0f, 0f, brightness.toFloat(),
                    0f, 1f, 0f, 0f, brightness.toFloat(),
                    0f, 0f, 1f, 0f, brightness.toFloat(),
                    0f, 0f, 0f, 1f, 0f
                ))
                colorFilter = ColorMatrixColorFilter(colorMatrix)
            }
            
            canvas.drawBitmap(it, 0f, 0f, paint)
            output
        }
    }

    /**
     * 调整Bitmap对比度
     *
     * @param bitmap 原始Bitmap
     * @param contrast 对比度值 (0.0到2.0，1.0为原始对比度)
     * @return 调整对比度后的Bitmap，原始Bitmap为null时返回null
     */
    @JvmStatic
    fun adjustContrast(bitmap: Bitmap?, contrast: Float): Bitmap? {
        return bitmap?.let {
            val output = Bitmap.createBitmap(it.width, it.height, it.config ?: Bitmap.Config.ARGB_8888)
            val canvas = Canvas(output)
            
            val paint = Paint().apply {
                val translate = (1.0f - contrast) / 2.0f * 255.0f
                val colorMatrix = ColorMatrix(floatArrayOf(
                    contrast, 0f, 0f, 0f, translate,
                    0f, contrast, 0f, 0f, translate,
                    0f, 0f, contrast, 0f, translate,
                    0f, 0f, 0f, 1f, 0f
                ))
                colorFilter = ColorMatrixColorFilter(colorMatrix)
            }
            
            canvas.drawBitmap(it, 0f, 0f, paint)
            output
        }
    }

    /**
     * 压缩Bitmap为字节数组
     *
     * @param bitmap 原始Bitmap
     * @param format 压缩格式
     * @param quality 压缩质量 (0-100)
     * @return 压缩后的字节数组，原始Bitmap为null时返回null
     */
    @JvmStatic
    fun compressBitmap(bitmap: Bitmap?, format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG, quality: Int): ByteArray? {
        return bitmap?.let {
            ByteArrayOutputStream().use { outputStream ->
                it.compress(format, quality, outputStream)
                outputStream.toByteArray()
            }
        }
    }

    /**
     * 从字节数组创建Bitmap
     *
     * @param data 字节数组
     * @return Bitmap，字节数组为null或空时返回null
     */
    @JvmStatic
    fun createBitmapFromBytes(data: ByteArray?): Bitmap? {
        return data?.takeIf { it.isNotEmpty() }?.let {
            BitmapFactory.decodeByteArray(it, 0, it.size)
        }
    }

    /**
     * 检查Bitmap是否已回收
     *
     * @param bitmap Bitmap对象
     * @return 是否已回收，null时返回true
     */
    @JvmStatic
    fun isBitmapRecycled(bitmap: Bitmap?): Boolean {
        return bitmap?.isRecycled ?: true
    }

    /**
     * 安全回收Bitmap
     *
     * @param bitmap Bitmap对象
     */
    @JvmStatic
    fun recycleBitmap(bitmap: Bitmap?) {
        bitmap?.takeIf { !it.isRecycled }?.recycle()
    }
}
