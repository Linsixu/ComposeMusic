package org.example.project

import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json
import org.example.project.db.DatabaseFactory
import org.example.project.db.manager.CourseReservationService
import org.example.project.db.model.ApiResponse
import org.example.project.route.courseReservationRoutes

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = HOST, module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    // 必须安装这个插件，否则 POST 的 JSON 无法被识别 (415 根源)
    install(ContentNegotiation) {
        json(Json {
            // 核心配置1：强制序列化默认值字段（classDuration=60会被序列化）
            encodeDefaults = true
            prettyPrint = true
            isLenient = true
            // 核心配置2：字段名使用驼峰（和前端保持一致）
            ignoreUnknownKeys = true // 建议开启，防止前端多传字段导致报错
            isLenient = true          // 宽容模式，允许不规范的 JSON（如 key 没加引号）
            coerceInputValues = true // 强制转换输入值（非常有用！）
        })
    }

    //跨域相关
    install(CORS) {
        // 1. 允许的访问来源（生产环境建议指定具体域名）
        // allowHost("www.yourdomain.com")
        anyHost() // 允许任何来源访问，调试时很方便

        // 2. 允许的 HTTP 方法
        allowMethod(HttpMethod.Options) // 必须允许 Options 用于预检请求
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)

        // 3. 允许的 Header
        // 允许 json 传输必须包含 Content-Type
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization) // 如果你有登录认证，通常也需要这个

        // 4. 如果前端需要读取非标准的 Response Header，需要显式暴露
        // exposeHeader(HttpHeaders.Serialization)

        // 5. 是否允许发送凭据（如 Cookie）
        // allowCredentials = true
    }
    install(StatusPages) {
        exception<IllegalArgumentException> { call, e ->
            call.respond(ApiResponse(success = false, data = null, message = e.message ?: "参数错误"))
        }
        exception<Exception> { call, e ->
            call.respond(ApiResponse(success = false, data = null, message = "服务器内部错误：${e.message}"))
        }
    }
    DatabaseFactory.init()
    val service = CourseReservationService()
    routing {
        courseReservationRoutes(service)
    }
}