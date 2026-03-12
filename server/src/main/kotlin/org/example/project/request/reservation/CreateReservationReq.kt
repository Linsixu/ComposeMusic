package org.example.project.request.reservation

import kotlinx.serialization.Serializable

/**
 * @author: linsixu@ruqimobility.com
 * @date:2026/3/11
 * 用途：
 */
@Serializable
data class CreateReservationReq(
    val classId: Long,
    val templateId: Long,
    val institutionId: Long,
    val teacherId: Long,
    val studentName: String,
    val phone: String
)