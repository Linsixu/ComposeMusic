package com.music.classroom.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @author: linsixu@ruqimobility.com
 * @date:2025/11/2
 * 用途：
 */
@Serializable
@Entity
data class TodoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @SerialName("student_name")
    val studentName: String,    // 学生姓名
    @SerialName("music_tool_name")
    val musicToolName: String, //乐器种类
    @SerialName("grade_range")
    val gradeRange: String,     // 考级范围
    @SerialName("sign_in_status")
    var signInStatus: Int = 0,      //签到状态，0：未签到；1：签到
    @SerialName("start_time")
    val startTime: Long, // 开始时间（LocalDateTime类型）
    @SerialName("lesson_minute")
    val lessonMinute: Int,        // 课时（单位：分钟）
    @SerialName("teacher_name")
    val teacherName: String? = "", //老师名字
    // 新增：年份和月份（建议与startTime关联，确保数据一致性）
    @SerialName("year")
    val year: Int,  // 从startTime提取年份（如2025）
    @SerialName("month")
    val month: Int // 从startTime提取月份（1-12）
)