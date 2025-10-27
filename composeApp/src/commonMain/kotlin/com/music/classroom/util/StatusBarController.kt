// shared/src/commonMain/kotlin/com/music/classroom/util/StatusBarController.kt
package com.music.classroom.util

import androidx.compose.ui.graphics.Color

// 跨平台状态栏控制接口（expect 声明）
expect class StatusBarController() {
    // 方法1：设置状态栏背景色
    fun setStatusBarColor(color: Color)
    // 方法2：设置状态栏文字是否为深色（适配背景色对比度）
    fun setStatusBarTextDark(isDark: Boolean)
}

// 工具函数：判断颜色是否为浅色（双端共用，用于决定文字颜色）
fun isLightColor(color: Color): Boolean {
    // 计算颜色亮度（0-255），亮度>128视为浅色
    val luminance = (0.299 * color.red + 0.587 * color.green + 0.114 * color.blue) * 255
    return luminance > 128
}