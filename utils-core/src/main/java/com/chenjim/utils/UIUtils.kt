package com.chenjim.utils

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import kotlin.math.roundToInt

/**
 * UI工具类
 * 提供常用的UI相关功能
 *
 * @author chenjim
 * @date 2024/01/01
 */
object UIUtils {

    /**
     * dp转px
     *
     * @param context 上下文
     * @param dpValue dp值
     * @return px值
     */
    @JvmStatic
    fun dp2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * px转dp
     *
     * @param context 上下文
     * @param pxValue px值
     * @return dp值
     */
    @JvmStatic
    fun px2dp(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * sp转px
     *
     * @param context 上下文
     * @param spValue sp值
     * @return px值
     */
    @JvmStatic
    fun sp2px(context: Context, spValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    /**
     * px转sp
     *
     * @param context 上下文
     * @param pxValue px值
     * @return sp值
     */
    @JvmStatic
    fun px2sp(context: Context, pxValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }

    /**
     * 获取尺寸资源值（px）
     *
     * @param context 上下文
     * @param dimenRes 尺寸资源ID
     * @return px值
     */
    @JvmStatic
    fun getDimensionPixelSize(context: Context, @DimenRes dimenRes: Int): Int {
        return context.resources.getDimensionPixelSize(dimenRes)
    }

    /**
     * 获取颜色资源值
     *
     * @param context 上下文
     * @param colorRes 颜色资源ID
     * @return 颜色值
     */
    @JvmStatic
    @ColorInt
    fun getColor(context: Context, @ColorRes colorRes: Int): Int {
        return ContextCompat.getColor(context, colorRes)
    }

    /**
     * 获取字符串资源值
     *
     * @param context 上下文
     * @param stringRes 字符串资源ID
     * @return 字符串
     */
    @JvmStatic
    fun getString(context: Context, @StringRes stringRes: Int): String {
        return context.getString(stringRes)
    }

    /**
     * 获取字符串资源值（带参数）
     *
     * @param context 上下文
     * @param stringRes 字符串资源ID
     * @param formatArgs 格式化参数
     * @return 格式化后的字符串
     */
    @JvmStatic
    fun getString(context: Context, @StringRes stringRes: Int, vararg formatArgs: Any): String {
        return context.getString(stringRes, *formatArgs)
    }

    /**
     * 显示短时间Toast
     *
     * @param context 上下文
     * @param message 消息
     */
    @JvmStatic
    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * 显示长时间Toast
     *
     * @param context 上下文
     * @param message 消息
     */
    @JvmStatic
    fun showLongToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    /**
     * 显示Toast（字符串资源）
     *
     * @param context 上下文
     * @param stringRes 字符串资源ID
     */
    @JvmStatic
    fun showToast(context: Context, @StringRes stringRes: Int) {
        Toast.makeText(context, stringRes, Toast.LENGTH_SHORT).show()
    }

    /**
     * 隐藏软键盘
     *
     * @param activity Activity
     */
    @JvmStatic
    fun hideSoftKeyboard(activity: Activity) {
        val view = activity.currentFocus
        if (view != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    /**
     * 隐藏软键盘
     *
     * @param view 当前焦点View
     */
    @JvmStatic
    fun hideSoftKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * 显示软键盘
     *
     * @param editText EditText
     */
    @JvmStatic
    fun showSoftKeyboard(editText: EditText) {
        editText.requestFocus()
        val imm = editText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }

    /**
     * 切换软键盘显示状态
     *
     * @param context 上下文
     */
    @JvmStatic
    fun toggleSoftKeyboard(context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    /**
     * 判断软键盘是否显示
     *
     * @param activity Activity
     * @return 软键盘是否显示
     */
    @JvmStatic
    fun isSoftKeyboardVisible(activity: Activity): Boolean {
        val rootView = activity.findViewById<View>(android.R.id.content)
        val rect = Rect()
        rootView.getWindowVisibleDisplayFrame(rect)
        val screenHeight = rootView.height
        val keypadHeight = screenHeight - rect.bottom
        return keypadHeight > screenHeight * 0.15
    }

    /**
     * 设置View的可见性
     *
     * @param view View
     * @param visible 是否可见
     */
    @JvmStatic
    fun setViewVisibility(view: View?, visible: Boolean) {
        view?.visibility = if (visible) View.VISIBLE else View.GONE
    }

    /**
     * 设置View的可见性（INVISIBLE）
     *
     * @param view View
     * @param visible 是否可见
     */
    @JvmStatic
    fun setViewVisibilityInvisible(view: View?, visible: Boolean) {
        view?.visibility = if (visible) View.VISIBLE else View.INVISIBLE
    }

    /**
     * 获取View在屏幕中的位置
     *
     * @param view View
     * @return 位置坐标[x, y]
     */
    @JvmStatic
    fun getViewLocationOnScreen(view: View): IntArray {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        return location
    }

    /**
     * 获取View在窗口中的位置
     *
     * @param view View
     * @return 位置坐标[x, y]
     */
    @JvmStatic
    fun getViewLocationInWindow(view: View): IntArray {
        val location = IntArray(2)
        view.getLocationInWindow(location)
        return location
    }

    /**
     * 测量View的尺寸
     *
     * @param view View
     * @return 尺寸[width, height]
     */
    @JvmStatic
    fun measureView(view: View): IntArray {
        val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        view.measure(widthMeasureSpec, heightMeasureSpec)
        return intArrayOf(view.measuredWidth, view.measuredHeight)
    }

    /**
     * 设置View的外边距
     *
     * @param view View
     * @param left 左边距
     * @param top 上边距
     * @param right 右边距
     * @param bottom 下边距
     */
    @JvmStatic
    fun setViewMargins(view: View, left: Int, top: Int, right: Int, bottom: Int) {
        val layoutParams = view.layoutParams
        if (layoutParams is ViewGroup.MarginLayoutParams) {
            layoutParams.setMargins(left, top, right, bottom)
            view.layoutParams = layoutParams
        }
    }

    /**
     * 设置View的内边距
     *
     * @param view View
     * @param left 左内边距
     * @param top 上内边距
     * @param right 右内边距
     * @param bottom 下内边距
     */
    @JvmStatic
    fun setViewPadding(view: View, left: Int, top: Int, right: Int, bottom: Int) {
        view.setPadding(left, top, right, bottom)
    }

    /**
     * 获取屏幕宽度
     *
     * @param context 上下文
     * @return 屏幕宽度（像素）
     */
    @JvmStatic
    fun getScreenWidth(context: Context): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }

    /**
     * 获取屏幕高度
     *
     * @param context 上下文
     * @return 屏幕高度（像素）
     */
    @JvmStatic
    fun getScreenHeight(context: Context): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    /**
     * 获取屏幕真实尺寸（包含导航栏）
     *
     * @param context 上下文
     * @return 屏幕尺寸[width, height]
     */
    @JvmStatic
    fun getRealScreenSize(context: Context): IntArray {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealSize(size)
        } else {
            display.getSize(size)
        }
        
        return intArrayOf(size.x, size.y)
    }

    /**
     * 获取屏幕密度
     *
     * @param context 上下文
     * @return 屏幕密度
     */
    @JvmStatic
    fun getScreenDensity(context: Context): Float {
        return context.resources.displayMetrics.density
    }

    /**
     * 获取屏幕DPI
     *
     * @param context 上下文
     * @return 屏幕DPI
     */
    @JvmStatic
    fun getScreenDPI(context: Context): Int {
        return context.resources.displayMetrics.densityDpi
    }

    /**
     * 判断是否为横屏
     *
     * @param context 上下文
     * @return 是否为横屏
     */
    @JvmStatic
    fun isLandscape(context: Context): Boolean {
        return context.resources.configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE
    }

    /**
     * 判断是否为竖屏
     *
     * @param context 上下文
     * @return 是否为竖屏
     */
    @JvmStatic
    fun isPortrait(context: Context): Boolean {
        return context.resources.configuration.orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT
    }

    /**
     * 设置状态栏透明
     *
     * @param activity Activity
     */
    @JvmStatic
    fun setStatusBarTransparent(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.statusBarColor = Color.TRANSPARENT
            activity.window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            )
        }
    }

    /**
     * 设置状态栏颜色
     *
     * @param activity Activity
     * @param color 颜色值
     */
    @JvmStatic
    fun setStatusBarColor(activity: Activity, @ColorInt color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.statusBarColor = color
        }
    }

    /**
     * 设置状态栏文字颜色为深色
     *
     * @param activity Activity
     * @param dark 是否为深色
     */
    @JvmStatic
    fun setStatusBarTextDark(activity: Activity, dark: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decorView = activity.window.decorView
            var flags = decorView.systemUiVisibility
            flags = if (dark) {
                flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
            decorView.systemUiVisibility = flags
        }
    }

    /**
     * 隐藏导航栏
     *
     * @param activity Activity
     */
    @JvmStatic
    fun hideNavigationBar(activity: Activity) {
        activity.window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        )
    }

    /**
     * 显示导航栏
     *
     * @param activity Activity
     */
    @JvmStatic
    fun showNavigationBar(activity: Activity) {
        activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
    }

    /**
     * 设置全屏
     *
     * @param activity Activity
     */
    @JvmStatic
    fun setFullScreen(activity: Activity) {
        activity.window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        )
    }

    /**
     * 退出全屏
     *
     * @param activity Activity
     */
    @JvmStatic
    fun exitFullScreen(activity: Activity) {
        activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
    }

    /**
     * 颜色加深
     *
     * @param color 原始颜色
     * @param factor 加深因子（0-1）
     * @return 加深后的颜色
     */
    @JvmStatic
    @ColorInt
    fun darkenColor(@ColorInt color: Int, factor: Float): Int {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        hsv[2] *= (1f - factor)
        return Color.HSVToColor(hsv)
    }

    /**
     * 颜色变亮
     *
     * @param color 原始颜色
     * @param factor 变亮因子（0-1）
     * @return 变亮后的颜色
     */
    @JvmStatic
    @ColorInt
    fun lightenColor(@ColorInt color: Int, factor: Float): Int {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        hsv[2] = (hsv[2] + (1f - hsv[2]) * factor).coerceAtMost(1f)
        return Color.HSVToColor(hsv)
    }

    /**
     * 设置颜色透明度
     *
     * @param color 原始颜色
     * @param alpha 透明度（0-255）
     * @return 设置透明度后的颜色
     */
    @JvmStatic
    @ColorInt
    fun setColorAlpha(@ColorInt color: Int, alpha: Int): Int {
        return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color))
    }

    /**
     * 判断颜色是否为深色
     *
     * @param color 颜色值
     * @return 是否为深色
     */
    @JvmStatic
    fun isDarkColor(@ColorInt color: Int): Boolean {
        val darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255
        return darkness >= 0.5
    }

    /**
     * 获取主题属性值
     *
     * @param context 上下文
     * @param attrRes 属性资源ID
     * @return 属性值
     */
    @JvmStatic
    fun getThemeAttrValue(context: Context, attrRes: Int): TypedValue {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(attrRes, typedValue, true)
        return typedValue
    }

    /**
     * 获取主题属性颜色
     *
     * @param context 上下文
     * @param attrRes 属性资源ID
     * @return 颜色值
     */
    @JvmStatic
    @ColorInt
    fun getThemeAttrColor(context: Context, attrRes: Int): Int {
        val typedValue = getThemeAttrValue(context, attrRes)
        return typedValue.data
    }

    /**
     * 判断是否为深色主题
     *
     * @param context 上下文
     * @return 是否为深色主题
     */
    @JvmStatic
    fun isDarkTheme(context: Context): Boolean {
        return (context.resources.configuration.uiMode and 
                android.content.res.Configuration.UI_MODE_NIGHT_MASK) == 
                android.content.res.Configuration.UI_MODE_NIGHT_YES
    }

    /**
     * 获取ActionBar高度
     *
     * @param context 上下文
     * @return ActionBar高度（像素）
     */
    @JvmStatic
    fun getActionBarHeight(context: Context): Int {
        val typedValue = TypedValue()
        return if (context.theme.resolveAttribute(android.R.attr.actionBarSize, typedValue, true)) {
            TypedValue.complexToDimensionPixelSize(typedValue.data, context.resources.displayMetrics)
        } else {
            0
        }
    }

    /**
     * 延迟执行UI操作
     *
     * @param view View
     * @param delayMillis 延迟时间（毫秒）
     * @param action 要执行的操作
     */
    @JvmStatic
    fun postDelayed(view: View, delayMillis: Long, action: () -> Unit) {
        view.postDelayed(action, delayMillis)
    }

    /**
     * 在下一帧执行UI操作
     *
     * @param view View
     * @param action 要执行的操作
     */
    @JvmStatic
    fun post(view: View, action: () -> Unit) {
        view.post(action)
    }

    /**
     * 移除View的所有回调
     *
     * @param view View
     * @param action 要移除的回调
     */
    @JvmStatic
    fun removeCallbacks(view: View, action: Runnable) {
        view.removeCallbacks(action)
    }
}