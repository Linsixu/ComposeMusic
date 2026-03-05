package org.example.project.request

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.serializers.LocalDateTimeIso8601Serializer
import kotlinx.serialization.Serializable

/**
 * @author: linsixu@ruqimobility.com
 * @date:2026/3/5
 * 用途：
 */
@Serializable
data class PublishCourseRequest(
    val templateId: Long? = null,
    val templateName: String,
    val subject: String? = null,
    val teacherName: String,
    val classDuration: Int = 60,
    val description: String? = null,
    @Serializable(with = LocalDateTimeIso8601Serializer::class)
    val createTime: LocalDateTime? = null,
    @Serializable(with = LocalDateTimeIso8601Serializer::class)
    val updateTime: LocalDateTime? = null
)