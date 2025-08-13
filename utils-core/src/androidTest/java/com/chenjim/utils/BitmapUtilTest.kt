package com.chenjim.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BitmapUtilTest {

    private lateinit var context: Context
    private lateinit var testBitmap: Bitmap

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        // 创建一个测试用的Bitmap
        testBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        testBitmap.eraseColor(Color.RED)
    }

    @Test
    fun testCalculateInSampleSize() {
        val options = BitmapFactory.Options()
        options.outWidth = 200
        options.outHeight = 200
        
        val sampleSize = BitmapUtil.calculateInSampleSize(options, 100, 100)
        assertTrue("采样大小应大于等于1", sampleSize >= 1)
        assertTrue("采样大小应合理", sampleSize <= 4)
    }

    @Test
    fun testToGrayscale() {
        val grayscaleBitmap = BitmapUtil.toGrayscale(testBitmap)
        assertNotNull("灰度处理后的Bitmap不应为null", grayscaleBitmap)
        assertEquals("处理后宽度应保持不变", testBitmap.width, grayscaleBitmap.width)
        assertEquals("处理后高度应保持不变", testBitmap.height, grayscaleBitmap.height)
        assertEquals("灰度图应使用RGB_565配置", Bitmap.Config.RGB_565, grayscaleBitmap.config)
    }

    @Test
    fun testDecodeFromBytes() {
        // 创建测试用的字节数组
        val byteArrayOutputStream = java.io.ByteArrayOutputStream()
        testBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        
        val decodedBitmap = BitmapUtil.decodeFromBytes(byteArray, 50, 50)
        assertNotNull("解码后的Bitmap不应为null", decodedBitmap)
        assertTrue("解码后的宽度应小于等于50", decodedBitmap.width <= 50)
        assertTrue("解码后的高度应小于等于50", decodedBitmap.height <= 50)
    }

    @Test
    fun testDecodeFromBytesLargeSize() {
        // 创建测试用的字节数组
        val byteArrayOutputStream = java.io.ByteArrayOutputStream()
        testBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        
        // 使用较大的目标尺寸
        val decodedBitmap = BitmapUtil.decodeFromBytes(byteArray, 200, 200)
        assertNotNull("解码后的Bitmap不应为null", decodedBitmap)
        // 由于原图是100x100，解码后应该保持原尺寸或更小
        assertTrue("解码后的宽度应合理", decodedBitmap.width <= 200)
        assertTrue("解码后的高度应合理", decodedBitmap.height <= 200)
    }
}