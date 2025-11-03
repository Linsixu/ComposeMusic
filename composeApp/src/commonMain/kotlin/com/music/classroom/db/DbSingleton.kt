package com.music.classroom.db

import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

/**
 * @author: linsixu@ruqimobility.com
 * @date:2025/11/2
 * 用途：
 */
object DbSingleton {
    val LocalAppContainer =
        staticCompositionLocalOf<AppContainer> { error("No AppContainer provided!") }

    val staticSQLIO = CoroutineScope(Dispatchers.IO)
}