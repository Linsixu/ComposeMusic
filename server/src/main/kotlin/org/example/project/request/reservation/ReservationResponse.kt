package org.example.project.request.reservation

import kotlinx.serialization.Serializable

/**
 * @author: linsixu@ruqimobility.com
 * @date:2026/3/12
 * 用途：预约记录表显示数据
 */
@Serializable
data class ReservationResponse(
    val reservationId: Long? = null,
    val teacherName: String,
    val studentName: String,
    val status: Int = 1,
    val startMillisecondTime: Long,
    val duration: Int,
    val reservationTime: Long
)