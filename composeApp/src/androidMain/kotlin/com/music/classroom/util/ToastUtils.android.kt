package com.music.classroom.util

import android.widget.Toast
import com.music.classroom.AppUtils

/**
 * @author: linsixu@ruqimobility.com
 * @date:2025/11/4
 * 用途：
 */
actual fun showToast(message: String, duration: ToastDuration) {
    // 获取全局 Context（确保在非 UI 线程也能调用）
    val context = AppUtils.application ?: return

    // 根据 duration 转换为 Android Toast 的时长
    val androidDuration = when (duration) {
        ToastDuration.SHORT -> Toast.LENGTH_SHORT
        ToastDuration.LONG -> Toast.LENGTH_LONG
    }

    // 确保在主线程显示 Toast（Android 要求）
    android.os.Handler(context.mainLooper).post {
        Toast.makeText(context, message, androidDuration).show()
    }
}