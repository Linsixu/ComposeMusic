package org.example.project.db.request

import kotlinx.serialization.Serializable

@Serializable
data class UserRequest(
    val name: String = "",
    val parentPhone: String = "",
    val sex: Int = 0
)