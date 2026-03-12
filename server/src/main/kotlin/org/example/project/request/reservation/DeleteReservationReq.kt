package org.example.project.request.reservation

import kotlinx.serialization.Serializable

/**
 * @author: linsixu@ruqimobility.com
 * @date:2026/3/12
 * 用途：删除预约表中数据的请求
 */
@Serializable
data class DeleteReservationReq(
    val reservationId: Long,
    val classId: Long,
    val studentName: String,
    val teacherName: String
//    val studentPhone: String
)