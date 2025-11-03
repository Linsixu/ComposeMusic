package com.music.classroom.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.music.classroom.AppUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

/**
 * @author: linsixu@ruqimobility.com
 * @date:2025/11/2
 * 用途：
 */
fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<AppDatabase> {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath("music_room.db")
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}

// The Room compiler generates the `actual` implementations.
//@Suppress("KotlinNoActualForExpect")
//actual object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
//    actual override fun initialize(): AppDatabase {
//        // 关键：使用 Application 上下文（避免内存泄漏，且确保唯一）
//        val androidContext: Context = AppUtils.application.applicationContext
//            ?: throw IllegalStateException("Application context is null")
//
//        // 构建 Room 数据库（仅 Android 平台逻辑，无跨平台代码）
//        return Room.databaseBuilder(
//            context = androidContext,
//            klass = AppDatabase::class.java, // 对应 actual abstract class AppDatabase
//            name = "music_room.db"         // 数据库名称（Room 自动处理路径，无需手动传文件）
//        )
//            .setDriver(BundledSQLiteDriver()).setQueryCoroutineContext(Dispatchers.IO)
////            .fallbackToDestructiveMigration() // 开发阶段允许销毁旧数据（上线前需改为迁移）
//            .build()
//    }
//}