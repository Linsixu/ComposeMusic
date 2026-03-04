plugins {
    //这个是根据Serializable动态编译生成对应的json对象，必须要
    kotlin("plugin.serialization") version "2.3.0"
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    application
}

group = "org.example.project"
version = "1.0.0"
application {
    mainClass.set("org.example.project.ApplicationKt")
    
    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    implementation(projects.shared)
    implementation(libs.logback)
    implementation(libs.ktor.serverCore)
    implementation(libs.ktor.serverNetty)
    testImplementation(libs.ktor.serverTestHost)
    testImplementation(libs.kotlin.testJunit)

    // Ktor 核心
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")

    // Exposed ORM 核心
    implementation("org.jetbrains.exposed:exposed-core:0.58.0")
    implementation("org.jetbrains.exposed:exposed-dao:0.58.0") // DAO 层（可选）
    implementation("org.jetbrains.exposed:exposed-jdbc:0.58.0")

    // MySQL 驱动
    implementation("mysql:mysql-connector-java:8.0.33")

    // 数据库连接池（HikariCP，Ktor 推荐）
    implementation("com.zaxxer:HikariCP:5.0.1")

    // Ktor 协程支持（Exposed 可结合协程）
//    implementation("org.jetbrains.exposed:exposed-coroutines:0.58.0")
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:0.58.0")
    //跨域依赖
    implementation("io.ktor:ktor-server-cors-jvm:2.3.3") // 版本与你的 Ktor 一致


    // Ktor 3.x 序列化插件
    implementation("io.ktor:ktor-server-content-negotiation-jvm:3.0.0")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:3.0.0")
}