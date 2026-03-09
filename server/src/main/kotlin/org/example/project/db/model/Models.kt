package org.example.project.db.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.serializers.LocalDateTimeIso8601Serializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.example.project.SqlCode.success_code

// 基础响应模型
@Serializable
data class ApiResponse<T>(
    val success: Boolean,
    val code: Int = success_code,
    val data: T? = null,
    val message: String? = null
)

// 教育机构模型
@Serializable
data class EduInstitution(
    val institutionId: Long? = null,
    val institutionName: String,
    val institutionAddress: String? = null,
    val contactPhone: String? = null,
    @Serializable(with = LocalDateTimeIso8601Serializer::class)
    val createTime: LocalDateTime? = null,
    @Serializable(with = LocalDateTimeIso8601Serializer::class)
    val updateTime: LocalDateTime? = null
)

// 老师模型
@Serializable
data class Teacher(
    val teacherId: Long? = null,
    @SerialName("teacherName")
    val teacherName: String,
    val teacherPhone: String? = null,
    val subject: String? = null,
    val institutionId: Long,
    @Serializable(with = LocalDateTimeIso8601Serializer::class)
    val createTime: LocalDateTime? = null,
    @Serializable(with = LocalDateTimeIso8601Serializer::class)
    val updateTime: LocalDateTime? = null
)

// 学生模型
@Serializable
data class Student(
    val studentId: Long? = null,
    val studentName: String,
    val phone: String,
    val institutionId: Long? = null
)

// 课程模板模型
@Serializable
data class CourseTemplate(
    val templateId: Long? = null,
    val templateName: String,
    val subject: String? = null,
    val teacherId: Long,
    @SerialName("classDuration")
    val classDuration: Int = 60,
    val description: String? = null,
    @Serializable(with = LocalDateTimeIso8601Serializer::class)
    val createTime: LocalDateTime? = null,
    @Serializable(with = LocalDateTimeIso8601Serializer::class)
    val updateTime: LocalDateTime? = null
)

// 可预约课时模型
@Serializable
data class CourseClass(
    val classId: Long? = null,
    val templateId: Long,
    val classTimestampMs: Long,
    val status: Int = 1,
    @Serializable(with = LocalDateTimeIso8601Serializer::class)
    val createTime: LocalDateTime? = null,
    @Serializable(with = LocalDateTimeIso8601Serializer::class)
    val updateTime: LocalDateTime? = null
)

// 预约记录模型
@Serializable
data class StudentReservation(
    val reservationId: Long? = null,
    val studentId: Long,
    val classId: Long,
    val reservationTimestampMs: Long,
    val status: Int = 1,
    @Serializable(with = LocalDateTimeIso8601Serializer::class)
    val createTime: LocalDateTime? = null,
    @Serializable(with = LocalDateTimeIso8601Serializer::class)
    val updateTime: LocalDateTime? = null
)

// 抢占课时请求模型
@Serializable
data class ReserveRequest(
    val studentId: Long,
    val classId: Long
)