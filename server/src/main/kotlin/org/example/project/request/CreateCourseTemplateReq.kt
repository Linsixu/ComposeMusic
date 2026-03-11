package org.example.project.request

import kotlinx.serialization.Serializable

/**
 * @author: linsixu@ruqimobility.com
 * @date:2026/3/11
 * 用途：
 */
@Serializable
data class CreateCourseTemplateReq(
    /**
     * 模版title
     */
    val templateName: String,
    /**
     * 科目
     */
    val subject: String,
    val teacherName: String,
    val classDuration: Int,
    val description: String
)