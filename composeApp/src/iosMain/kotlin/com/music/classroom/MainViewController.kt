// shared/src/iosMain/kotlin/com/music/classroom/MainViewController.kt
package com.music.classroom

import androidx.compose.ui.uikit.ComposeUIViewControllerDelegate
import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIStatusBarStyle
import platform.UIKit.UIStatusBarStyleDarkContent

fun MainViewController() = ComposeUIViewController(
    // 配置状态栏文字样式为黑色（核心1）
    configure = {
        delegate = object: ComposeUIViewControllerDelegate {
            override val preferredStatusBarStyle: UIStatusBarStyle?
                get() = UIStatusBarStyleDarkContent

            override val prefersStatusBarHidden: Boolean?
                get() = false
        }
    }
) {
    // 你的 Compose 根组件
    App()
}