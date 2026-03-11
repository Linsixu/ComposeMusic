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

object CourseClasses : Table("class_sessions") {
    val classId = long("id").autoIncrement()
    val templateId = long("template_id")
    val teacherId = long("teacher_id")
    val institutionId = long("institution_id")
    val startMillisecondTime = long("start_time_ms")
    val duration = integer("duration")
    val status = integer("status")
    val createTime = datetime("created_at")

    override val primaryKey = PrimaryKey(classId)
}

class CourseClassDAO {
    private fun resultRowToCourseClass(row: ResultRow) = CourseClass(
        classId = row[CourseClasses.classId],
        templateId = row[CourseClasses.templateId],
        teacherId = row[CourseClasses.teacherId],
        institutionId = row[CourseClasses.institutionId],
        startMillisecondTime = row[CourseClasses.startMillisecondTime],
        duration = row[CourseClasses.duration],
        status = row[CourseClasses.status],
        createTime = row[CourseClasses.createTime]
    )

    suspend fun create(courseClass: CourseClass): Long = dbQuery {
        CourseClasses.insert {
            it[templateId] = courseClass.templateId
            it[teacherId] = courseClass.teacherId
            it[institutionId] = courseClass.institutionId
            it[startMillisecondTime] = courseClass.startMillisecondTime
            it[duration] = courseClass.duration
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

    suspend fun findAllByTeacherId(teacherId: Long): List<CourseClass>? = dbQuery {
        CourseClasses.selectAll().where {
            CourseClasses.teacherId eq teacherId
        }.map { resultRowToCourseClass(it) }
    }

    suspend fun findAvailableAllByTeacherId(teacherId: Long): List<CourseClass>? = dbQuery {
        CourseClasses.selectAll().where {
            (CourseClasses.teacherId eq teacherId and (CourseClasses.status eq 1))
        }.map { resultRowToCourseClass(it) }
    }

    suspend fun findAvailableByTemplate(templateId: Long): List<CourseClass> = dbQuery {
        CourseClasses.selectAll()
            .where { (CourseClasses.templateId eq templateId) and (CourseClasses.status eq 1) }
            .map { resultRowToCourseClass(it) }
    }
}
