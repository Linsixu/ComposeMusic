package org.example.project.db

/**
 * @author: linsixu@ruqimobility.com
 * @date:2026/2/26
 * 用途：
 */
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    // 初始化数据库连接（调用一次即可，如在 Ktor 启动时）
    fun init() {
        val config = HikariConfig().apply {
            jdbcUrl = "jdbc:mysql://localhost:3306/class_room?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true"
//            driverClassName = "com.mysql.cj.jdbc.Driver"
            username = "root" // 你的 MySQL 用户名
            password = "12345678" // 你的 MySQL 密码
            maximumPoolSize = 10 // 连接池最大连接数
        }
        val dataSource = HikariDataSource(config)
        // 绑定 Exposed 到数据库连接
        Database.connect(dataSource)
    }

    // 封装协程事务（适配 Ktor 协程，推荐）
    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction { block() }

    // 封装普通事务（非协程场景）
    fun <T> dbSyncQuery(block: () -> T): T =
        transaction { block() }
}