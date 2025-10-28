// shared/src/iosMain/kotlin/com/music/classroom/util/StatusBarController.ios.kt
package com.music.classroom.util

import androidx.compose.ui.graphics.Color
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.UnsafeNumber
import platform.Foundation.NSSelectorFromString
import platform.Foundation.setValue
import platform.UIKit.UIColor
import platform.UIKit.UIStatusBarStyle
import platform.UIKit.UIStatusBarStyleDarkContent
import platform.UIKit.UIStatusBarStyleLightContent
import platform.UIKit.UIViewController
import platform.UIKit.UIWindow
import platform.UIKit.UIApplication
import platform.UIKit.UIDevice
import platform.darwin.NSObject
import platform.darwin.sel_registerName
import platform.objc.objc_getClass
import platform.objc.objc_msgSend

// iOS 状态栏控制器实现（Kotlin 反射适配）
actual class StatusBarController actual constructor() {
    // 缓存 iOS 关键对象：顶层VC（控制文字）、窗口（控制背景）
    private var topViewController: UIViewController? = null
    private var keyWindow: UIWindow? = null

    /**
     * 初始化：绑定 iOS 顶层 VC 和关键窗口（需在 iOS 入口调用）
     * @param vc 顶层 UIViewController（控制状态栏文字样式）
     * @param window 当前活跃窗口（控制状态栏背景色）
     */
    fun init(vc: UIViewController, window: UIWindow) {
        this.topViewController = vc
        this.keyWindow = window
        // 允许 VC 控制状态栏（必须配置，否则文字样式不生效）
        setupViewControllerStatusBarPermission(vc)
    }

    /**
     * 实现：设置状态栏背景色（仅 iOS 13+ 支持）
     */
    @OptIn(ExperimentalForeignApi::class)
    actual fun setStatusBarColor(color: Color) {
        if (!isIos13OrLater()) return // 低于13版本不支持背景色，直接返回

        keyWindow?.let { window ->
            // 1. Compose Color 转 iOS UIColor
            val iosColor = UIColor(
                red = color.red.toDouble(),
                green = color.green.toDouble(),
                blue = color.blue.toDouble(),
                alpha = color.alpha.toDouble()
            )

            // 2. 反射调用 window 的 setStatusBarBackgroundColor: 方法（规避 Kotlin 语法差异）
            val setBgSelector = NSSelectorFromString("setStatusBarBackgroundColor:")
            if (window.respondsToSelector(setBgSelector)) {
                window.performSelector(setBgSelector, iosColor)
            }

            // 3. 刷新状态栏外观
            topViewController?.performSelector(NSSelectorFromString("setNeedsStatusBarAppearanceUpdate"))
        }
    }

    /**
     * 实现：设置状态栏文字颜色（深色/浅色）
     * @param isDark true：深色文字（适配浅色背景）；false：白色文字（适配深色背景）
     */
    @OptIn(ExperimentalForeignApi::class)
    actual fun setStatusBarTextDark(isDark: Boolean) {
        val targetStyle = if (isDark) {
            UIStatusBarStyleDarkContent // 深色文字
        } else {
            UIStatusBarStyleLightContent // 白色文字
        }

        // 方式1：通过顶层 VC 控制状态栏样式
        topViewController?.let { vc ->
            // 1. 设置 statusBarStyle 属性（用 setValue 传递参数）
            vc.setValue(targetStyle, forKey = "statusBarStyle")
            // 2. 刷新状态栏
            vc.performSelector(NSSelectorFromString("setNeedsStatusBarAppearanceUpdate"))
        }

        // 方式2：全局设置（兼容旧版本）
        val app = UIApplication.sharedApplication
        // 1. 设置全局状态栏样式（用 setValue 传递参数）
        app.setValue(targetStyle, forKey = "statusBarStyle")
        // 2. 刷新状态栏（带动画）
        app.setValue(true, forKey = "statusBarHidden") // 触发刷新
        app.setValue(false, forKey = "statusBarHidden")
    }

    // ------------------------------ 私有辅助方法 ------------------------------
    /**
     * 配置 VC 权限：允许控制状态栏
     */
    private fun setupViewControllerStatusBarPermission(vc: UIViewController) {
        // 反射设置 prefersStatusBarHidden（是否隐藏状态栏：false = 显示）
        setVCProperty(vc, "prefersStatusBarHidden", false)
        // 反射设置 modalPresentationCapturesStatusBarAppearance（允许模态VC控制状态栏）
        setVCProperty(vc, "modalPresentationCapturesStatusBarAppearance", true)
    }

    /**
     * 反射给 UIViewController 设置属性（解决 Kotlin 直接赋值报错）
     */
    @OptIn(ExperimentalForeignApi::class)
    private fun setVCProperty(vc: UIViewController, propertyName: String, value: Any) {
        // iOS 属性 Setter 命名规则：set + 首字母大写属性名 + :
        val setterName = "set${propertyName.replaceFirstChar { it.uppercase() }}:"
        val selector = NSSelectorFromString(setterName)

        if (vc.respondsToSelector(selector)) {
            when (value) {
                is Boolean -> vc.performSelector(selector, value)
                is Int -> vc.performSelector(selector, value)
                else -> return // 暂不支持其他类型
            }
        }
    }

    /**
     * 判断是否为 iOS 13+（背景色仅支持13+）
     */
    private fun isIos13OrLater(): Boolean {
        val systemVersion = UIDevice.currentDevice.systemVersion.toDoubleOrNull() ?: 0.0
        return systemVersion >= 13.0
    }
}