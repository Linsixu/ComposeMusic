package org.example.project.db.bean

import org.example.project.db.DatabaseFactory
import org.example.project.db.DatabaseFactory.dbQuery
import org.example.project.db.Users
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import kotlinx.serialization.Serializable

/**
 * @author: linsixu@ruqimobility.com
 * @date:2026/2/26
 * 用途：
 */
@Serializable
data class User(
    val id: Int? = null,
    val name: String,
    val parentPhone: String,
    val sex: Int
)

class UserDao {
    // 1. 新增数据（存储用户到数据库）
    suspend fun createUser(user: User): Int = DatabaseFactory.dbQuery {
        // 前置校验
        require(user.name.isNotBlank()) { "name 不能为空" }
        require(user.parentPhone.matches(Regex("^1[3-9]\\d{9}$"))) { "手机号格式错误" }

        // 使用 insertAndGetId 直接获取生成的 EntityID
        val generatedId = Users.insertAndGetId { row ->
            row[name] = user.name
            row[parentPhone] = user.parentPhone
            row[sex] = user.sex
        }

        generatedId.value // 返回 Int 值
    }

    // 2. 查询数据
    suspend fun getUserById(id: Int): User? = DatabaseFactory.dbQuery {
        Users.selectAll().where { Users.id eq id }
            .singleOrNull()
            ?.mapToUser()
    }

    // 3. 更新数据
    suspend fun updateUser(id: Int, user: User): Boolean = DatabaseFactory.dbQuery {
        val rowsUpdated = Users.update({ Users.id eq id }) {
            it[name] = user.name
            it[parentPhone] = user.parentPhone
            it[sex] = user.sex
        }
        rowsUpdated > 0
    }

    // 4. 删除数据
    suspend fun deleteUser(id: Int): Boolean = DatabaseFactory.dbQuery {
        // 注意：新版 Exposed 的 deleteWhere 需要显式引用表达式
        val rowsDeleted = Users.deleteWhere { Users.id eq id }
        rowsDeleted > 0
    }

    // 抽取一个扩展函数，方便重复使用
    private fun ResultRow.mapToUser() = User(
        id = this[Users.id].value, // 注意这里要用 .value
        name = this[Users.name],
        parentPhone = this[Users.parentPhone],
        sex = this[Users.sex]
    )
}