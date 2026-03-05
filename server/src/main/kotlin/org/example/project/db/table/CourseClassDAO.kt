package org.example.project.db.table

/**
 * @author: linsixu@ruqimobility.com
 * @date:2026/3/4
 * 用途：可预约课时
 */
import org.example.project.db.DatabaseFactory.dbQuery
import org.example.project.db.model.CourseClass
import org.example.project.db.table.CourseClasses.classId
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

object CourseClasses : Table("course_class") {
    val classId = long("class_id").autoIncrement()
    val templateId = long("template_id")
    val classTimestampMs = long("class_timestamp_ms")
    val status = integer("status")
    val createTime = datetime("create_time")
    val updateTime = datetime("update_time")

    override val primaryKey = PrimaryKey(classId)
}

class CourseClassDAO {
    private fun resultRowToCourseClass(row: ResultRow) = CourseClass(
        classId = row[CourseClasses.classId],
        templateId = row[CourseClasses.templateId],
        classTimestampMs = row[CourseClasses.classTimestampMs],
        status = row[CourseClasses.status],
        createTime = row[CourseClasses.createTime],
        updateTime = row[CourseClasses.updateTime]
    )

    suspend fun create(courseClass: CourseClass): Long = dbQuery {
        CourseClasses.insert {
            it[templateId] = courseClass.templateId
            it[classTimestampMs] = courseClass.classTimestampMs
            it[status] = courseClass.status
        } get CourseClasses.classId
    }

    suspend fun delete(id: Long): Boolean = dbQuery {
        CourseClasses.deleteWhere { classId eq id } > 0
    }

    suspend fun updateStatus(id: Long, status: Int): Boolean = dbQuery {
        CourseClasses.update({ classId eq id }) {
            it[this.status] = status
        } > 0
    }

    suspend fun findById(id: Long): CourseClass? = dbQuery {
        CourseClasses.selectAll().where { classId eq id }
            .singleOrNull()?.let { resultRowToCourseClass(it) }
    }

    suspend fun findAll(): List<CourseClass> = dbQuery {
        CourseClasses.selectAll().map { resultRowToCourseClass(it) }
    }

    suspend fun findAvailableByTemplate(templateId: Long): List<CourseClass> = dbQuery {
        CourseClasses.selectAll()
            .where { (CourseClasses.templateId eq templateId) and (CourseClasses.status eq 1) }
            .map { resultRowToCourseClass(it) }
    }
}