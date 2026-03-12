package org.example.project.db.table

/**
 * @author: linsixu@ruqimobility.com
 * @date:2026/3/4
 * 用途：预约记录 DAO
 */
import org.example.project.db.DatabaseFactory.dbQuery
import org.example.project.db.model.StudentReservation
import org.example.project.db.table.StudentReservations.reservationId
import org.example.project.db.table.StudentReservations.teacherId
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

object StudentReservations : Table("reservations") {
    val reservationId = long("id").autoIncrement()
    val classId = long("session_id")
    val studentId = long("student_id")
    val institutionId = long("institution_id")
    val status = integer("status")
    val bookedAtms = long("booked_at_ms")
    val teacherId = long("teacher_id")

    override val primaryKey = PrimaryKey(reservationId)
}

class StudentReservationDAO {
    private fun resultRowToReservation(row: ResultRow) = StudentReservation(
        reservationId = row[StudentReservations.reservationId],
        studentId = row[StudentReservations.studentId],
        classId = row[StudentReservations.classId],
        institutionId = row[StudentReservations.institutionId],
        status = row[StudentReservations.status],
        bookedAtms = row[StudentReservations.bookedAtms],
        teacherId = row[StudentReservations.teacherId]
    )

    suspend fun create(reservation: StudentReservation): Long = dbQuery {
        StudentReservations.insert {
            it[classId] = reservation.classId
            it[studentId] = reservation.studentId
            it[institutionId] = reservation.institutionId
            it[status] = reservation.status
            it[bookedAtms] = reservation.bookedAtms
            it[teacherId] = reservation.teacherId
        } get StudentReservations.reservationId
    }

    suspend fun delete(id: Long): Boolean = dbQuery {
        StudentReservations.deleteWhere { reservationId eq id } > 0
    }

    suspend fun deleteBySessionId(sessionId: Long): Boolean = dbQuery {
        StudentReservations.deleteWhere { classId eq sessionId } > 0
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

    suspend fun findByTeacherId(outTeacherId: Long): List<StudentReservation> = dbQuery {
        StudentReservations.selectAll().where { teacherId eq outTeacherId }
            .map { resultRowToReservation(it) }
    }

    suspend fun findAll(): List<StudentReservation> = dbQuery {
        StudentReservations.selectAll().map { resultRowToReservation(it) }
    }

    suspend fun findByStudent(studentId: Long): List<StudentReservation> = dbQuery {
        StudentReservations.selectAll().where { StudentReservations.studentId eq studentId }
            .map { resultRowToReservation(it) }
    }
}