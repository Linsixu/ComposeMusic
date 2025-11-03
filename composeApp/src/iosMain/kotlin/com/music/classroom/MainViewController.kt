// shared/src/iosMain/kotlin/com/music/classroom/MainViewController.kt
package com.music.classroom

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.uikit.ComposeUIViewControllerDelegate
import androidx.compose.ui.window.ComposeUIViewController
import com.music.classroom.db.AppContainer
import com.music.classroom.db.AppDatabaseConstructor
import com.music.classroom.db.DbSingleton.LocalAppContainer
import platform.UIKit.UIStatusBarStyle
import platform.UIKit.UIStatusBarStyleDarkContent

fun MainViewController(appContainer: AppContainer) = ComposeUIViewController{
    // 你的 Compose 根组件
    CompositionLocalProvider(
        LocalAppContainer provides appContainer,
    ) {
        App()
    }
}