package org.example.project.db.table


import org.example.project.db.DatabaseFactory.dbQuery
import org.example.project.db.model.Student
import org.example.project.db.table.Students.institutionId
import org.example.project.db.table.Students.phone
import org.example.project.db.table.Students.studentId
import org.example.project.db.table.Students.studentName
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object Students : Table("students") {
    val studentId = long("id").autoIncrement()
    val institutionId = long("institution_id")
    val studentName = varchar("name", 50)
    val phone = varchar("phone", 20)

    override val primaryKey = PrimaryKey(studentId)
}

class StudentDAO {
    private fun resultRowToStudent(row: ResultRow) = Student(
        studentId = row[Students.studentId],
        studentName = row[Students.studentName],
        phone = row[Students.phone],
        institutionId = row[Students.institutionId]
    )

    suspend fun create(student: Student): Long = dbQuery {
        Students.insert {
            it[studentName] = student.studentName
            it[phone] = student.phone
            it[institutionId] = student.institutionId!!
        } get Students.studentId
    }

    suspend fun delete(id: Long): Boolean = dbQuery {
        Students.deleteWhere { studentId eq id } > 0
    }

    suspend fun update(id: Long, student: Student): Boolean = dbQuery {
        Students.update({ studentId eq id }) {
            it[studentName] = student.studentName
            it[phone] = student.phone
        } > 0
    }

    suspend fun findById(id: Long): Student? = dbQuery {
        Students.selectAll().where { studentId eq id }
            .singleOrNull()?.let { resultRowToStudent(it) }
    }

    suspend fun findByUserInfo(name: String, parentPhone: String, institution: Long): Student? = dbQuery {
        Students.selectAll().where {
            ((institutionId eq institution) and (studentName eq name) and (phone eq parentPhone))
        }.singleOrNull()?.let { resultRowToStudent(it) }
    }

    suspend fun findByUserInfoV2(name: String, parentPhone: String): Student? = dbQuery {
        Students.selectAll().where {
            (studentName eq name) and (phone eq parentPhone)
        }.singleOrNull()?.let { resultRowToStudent(it) }
    }

    suspend fun findAll(): List<Student> = dbQuery {
        Students.selectAll().map { resultRowToStudent(it) }
    }
}