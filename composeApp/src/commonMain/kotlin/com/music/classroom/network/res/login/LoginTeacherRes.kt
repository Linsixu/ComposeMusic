package com.music.classroom.network.res.login

import kotlinx.serialization.Serializable

/**
 * @author: linsixu@ruqimobility.com
 * @date:2026/3/14
 * 用途：
 */
@Serializable
data class LoginTeacherRes(
    val teacherId: Long,
    val teacherName: String,
    val teacherPhone: String
)

