// shared/src/iosMain/kotlin/com/music/classroom/MainViewController.kt
package com.music.classroom

import androidx.compose.ui.uikit.ComposeUIViewControllerDelegate
import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIStatusBarStyle
import platform.UIKit.UIStatusBarStyleDarkContent

fun MainViewController() = ComposeUIViewController{
    // 你的 Compose 根组件
    App()
}