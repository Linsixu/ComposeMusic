package com.music.classroom.network.res

import kotlinx.serialization.Serializable

/**
 * @author: linsixu@ruqimobility.com
 * @date:2026/3/13
 * 用途：
 */
@Serializable
data class ApiResponse<T>(
    val success: Boolean,
    val code: Int = 200,
    val data: T? = null,
    val message: String? = null
)