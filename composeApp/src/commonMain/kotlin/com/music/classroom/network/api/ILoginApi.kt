package com.music.classroom.network.api

import com.music.classroom.network.BaseApi
import com.music.classroom.network.KtorClient
import com.music.classroom.network.res.ApiResponse
import com.music.classroom.network.res.login.LoginTeacherRes
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.Json

/**
 * @author: linsixu@ruqimobility.com
 * @date:2026/3/13
 * 用途：
 */
class ILoginApi {
    private val client = KtorClient.instance

    private val baseUrl = BaseApi.baseUrl

    suspend fun requestTeacherLogin(
        account: String, password: String
    ): LoginTeacherRes? {
        val body = mapOf(
            "account" to account,
            "password" to password
        )
        try {
            val response: ApiResponse<LoginTeacherRes?> = client.post("$baseUrl/login/teacher") {
                contentType(ContentType.Application.Json)
                setBody(body = body)
            }.body()
            println("✅ 接口返回：$response")
            return response.data
        }catch (e: Exception) {
            println("✅ 接口e=$e")
        }
        return null
    }

    suspend fun requestStudentLogin(
        account: String, password: String
    ): Boolean {
        val body = mapOf(
            "account" to account,
            "password" to password
        )
        val response: ApiResponse<Int> = client.post("$baseUrl/login/student") {
            contentType(ContentType.Application.Json)
            setBody(body = body)
        }.body()
        return response.success && response.data == 1
    }
}