package org.example.project.db

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table

/**
 * @author: linsixu@ruqimobility.com
 * @date:2026/2/26
 * 用途：
 */
//object Users: Table("user_message") {
//    val id = integer("id").autoIncrement()
//    //val name = text("name").nullable() 定义可控
//    val name = text("name")
//    val parentPhone = text("parent_phone")
//    val sex = integer("sex")
//
//    override val primaryKey = PrimaryKey(id, name = "PK_user_message")
//}

object Users : IntIdTable("user_message", "id") { // 第二个参数是列名，默认就是 "id"
    val name = text("name")
    val parentPhone = text("parent_phone")
    val sex = integer("sex")
}