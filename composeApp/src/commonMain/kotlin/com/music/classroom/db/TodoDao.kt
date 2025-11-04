package com.music.classroom.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.music.classroom.allpage.CourseItem
import kotlinx.coroutines.flow.Flow

/**
 * @author: linsixu@ruqimobility.com
 * @date:2025/11/2
 * 用途：
 */
@Dao
interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: TodoEntity)

    @Query("SELECT count(*) FROM TodoEntity")
    suspend fun count(): Int

    @Query("SELECT * FROM TodoEntity WHERE year =:year and month =:month")
    suspend fun getAllCourseByYearAndMonth(year: Int, month: Int): List<TodoEntity>

    @Query("SELECT * FROM TodoEntity")
    suspend fun getAllAsFlow(): List<TodoEntity>

    @Delete
    suspend fun deleteCourse(item: TodoEntity): Int

    @Update
    suspend fun updateCourse(item: TodoEntity): Int
}