package org.example.project.db.table

import org.example.project.db.DatabaseFactory.dbQuery
import org.example.project.db.model.EduInstitution
import org.example.project.db.table.EduInstitutions.institutionId

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import java.sql.Timestamp

object EduInstitutions : Table("institutions") {
    val institutionId = long("id").autoIncrement()
    val institutionName = varchar("name", 100)
    val institutionAddress = varchar("address", 255).nullable()
    val contactPhone = varchar("contact_phone", 20).nullable()
    val createTime = datetime("create_time")
    override val primaryKey = PrimaryKey(institutionId)
}

class EduInstitutionDAO {
    // 转换为模型
    private fun resultRowToInstitution(row: ResultRow) = EduInstitution(
        institutionId = row[EduInstitutions.institutionId],
        institutionName = row[EduInstitutions.institutionName],
        institutionAddress = row[EduInstitutions.institutionAddress],
        contactPhone = row[EduInstitutions.contactPhone],
        createTime = row[EduInstitutions.createTime],
    )

    // 新增机构
    suspend fun create(institution: EduInstitution): Long = dbQuery {
        EduInstitutions.insert {
            it[institutionName] = institution.institutionName
            it[institutionAddress] = institution.institutionAddress
            it[contactPhone] = institution.contactPhone
        } get EduInstitutions.institutionId
    }

    // 删除机构
    suspend fun delete(id: Long): Boolean = dbQuery {
        EduInstitutions.deleteWhere { institutionId eq id } > 0
    }

    // 更新机构
    suspend fun update(id: Long, institution: EduInstitution): Boolean = dbQuery {
        EduInstitutions.update({ institutionId eq id }) {
            it[institutionName] = institution.institutionName
            it[institutionAddress] = institution.institutionAddress
            it[contactPhone] = institution.contactPhone
        } > 0
    }

    // 根据ID查询
    suspend fun findById(id: Long): EduInstitution? = dbQuery {
        EduInstitutions.selectAll().where { institutionId eq id }
            .singleOrNull()?.let { resultRowToInstitution(it) }
    }

    // 查询所有
    suspend fun findAll(): List<EduInstitution> = dbQuery {
        EduInstitutions.selectAll().map { resultRowToInstitution(it) }
    }
}