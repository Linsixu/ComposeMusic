package com.music.classroom.network.req.login

import kotlinx.serialization.Serializable

/**
 * @author: linsixu@ruqimobility.com
 * @date:2026/3/13
 * 用途：
 */
@Serializable
data class LoginRequest(
    val account: String,
    val password: String
)