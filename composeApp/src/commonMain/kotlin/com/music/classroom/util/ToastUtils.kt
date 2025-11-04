package com.music.classroom.util

/**
 * @author: linsixu@ruqimobility.com
 * @date:2025/11/4
 * 用途：
 */
// 抽象声明：跨平台 Toast 函数
expect fun showToast(message: String, duration: ToastDuration = ToastDuration.SHORT)

// 枚举：定义显示时长（短/长），统一跨平台行为
enum class ToastDuration {
    SHORT, LONG
}