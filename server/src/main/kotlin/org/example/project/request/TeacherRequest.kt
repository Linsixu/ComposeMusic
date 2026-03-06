package org.example.project.request

import kotlinx.serialization.Serializable

/**
 * @author: linsixu@ruqimobility.com
 * @date:2026/3/6
 * 用途：
 */
@Serializable
data class TeacherRequest(
    val institutionName: String,
    val teacherName: String,
    val teacherPhone: String,
    //负责的课题
    val subject: String
)