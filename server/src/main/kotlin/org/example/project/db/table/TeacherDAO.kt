package org.example.project.db.table


import org.example.project.db.DatabaseFactory.dbQuery
import org.example.project.db.model.Teacher
import org.example.project.db.table.Teachers.teacherId
import org.example.project.db.table.Teachers.teacherName
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object Teachers : Table("teachers") {
    val teacherId = long("id").autoIncrement()
    val institutionId = long("institution_id")
    val teacherName = varchar("name", 50)
    val teacherPhone = varchar("phone", 20).nullable()
    val subject = varchar("subject", 50).nullable()

    override val primaryKey = PrimaryKey(teacherId)
}

class TeacherDAO {
    private fun resultRowToTeacher(row: ResultRow) = Teacher(
        teacherId = row[Teachers.teacherId],
        teacherName = row[Teachers.teacherName],
        teacherPhone = row[Teachers.teacherPhone],
        subject = row[Teachers.subject],
        institutionId = row[Teachers.institutionId]
    )

    suspend fun create(teacher: Teacher): Long = dbQuery {
        Teachers.insert {
            it[teacherName] = teacher.teacherName
            it[teacherPhone] = teacher.teacherPhone
            it[subject] = teacher.subject
            it[institutionId] = teacher.institutionId
        } get Teachers.teacherId
    }

    suspend fun delete(id: Long): Boolean = dbQuery {
        Teachers.deleteWhere { teacherId eq id } > 0
    }

    suspend fun update(id: Long, teacher: Teacher): Boolean = dbQuery {
        Teachers.update({ teacherId eq id }) {
            it[teacherName] = teacher.teacherName
            it[teacherPhone] = teacher.teacherPhone
            it[subject] = teacher.subject
            it[institutionId] = teacher.institutionId
        } > 0
    }

    suspend fun findById(id: Long): Teacher? = dbQuery {
        Teachers.selectAll().where { teacherId eq id }
            .singleOrNull()?.let { resultRowToTeacher(it) }
    }

    suspend fun findByName(teacherNameParam: String): Teacher? = dbQuery {
        Teachers.selectAll().where { teacherName eq teacherNameParam }
            .singleOrNull()?.let { resultRowToTeacher(it) }
    }

    suspend fun findAll(): List<Teacher> = dbQuery {
        Teachers.selectAll().map { resultRowToTeacher(it) }
    }

    suspend fun findByInstitution(institutionId: Long): List<Teacher> = dbQuery {
        Teachers.selectAll().where { Teachers.institutionId eq institutionId }
            .map { resultRowToTeacher(it) }
    }
}