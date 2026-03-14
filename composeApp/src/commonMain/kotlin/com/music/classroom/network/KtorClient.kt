package com.music.classroom.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * @author: linsixu@ruqimobility.com
 * @date:2026/3/13
 * 用途：网络引擎（全局单例，适配 kotlin 2.3.0）
 */
object KtorClient {
    val instance = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                isLenient = true
                encodeDefaults = true
                ignoreUnknownKeys = true // 👈 建议开启，防止后端多传字段导致崩溃
            })
        }
        install(Logging) {
            level = LogLevel.ALL
        }
    }
}