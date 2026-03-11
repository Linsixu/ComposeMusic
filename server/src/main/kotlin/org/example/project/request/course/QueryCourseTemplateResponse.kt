package org.example.project.request.course

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.serializers.LocalDateTimeIso8601Serializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @author: linsixu@ruqimobility.com
 * @date:2026/3/11
 * 用途：查询课程模版的请求
 */
@Serializable
data class QueryCourseTemplateResponse(
    val templateName: String,
    val templateId: Long? = null,
    val institutionId: Long,
    val teacherName: String,
    val subject: String,
    @SerialName("classDuration")
    val classDuration: Int = 60,
    val description: String? = null,
    @Serializable(with = LocalDateTimeIso8601Serializer::class)
    val createTime: LocalDateTime? = null
)
