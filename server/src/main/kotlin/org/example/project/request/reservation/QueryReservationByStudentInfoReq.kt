package org.example.project.request.reservation

import kotlinx.serialization.Serializable

/**
 * @author: linsixu@ruqimobility.com
 * @date:2026/3/12
 * 用途：
 */
@Serializable
data class QueryReservationByStudentInfoReq(
    val studentName: String,
    val studentPhone: String
)