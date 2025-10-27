// shared/src/iosMain/kotlin/com/music/classroom/MainViewController.kt
package com.music.classroom

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.ComposeUIViewController
import com.music.classroom.util.StatusBarController
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.valueForKey
import platform.UIKit.UIApplication
import platform.UIKit.UIDevice
import platform.UIKit.UIViewController
import platform.UIKit.UIWindow
import platform.UIKit.addChildViewController
import platform.UIKit.didMoveToParentViewController

// 1. 自定义 ViewController，继承自 UIViewController，用于重写生命周期方法
class ComposeContainerViewController(
    private val statusBarController: StatusBarController,
    private val content: @Composable () -> Unit
) : UIViewController(nibName = null, bundle = null) {
    // 2. 重写 Objective-C 原生的 viewDidLoad 方法（Kotlin 中用 @Override 标注）
    @OptIn(ExperimentalForeignApi::class)
    override fun viewDidLoad() {
        super.viewDidLoad()
        // 3. 初始化 Compose 内容，并添加到当前 ViewController 的视图中
        val composeViewController = ComposeUIViewController { content() }
        // 将 Compose 视图控制器的视图添加到当前视图中
        addChildViewController(composeViewController)
//        composeViewController.view.frame = view.bounds // 充满屏幕
        composeViewController.view.setFrame(view.bounds) // 使用 setFrame 方法
        view.addSubview(composeViewController.view)
        composeViewController.didMoveToParentViewController(this)

        // 4. 初始化状态栏控制器（获取当前 VC 和 Window）
        val currentVC = this // 当前自定义 ViewController
        val currentWindow = getCurrentKeyWindow() // 获取关键窗口

        currentWindow?.let {
            statusBarController.init(vc = currentVC, window = it)
        }
    }

    // 5. 获取当前活跃的关键窗口（适配 iOS 13+ 和旧版本）
    private fun getCurrentKeyWindow(): UIWindow? {
        val windows = (UIApplication.sharedApplication.windows as? List<UIWindow>) ?: return null
        for (window in windows) {
            if (isKeyWindow(window)) {
                return window
            }
        }
        return windows.firstOrNull()
    }

    // 6. 判断窗口是否为关键窗口（替代 #available 语法）
    private fun isKeyWindow(window: UIWindow): Boolean {
        return try {
            // 优先判断 iOS 13+ 的 windowScene.keyWindow
            val windowScene = window.valueForKey("windowScene") as? UIWindow
            if (windowScene != null) {
                val keyWindow = windowScene.valueForKey("keyWindow") as? UIWindow
                keyWindow === window
            } else {
                // 旧版本判断 isKeyWindow
                (window.valueForKey("isKeyWindow") as? Boolean) ?: false
            }
        } catch (e: Exception) {
            // 异常时降级到旧版本判断
            (window.valueForKey("isKeyWindow") as? Boolean) ?: false
        }
    }
}

// 7. iOS 入口函数：返回自定义 ViewController
fun MainViewController(): UIViewController {
    val statusBarController = StatusBarController()
    return ComposeContainerViewController(
        statusBarController = statusBarController,
        content = {
            // 传入状态栏控制器到 App 组件
            App(statusBarController = statusBarController)
        }
    )
}

// 8. 判断 iOS 版本是否为 13+
private fun isIos13OrLater(): Boolean {
    val systemVersion = UIDevice.currentDevice.systemVersion.toDoubleOrNull() ?: 0.0
    return systemVersion >= 13.0
}