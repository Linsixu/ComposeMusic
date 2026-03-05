package org.example.project.db.table

import org.example.project.db.DatabaseFactory.dbQuery
import org.example.project.db.model.CourseTemplate
import org.example.project.db.table.CourseTemplates.templateId
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object CourseTemplates : Table("course_template") {
    val templateId = long("template_id").autoIncrement()
    val templateName = varchar("template_name", 100)
    val subject = varchar("subject", 50).nullable()
    val teacherId = long("teacher_id")
    val classDuration = integer("class_duration")
    val description = text("description").nullable()
    val createTime = datetime("create_time")
    val updateTime = datetime("update_time")

    override val primaryKey = PrimaryKey(templateId)
}

class CourseTemplateDAO {
    private fun resultRowToTemplate(row: ResultRow) = CourseTemplate(
        templateId = row[CourseTemplates.templateId],
        templateName = row[CourseTemplates.templateName],
        subject = row[CourseTemplates.subject],
        teacherId = row[CourseTemplates.teacherId],
        classDuration = row[CourseTemplates.classDuration],
        description = row[CourseTemplates.description],
        createTime = row[CourseTemplates.createTime],
        updateTime = row[CourseTemplates.updateTime]
    )

    suspend fun create(template: CourseTemplate): Long = dbQuery {
        CourseTemplates.insert {
            it[templateName] = template.templateName
            it[subject] = template.subject
            it[teacherId] = template.teacherId
            it[classDuration] = template.classDuration
            it[description] = template.description
        } get CourseTemplates.templateId
    }

    suspend fun delete(id: Long): Boolean = dbQuery {
        CourseTemplates.deleteWhere { templateId eq id } > 0
    }

    suspend fun update(id: Long, template: CourseTemplate): Boolean = dbQuery {
        CourseTemplates.update({ templateId eq id }) {
            it[templateName] = template.templateName
            it[subject] = template.subject
            it[teacherId] = template.teacherId
            it[classDuration] = template.classDuration
            it[description] = template.description
        } > 0
    }

    suspend fun findById(id: Long): CourseTemplate? = dbQuery {
        CourseTemplates.selectAll().where { templateId eq id }
            .singleOrNull()?.let { resultRowToTemplate(it) }
    }

    suspend fun findAll(): List<CourseTemplate> = dbQuery {
        CourseTemplates.selectAll().map { resultRowToTemplate(it) }
    }

    suspend fun findByTeacher(teacherId: Long): List<CourseTemplate> = dbQuery {
        CourseTemplates.selectAll().where { CourseTemplates.teacherId eq teacherId }
            .map { resultRowToTemplate(it) }
    }
}