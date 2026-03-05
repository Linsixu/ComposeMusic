package org.example.project.db.table

/**
 * @author: linsixu@ruqimobility.com
 * @date:2026/3/4
 * 用途：预约记录 DAO
 */
import org.example.project.db.DatabaseFactory.dbQuery
import org.example.project.db.model.StudentReservation
import org.example.project.db.table.StudentReservations.reservationId
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object StudentReservations : Table("student_reservation") {
    val reservationId = long("reservation_id").autoIncrement()
    val studentId = long("student_id")
    val classId = long("class_id")
    val reservationTime = datetime("reservation_time")
    val status = integer("status")
    val createTime = datetime("create_time")
    val updateTime = datetime("update_time")

    override val primaryKey = PrimaryKey(reservationId)
}

class StudentReservationDAO {
    private fun resultRowToReservation(row: ResultRow) = StudentReservation(
        reservationId = row[StudentReservations.reservationId],
        studentId = row[StudentReservations.studentId],
        classId = row[StudentReservations.classId],
        reservationTime = row[StudentReservations.reservationTime],
        status = row[StudentReservations.status],
        createTime = row[StudentReservations.createTime],
        updateTime = row[StudentReservations.updateTime]
    )

    suspend fun create(reservation: StudentReservation): Long = dbQuery {
        StudentReservations.insert {
            it[studentId] = reservation.studentId
            it[classId] = reservation.classId
            it[status] = reservation.status
        } get StudentReservations.reservationId
    }

    suspend fun delete(id: Long): Boolean = dbQuery {
        StudentReservations.deleteWhere { reservationId eq id } > 0
    }

    suspend fun updateStatus(id: Long, status: Int): Boolean = dbQuery {
        StudentReservations.update({ reservationId eq id }) {
            it[this.status] = status
        } > 0
    }

    suspend fun findById(id: Long): StudentReservation? = dbQuery {
        StudentReservations.selectAll().where { reservationId eq id }
            .singleOrNull()?.let { resultRowToReservation(it) }
    }

    suspend fun findAll(): List<StudentReservation> = dbQuery {
        StudentReservations.selectAll().map { resultRowToReservation(it) }
    }

    suspend fun findByStudent(studentId: Long): List<StudentReservation> = dbQuery {
        StudentReservations.selectAll().where { StudentReservations.studentId eq studentId }
            .map { resultRowToReservation(it) }
    }
}