package com.music.classroom.util

import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.cValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import platform.CoreGraphics.CGRect
import platform.CoreGraphics.CGRectGetHeight
import platform.CoreGraphics.CGRectGetWidth
import platform.CoreGraphics.CGRectMake
import platform.CoreGraphics.CGSizeMake
import platform.UIKit.NSTextAlignmentCenter
import platform.UIKit.UIAlertController
import platform.UIKit.UIApplication
import platform.UIKit.UIColor
import platform.UIKit.UIFont
import platform.UIKit.UILabel
import platform.UIKit.UIScreen
import platform.UIKit.UIView
import platform.UIKit.UIViewController
import platform.UIKit.UIWindow
import platform.UIKit.UIWindowLevelAlert
import kotlin.math.min

/**
 * @author: linsixu@ruqimobility.com
 * @date:2025/11/4
 * 用途：
 */
@OptIn(ExperimentalForeignApi::class)
actual fun showToast(message: String, duration: ToastDuration) {
    val duration = if (duration == ToastDuration.SHORT) {
        2
    } else {
        3
    }
    showCustomToast(message, duration.toDouble())
}

// 自定义 iOS Toast（简化版）
@OptIn(ExperimentalForeignApi::class)
private fun showCustomToast(message: String, duration: Double) {
    val topWindow = UIApplication.sharedApplication.keyWindow as? UIWindow
        ?: UIApplication.sharedApplication.windows?.filterIsInstance<UIWindow>()?.lastOrNull()
        ?: UIWindow()

    // 3. 获取屏幕尺寸（通过 UIScreen 确保正确）
    val mainScreen = UIScreen.mainScreen
    val screenBounds = mainScreen?.bounds ?: cValue { CGRectMake(0.0, 0.0, 320.0, 568.0) }
    val screenWidth = CGRectGetWidth(screenBounds)
    val screenHeight = CGRectGetHeight(screenBounds)

    // 4. 创建专用 Toast 窗口（确保层级最高）
    val toastWindow = UIWindow(frame = screenBounds).apply {
        windowLevel = UIWindowLevelAlert + 1000.0 // 远高于其他窗口
        backgroundColor = null // 透明背景
        rootViewController = UIViewController() // 必须设置根控制器，否则可能不显示
    }

    // 5. 构建 Toast 视图（确保可见）
    val toastWidth = min(screenWidth - 40.0, 300.0)
    val toastView = UIView(frame = CGRectMake(
        x = (screenWidth - toastWidth) / 2.0,
        y = screenHeight - 120.0, // 底部上方120pt，避免被底部安全区遮挡
        width = toastWidth,
        height = 44.0
    )).apply {
        // 1. 背景色（半透明黑色）
        backgroundColor = UIColor.blackColor().colorWithAlphaComponent(0.7)

        // 2. 圆角设置（核心）
        layer?.cornerRadius = 12.0 // 圆角半径，数值越大越圆润
        clipsToBounds = true // 关键：启用裁剪，确保圆角生效

        // 3. 可选：添加阴影（与圆角搭配更美观）
        // 注意：阴影需关闭 clipsToBounds，改用 masksToBounds = false
        layer?.masksToBounds = false // 允许阴影显示在视图外
        layer?.shadowColor = UIColor.blackColor().CGColor
        layer?.shadowOpacity = 0.2f // 阴影透明度
        layer?.shadowRadius = 6.0 // 阴影模糊半径
        layer?.shadowOffset = CGSizeMake(0.0, 2.0) // 阴影偏移（向下2pt）
    }

    // 6. 添加文本标签（确保文字可见）
    val label = UILabel(frame = toastView.bounds).apply {
        text = message
        textColor = UIColor.whiteColor()
        textAlignment = NSTextAlignmentCenter
        font = UIFont.systemFontOfSize(14.0)
        numberOfLines = 0 // 支持多行文本
    }
    toastView.addSubview(label)
    toastWindow.rootViewController?.view?.addSubview(toastView)

    // 7. 关键：显示窗口并激活
    toastWindow.makeKeyAndVisible()
    topWindow.addSubview(toastWindow) // 附加到顶层窗口，确保显示


    GlobalScope.launch(Dispatchers.Main) {
        // 8. 延迟后消失（带淡出动画）
        delay((duration * 1000).toLong())
        UIView.animateWithDuration(0.3) {
            toastView.alpha = 0.0
        }
        delay(300) // 等待动画完成
        toastView.removeFromSuperview()
        toastWindow.removeFromSuperview()
        toastWindow.hidden = true
    }
}