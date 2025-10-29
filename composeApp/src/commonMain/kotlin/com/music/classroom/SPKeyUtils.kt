package com.music.classroom

import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * @author: linsixu@ruqimobility.com
 * @date:2025/10/28
 * 用途：
 */
object SPKeyUtils {
    const val DEFAULT_LESSON_DEFAULT_TIME = "lesson_default_value"

    const val DEFAULT_MUSIC_TOOLS = "default_music_tools"

    //默认课时
    var currentLessonTime = mutableStateOf(0L)

    //默认乐器值
    var currentMusicTools = mutableStateOf("")
}