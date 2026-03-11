package org.example.project.db.table

import org.example.project.db.DatabaseFactory.dbQuery
import org.example.project.db.model.CourseTemplate
import org.example.project.db.table.CourseTemplates.templateId
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object CourseTemplates : Table("course_templates") {
    val templateId = long("id").autoIncrement()
    val templateName = varchar("title", 100)
    val teacherId = long("teacher_id")
    val institutionId = long("institution_id")
    val classDuration = integer("duration_minutes")
    val description = text("description").nullable()
    val subject = varchar("subject", 50)
    val createTime = datetime("created_at")

    override val primaryKey = PrimaryKey(templateId)
}

class CourseTemplateDAO {
    private fun resultRowToTemplate(row: ResultRow) = CourseTemplate(
        templateId = row[CourseTemplates.templateId],
        teacherId = row[CourseTemplates.teacherId],
        institutionId = row[CourseTemplates.institutionId],
        classDuration = row[CourseTemplates.classDuration],
        description = row[CourseTemplates.description],
        createTime = row[CourseTemplates.createTime],
        subject = row[CourseTemplates.subject],
        templateName = row[CourseTemplates.templateName]
    )

    suspend fun create(template: CourseTemplate): Long = dbQuery {
        CourseTemplates.insert {
            it[institutionId] = template.institutionId
            it[teacherId] = template.teacherId
            it[classDuration] = template.classDuration
            it[description] = template.description
            it[subject] = template.subject
            it[templateName] = template.templateName
        } get CourseTemplates.templateId
    }

    suspend fun delete(id: Long): Boolean = dbQuery {
        CourseTemplates.deleteWhere { templateId eq id } > 0
    }

    suspend fun update(id: Long, template: CourseTemplate): Boolean = dbQuery {
        CourseTemplates.update({ templateId eq id }) {
            it[institutionId] = template.institutionId
            it[teacherId] = template.teacherId
            it[classDuration] = template.classDuration
            it[description] = template.description
            it[subject] = template.subject
            it[templateName] = template.templateName
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