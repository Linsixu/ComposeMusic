package com.music.classroom.db

import com.music.classroom.allpage.CourseItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

/**
 * @author: linsixu@ruqimobility.com
 * @date:2025/11/2
 * 用途：
 */
class CourseRepository(
    private val database: AppDatabase,
    private val scope: CoroutineScope,
) {
    // 插入课程（调用 DAO 方法，底层已通过 getRoomDatabase 配置的 IO 协程执行）
    suspend fun insertCourse(course: CourseItem) {
        withContext(Dispatchers.IO) {
            database.getDao().insert(course.toTodoEntity())
        }
    }

    // 查询所有课程
    suspend fun getAllCoursesByYearAndMonth(year: Int, month: Int): Flow<List<TodoEntity>> {
        return database.getDao().getAllCourseByYearAndMonth(year, month)
    }

    fun getAllAsFlow(): Flow<List<TodoEntity>> {
        return database.getDao().getAllAsFlow()
    }
}

@OptIn(ExperimentalTime::class)
fun CourseItem.toTodoEntity(): TodoEntity {
    return TodoEntity(
        studentName = this.studentName,
        musicToolName = this.musicToolName,
        gradeRange = this.gradeRange,
        signInStatus = this.signInStatus,
        startTime = this.startTime.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds(),
        lessonMinute = this.lessonMinute,
        teacherName = this.teacherName,
        year = this.startTime.year,
        month = this.startTime.monthNumber
    )
}

@OptIn(ExperimentalTime::class)
fun TodoEntity.toCourseItem(): CourseItem {
    val startLocalDataTime = Instant.fromEpochMilliseconds(this.startTime)
        .toLocalDateTime(TimeZone.currentSystemDefault())
    return CourseItem(
        id = this.id,
        studentName = this.studentName,
        musicToolName = this.musicToolName,
        gradeRange = this.gradeRange,
        signInStatus = this.signInStatus,
        startTime = startLocalDataTime,
        lessonMinute = this.lessonMinute,
        teacherName = this.teacherName,
        year = startLocalDataTime.year,
        month = startLocalDataTime.month.ordinal
    )
}

fun List<TodoEntity>.toCourseItem(): List<CourseItem> {
    val list = ArrayList<CourseItem>()
    this.forEach {
        list.add(it.toCourseItem())
    }
    return list
}