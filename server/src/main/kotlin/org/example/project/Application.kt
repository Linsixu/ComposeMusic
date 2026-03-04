package org.example.project

import com.mysql.cj.log.Log
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.example.project.db.DatabaseFactory
import org.example.project.db.bean.User
import org.example.project.db.bean.UserDao
import org.example.project.db.request.UserRequest

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = HOST, module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    // 必须安装这个插件，否则 POST 的 JSON 无法被识别 (415 根源)
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
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

    DatabaseFactory.init()
    val userDao = UserDao()
    routing {
        // 新增用户接口（POST /user）
        post("/user") {
            val user = call.receive<UserRequest>() // 接收前端传的用户数据
            println("Ktor 收到原始字符串: $user")
            val saveUer = User(null, user.name, user.parentPhone, user.sex)
            val userId = userDao.createUser(saveUer) // 存储到数据库
            call.respondText("用户创建成功，ID：$userId")
        }
        // 查询用户接口（GET /user/{id}）
        get("/user/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("ID不能为空")
            val user = userDao.getUserById(id)
            if (user != null) {
                call.respond(user) // 返回用户数据
            } else {
                call.respondText("用户不存在", status = HttpStatusCode(404, "参数错误"))
            }
        }

//        get("/") {
//            call.respondText("Ktor: ${Greeting().greet()}")
//        }
    }
}