// shared/src/androidMain/kotlin/com/music/classroom/util/StatusBarController.android.kt
package com.music.classroom.util

import android.app.Activity
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView

// Android 平台接口实现（actual）
actual class StatusBarController actual constructor() {
    // 缓存当前Activity的Window（需从Compose视图中获取）
    private lateinit var window: Window

    // 初始化：绑定当前Activity的Window（需在Compose组件中调用）
    fun init(activity: Activity) {
        this.window = activity.window
        // 可选：设置状态栏为沉浸式（无半透明遮罩）
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }

    // 实现：设置状态栏背景色
    actual fun setStatusBarColor(color: Color) {
        if (::window.isInitialized) {
            window.statusBarColor = color.toArgb()
        }
    }

    // 实现：设置状态栏文字是否为深色
    actual fun setStatusBarTextDark(isDark: Boolean) {
        if (::window.isInitialized) {
            val decorView = window.decorView
            var flags = decorView.systemUiVisibility
            flags = if (isDark) {
                // 浅色背景 → 深色文字（添加浅色状态栏标志）
                flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                // 深色背景 → 白色文字（清除标志）
                flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
            decorView.systemUiVisibility = flags
        }
    }
}