package com.music.classroom.db

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSError
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.nativeHeap.alloc

/**
 * @author: linsixu@ruqimobility.com
 * @date:2025/11/2
 * 用途：
 */
fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFilePath = documentDirectory() + "/music_room.db"
    return Room.databaseBuilder<AppDatabase>(
        name = dbFilePath,
    ).setDriver(BundledSQLiteDriver()).setQueryCoroutineContext(Dispatchers.IO)
        //.enableMultiInstanceInvalidation()
        //.addMigrations(MIGRATION_1_2)
}

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
    memScoped {
        val errorVar = alloc<ObjCObjectVar<NSError?>>()    // 检查查询错误
        val documentDirectoryURL = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = errorVar.ptr,
        )
        println("magic path：${documentDirectoryURL?.path ?: "empty path"}")
        // 检查错误
        if (documentDirectoryURL == null) {
            val error = errorVar.value
            println("magic 获取目录失败：${error?.localizedDescription ?: "未知错误"}")
        }
        return requireNotNull(documentDirectoryURL).path!!
    }
    return ""
}

// The Room compiler generates the `actual` implementations.
@Suppress("KotlinNoActualForExpect")
actual object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    actual override fun initialize(): AppDatabase {
        return getDatabaseBuilder().build()
    }
}