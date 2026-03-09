package org.example.project.request

import kotlinx.serialization.Serializable

/**
 * @author: linsixu@ruqimobility.com
 * @date:2026/3/6
 * 用途：
 */
@Serializable
data class CreateStudentReq(
    val studentName: String,
    val phone: String,
    val institutionName: String
)