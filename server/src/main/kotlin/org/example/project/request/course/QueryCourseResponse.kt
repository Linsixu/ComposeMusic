package org.example.project.request.course

import kotlinx.serialization.Serializable

/**
 * @author: linsixu@ruqimobility.com
 * @date:2026/3/11
 * 用途：
 */
@Serializable
class QueryCourseResponse(
    val classId: Long,
    val templateId: Long,
    val teacherName: String,
    val startMillisecondTime: Long,
    val duration: Int,
    val status: Int
)