package org.example.project.db.manager

/**
 * @author: linsixu@ruqimobility.com
 * @date:2026/3/4
 * 用途：核心服务
 */

import org.example.project.SqlCode.course_has_exit_at_same_time
import org.example.project.SqlCode.course_id_has_not_exit
import org.example.project.SqlCode.course_status_has_change
import org.example.project.SqlCode.error_other
import org.example.project.SqlCode.institution_has_exit
import org.example.project.SqlCode.institution_not_exit
import org.example.project.SqlCode.student_has_exit
import org.example.project.SqlCode.student_has_not_exit
import org.example.project.SqlCode.teacher_has_not_exit
import org.example.project.SqlCode.template_course_has_exit
import org.example.project.db.DatabaseFactory.dbQuery
import org.example.project.db.model.ApiResponse
import org.example.project.db.model.CourseClass
import org.example.project.db.model.CourseTemplate
import org.example.project.db.model.EduInstitution
import org.example.project.db.model.Student
import org.example.project.db.model.StudentReservation
import org.example.project.db.model.Teacher
import org.example.project.db.table.CourseClassDAO
import org.example.project.db.table.CourseTemplateDAO
import org.example.project.db.table.EduInstitutionDAO
import org.example.project.db.table.StudentDAO
import org.example.project.db.table.StudentReservationDAO
import org.example.project.db.table.TeacherDAO
import org.example.project.request.CreateCourseTemplateReq
import org.example.project.request.CreateStudentReq
import org.example.project.request.TeacherRequest
import org.example.project.request.course.CreateCourseReq
import org.example.project.request.course.QueryCourseResponse
import org.example.project.request.course.QueryCourseTemplateResponse
import org.example.project.request.reservation.CreateReservationReq
import org.jetbrains.exposed.exceptions.ExposedSQLException
import java.sql.SQLIntegrityConstraintViolationException

class CourseReservationService(
    private val institutionDAO: EduInstitutionDAO = EduInstitutionDAO(),
    private val teacherDAO: TeacherDAO = TeacherDAO(),
    private val studentDAO: StudentDAO = StudentDAO(),
    private val templateDAO: CourseTemplateDAO = CourseTemplateDAO(),
    private val courseClassDAO: CourseClassDAO = CourseClassDAO(),
    private val reservationDAO: StudentReservationDAO = StudentReservationDAO()
) {
    // ========== 机构服务 ==========
    suspend fun createInstitution(institution: EduInstitution): ApiResponse<Long> {
        return try {
            val id = institutionDAO.create(institution)
            ApiResponse(success = true, data = id, message = "机构创建成功")
        } catch (e: ExposedSQLException) {
            ApiResponse(success = false, code = institution_has_exit, message = "机构名称已存在：${e.message}")
        } catch (e: Exception) {
            ApiResponse(success = false, code = error_other, message = "创建失败：${e.message}")
        }
    }

    suspend fun deleteInstitution(id: Long): ApiResponse<Boolean> {
        return try {
            val success = institutionDAO.delete(id)
            ApiResponse(success = success, message = if (success) "删除成功" else "机构不存在")
        } catch (e: Exception) {
            ApiResponse(success = false, message = "删除失败：${e.message}")
        }
    }

    suspend fun updateInstitution(id: Long, institution: EduInstitution): ApiResponse<Boolean> {
        return try {
            val success = institutionDAO.update(id, institution)
            ApiResponse(success = success, message = if (success) "更新成功" else "机构不存在")
        } catch (e: ExposedSQLException) {
            ApiResponse(success = false, message = "机构名称已存在：${e.message}")
        } catch (e: Exception) {
            ApiResponse(success = false, message = "更新失败：${e.message}")
        }
    }

    suspend fun getInstitutionById(id: Long): ApiResponse<EduInstitution?> {
        return try {
            val institution = institutionDAO.findById(id)
            ApiResponse(success = true, data = institution, message = if (institution == null) "机构不存在" else "查询成功")
        } catch (e: Exception) {
            ApiResponse(success = false, message = "查询失败：${e.message}")
        }
    }

    suspend fun getAllInstitutions(): ApiResponse<List<EduInstitution>> {
        return try {
            val list = institutionDAO.findAll()
            ApiResponse(success = true, data = list)
        } catch (e: Exception) {
            ApiResponse(success = false, message = "查询失败：${e.message}")
        }
    }

    // ========== 老师服务 ==========
    suspend fun createTeacher(teacherReq: TeacherRequest): ApiResponse<Long> {
        return try {
            dbQuery {
                val institution = institutionDAO.findByName(teacherReq.institutionName)
                    ?: throw IllegalArgumentException("当前机构不存在")
                println("magic create teacher id=$institution")
                val teacher = Teacher(teacherName = teacherReq.teacherName, teacherPhone = teacherReq.teacherPhone,
                    subject = teacherReq.subject, institutionId = institution.institutionId!!)
                val id = teacherDAO.create(teacher)
                println("magic create teacher id=$id")
                ApiResponse(success = true, data = id, message = "老师创建成功")
            }
        } catch (e: IllegalArgumentException) {
            ApiResponse(success = false, code = institution_not_exit, message = "${e.message}")
        }
    }

    suspend fun deleteTeacher(id: Long): ApiResponse<Boolean> {
        return try {
            val success = teacherDAO.delete(id)
            ApiResponse(success = success, message = if (success) "删除成功" else "老师不存在")
        } catch (e: Exception) {
            ApiResponse(success = false, message = "删除失败：${e.message}")
        }
    }

    suspend fun updateTeacher(id: Long, teacher: Teacher): ApiResponse<Boolean> {
        return try {
            val success = teacherDAO.update(id, teacher)
            ApiResponse(success = success, message = if (success) "更新成功" else "老师不存在")
        } catch (e: ExposedSQLException) {
            ApiResponse(success = false, message = "手机号已存在：${e.message}")
        } catch (e: Exception) {
            ApiResponse(success = false, message = "更新失败：${e.message}")
        }
    }

    suspend fun getTeacherById(id: Long): ApiResponse<Teacher?> {
        return try {
            val teacher = teacherDAO.findById(id)
            ApiResponse(success = true, data = teacher, message = if (teacher == null) "老师不存在" else "查询成功")
        } catch (e: Exception) {
            ApiResponse(success = false, message = "查询失败：${e.message}")
        }
    }

    suspend fun getAllTeachers(): ApiResponse<List<Teacher>> {
        return try {
            val list = teacherDAO.findAll()
            ApiResponse(success = true, data = list)
        } catch (e: Exception) {
            ApiResponse(success = false, message = "查询失败：${e.message}")
        }
    }

    suspend fun getTeachersByInstitution(institutionId: Long): ApiResponse<List<Teacher>> {
        return try {
            val list = teacherDAO.findByInstitution(institutionId)
            ApiResponse(success = true, data = list)
        } catch (e: Exception) {
            ApiResponse(success = false, message = "查询失败：${e.message}")
        }
    }

    // ========== 学生服务 ==========
    suspend fun createStudent(student: CreateStudentReq): ApiResponse<Long> {
        return try {
            dbQuery {
                val institution = institutionDAO.findByName(student.institutionName)
                    ?: throw IllegalArgumentException("当前机构不存在")
                println("magic create student id=$institution")
                val student = Student(studentName = student.studentName, phone = student.phone, institutionId = institution.institutionId!!)
                val id = studentDAO.create(student)
                println("magic create student id=$id")
                ApiResponse(success = true, data = id, message = "学生创建成功")
            }
        } catch (e: IllegalArgumentException) {
            ApiResponse(success = false, code= institution_not_exit, message = "${e.message}")
        } catch (e: SQLIntegrityConstraintViolationException){
            ApiResponse(success = false, code= student_has_exit, message = "该学生已在对应机构添加，请勿重新添加")
        } catch (e: ExposedSQLException) {
            // 数据库异常（如唯一键冲突）
            ApiResponse(success = false, code = student_has_exit, message = "数据库异常：${e.message}")
        } catch (e: Exception) {
            // 其他异常
            ApiResponse(success = false, code = error_other, message = "抢占失败：${e.message}")
        }
    }

    suspend fun deleteStudent(id: Long): ApiResponse<Boolean> {
        return try {
            val success = studentDAO.delete(id)
            ApiResponse(success = success, message = if (success) "删除成功" else "学生不存在")
        } catch (e: Exception) {
            ApiResponse(success = false, message = "删除失败：${e.message}")
        }
    }

    suspend fun updateStudent(id: Long, student: Student): ApiResponse<Boolean> {
        return try {
            val success = studentDAO.update(id, student)
            ApiResponse(success = success, message = if (success) "更新成功" else "学生不存在")
        } catch (e: ExposedSQLException) {
            ApiResponse(success = false, message = "手机号已存在：${e.message}")
        } catch (e: Exception) {
            ApiResponse(success = false, message = "更新失败：${e.message}")
        }
    }

    suspend fun getStudentById(id: Long): ApiResponse<Student?> {
        return try {
            val student = studentDAO.findById(id)
            ApiResponse(success = true, data = student, message = if (student == null) "学生不存在" else "查询成功")
        } catch (e: Exception) {
            ApiResponse(success = false, message = "查询失败：${e.message}")
        }
    }

    suspend fun getAllStudents(): ApiResponse<List<Student>> {
        return try {
            val list = studentDAO.findAll()
            ApiResponse(success = true, data = list)
        } catch (e: Exception) {
            ApiResponse(success = false, message = "查询失败：${e.message}")
        }
    }

    // ========== 课程模板服务 ==========
    suspend fun createCourseTemplate(templateReq: CreateCourseTemplateReq): ApiResponse<Long> {
        return try {
            dbQuery {
                val teacher = teacherDAO.findByName(templateReq.teacherName)
                    ?: throw IllegalArgumentException("当前老师不存在系统中")
                println("magic createCourseTemplate id=$teacher")
                val courseTemplate = CourseTemplate(
                    templateName = templateReq.templateName,
                    institutionId = teacher.institutionId,
                    teacherId = teacher.teacherId!!,
                    subject = templateReq.subject,
                    classDuration = templateReq.classDuration,
                    description = templateReq.description
                    )
                val id = templateDAO.create(courseTemplate)
                println("magic createCourseTemplate id=$id")
                ApiResponse(success = true, data = id, message = "创建课程模版成功")
            }
        } catch (e: IllegalArgumentException) {
            ApiResponse(success = false, code= teacher_has_not_exit, message = "${e.message}")
        } catch (e: SQLIntegrityConstraintViolationException){
            ApiResponse(success = false, code= template_course_has_exit, message = "课程模版已存在，请勿重新添加")
        } catch (e: ExposedSQLException) {
            // 数据库异常（如唯一键冲突）
            ApiResponse(success = false, code = student_has_exit, message = "数据库异常：${e.message}")
        } catch (e: Exception) {
            // 其他异常
            ApiResponse(success = false, code = error_other, message = "创建模版失败：${e.message}")
        }
    }

    suspend fun deleteCourseTemplate(id: Long): ApiResponse<Boolean> {
        return try {
            val success = templateDAO.delete(id)
            ApiResponse(success = success, message = if (success) "删除成功" else "模板不存在")
        } catch (e: Exception) {
            ApiResponse(success = false, message = "删除失败：${e.message}")
        }
    }

    suspend fun updateCourseTemplate(id: Long, template: CourseTemplate): ApiResponse<Boolean> {
        return try {
            val success = templateDAO.update(id, template)
            ApiResponse(success = success, message = if (success) "更新成功" else "模板不存在")
        } catch (e: Exception) {
            ApiResponse(success = false, message = "更新失败：${e.message}")
        }
    }

    suspend fun getCourseTemplateById(id: Long): ApiResponse<CourseTemplate?> {
        return try {
            val template = templateDAO.findById(id)
            ApiResponse(success = true, data = template, message = if (template == null) "模板不存在" else "查询成功")
        } catch (e: Exception) {
            ApiResponse(success = false, message = "查询失败：${e.message}")
        }
    }

    suspend fun getAllCourseTemplates(): ApiResponse<List<QueryCourseTemplateResponse>> {
        return try {
            dbQuery {
                val listResponse = ArrayList<QueryCourseTemplateResponse>()
                val list = templateDAO.findAll()
                list.forEach {
                    val teacher = teacherDAO.findById(it.teacherId) ?: throw IllegalArgumentException("当前老师不存在系统中")
                    listResponse.add(QueryCourseTemplateResponse(
                        templateName = it.templateName,
                        templateId = it.templateId,
                        institutionId = it.institutionId,
                        teacherName = teacher.teacherName,
                        subject = it.subject,
                        classDuration = it.classDuration,
                        description = it.description
                    ))
                }
                ApiResponse(success = true, data = listResponse)
            }
        } catch (e: IllegalArgumentException) {
            ApiResponse(success = false, code= teacher_has_not_exit, message = "${e.message}")
        } catch (e: Exception) {
            ApiResponse(success = false, message = "查询失败：${e.message}")
        }
    }

    suspend fun getTemplatesByTeacher(teacherName: String): ApiResponse<List<QueryCourseTemplateResponse>> {
        return try {
            dbQuery {
                val listResponse = ArrayList<QueryCourseTemplateResponse>()
                val list = templateDAO.findAll()
                list.forEach {
                    val teacher = teacherDAO.findByName(teacherName) ?: throw IllegalArgumentException("当前老师不存在系统中")
                    if (teacher.teacherId == it.teacherId) {
                        listResponse.add(QueryCourseTemplateResponse(
                            templateName = it.templateName,
                            templateId = it.templateId,
                            institutionId = it.institutionId,
                            teacherName = teacher.teacherName,
                            subject = it.subject,
                            classDuration = it.classDuration,
                            description = it.description
                        ))
                    }
                }
                ApiResponse(success = true, data = listResponse)
            }
        } catch (e: IllegalArgumentException) {
            ApiResponse(success = false, code= teacher_has_not_exit, message = "${e.message}")
        } catch (e: Exception) {
            ApiResponse(success = false, message = "查询失败：${e.message}")
        }
    }

    // ========== 课时服务 ==========
    suspend fun createCourseClass(courseClassReq: CreateCourseReq): ApiResponse<Long> {
        return try {
            dbQuery {
                val teacher = teacherDAO.findByName(courseClassReq.teacherName) ?: throw IllegalArgumentException("当前老师不存在系统中")
                val courseClass = CourseClass(
                    templateId = courseClassReq.templateId,
                    teacherId = teacher.teacherId!!,
                    institutionId = teacher.institutionId,
                    startMillisecondTime = courseClassReq.startMillisecondTime,
                    duration = courseClassReq.duration,
                    status = courseClassReq.status
                )
                val id = courseClassDAO.create(courseClass)
                ApiResponse(success = true, data = id, message = "课时创建成功")
            }
        } catch (e: IllegalArgumentException) {
            ApiResponse(success = false, code= teacher_has_not_exit, message = "${e.message}")
        } catch (e: SQLIntegrityConstraintViolationException) {
          //机构id+老师id+开始时间去重
            ApiResponse(success = false, code= course_has_exit_at_same_time, message = "当前时间段你已经有发布课程")
        } catch (e: ExposedSQLException) {
            ApiResponse(success = false, code = course_has_exit_at_same_time, message = "当前时间段你已经有发布课程")
        } catch (e: Exception) {
            ApiResponse(success = false, code = error_other, message = "创建失败：${e.message}")
        }
    }

    suspend fun deleteCourseClass(id: Long): ApiResponse<Boolean> {
        return try {
            dbQuery {
                val currentCourse = courseClassDAO.findById(id) ?: throw NullPointerException("当前课时不存在，请检查课时id")
                val success = if (currentCourse.status == 0) {
                    //已经预约了，从预约表中删除才行
                    reservationDAO.deleteBySessionId(id)
                    courseClassDAO.delete(id)
                } else {
                    //可预约直接删除
                    courseClassDAO.delete(id)
                }
                ApiResponse(success = success, message = if (success) "删除成功" else "课时不存在")
            }
        } catch (e: NullPointerException) {
            ApiResponse(success = false,code = course_id_has_not_exit,message = "${e.message}")
        } catch (e: Exception) {
            ApiResponse(success = false, message = "删除失败：${e.message}")
        }
    }

    suspend fun updateClassStatus(id: Long, status: Int): ApiResponse<Boolean> {
        return try {
            val success = courseClassDAO.updateStatus(id, status)
            ApiResponse(success = success, message = if (success) "状态更新成功" else "课时不存在")
        } catch (e: Exception) {
            ApiResponse(success = false, message = "更新失败：${e.message}")
        }
    }

    suspend fun getCourseClassById(id: Long): ApiResponse<CourseClass?> {
        return try {
            val courseClass = courseClassDAO.findById(id)
            ApiResponse(success = true, data = courseClass, message = if (courseClass == null) "课时不存在" else "查询成功")
        } catch (e: Exception) {
            ApiResponse(success = false, message = "查询失败：${e.message}")
        }
    }

    suspend fun getAllCourseClasses(teacherName: String): ApiResponse<List<QueryCourseResponse>> {
        return try {
            dbQuery {
                val teacher = teacherDAO.findByName(teacherName) ?: throw IllegalArgumentException("当前老师不存在系统中")
                val outList = ArrayList<QueryCourseResponse>()
                val list = courseClassDAO.findAllByTeacherId(teacher.teacherId!!)
                list?.forEach {
                    outList.add(it.toQueryCourseResponse(teacher))
                }
                ApiResponse(success = true, data = outList)
            }
        } catch (e: IllegalArgumentException) {
            ApiResponse(success = false, code= teacher_has_not_exit, message = "查询失败：${e.message}")
        } catch (e: Exception) {
            ApiResponse(success = false, message = "查询失败：${e.message}")
        }
    }

    suspend fun getAvailableClassesByTemplate(teacherName: String): ApiResponse<List<QueryCourseResponse>> {
        return try {
            dbQuery {
                val teacher = teacherDAO.findByName(teacherName) ?: throw IllegalArgumentException("当前老师不存在系统中")
                val outList = ArrayList<QueryCourseResponse>()
                val list = courseClassDAO.findAvailableAllByTeacherId(teacherId = teacher.teacherId!!)
                list?.forEach {
                    outList.add(it.toQueryCourseResponse(teacher))
                }
                ApiResponse(success = true, data = outList)
            }
        } catch (e: IllegalArgumentException) {
            ApiResponse(success = false, code= teacher_has_not_exit, message = "查询失败：${e.message}")
        } catch (e: Exception) {
            ApiResponse(success = false, message = "查询失败：${e.message}")
        }
    }

    private fun CourseClass.toQueryCourseResponse(teacher: Teacher): QueryCourseResponse {
        return QueryCourseResponse(
            classId = this.classId!!,
            templateId = this.templateId,
            teacherName = teacher.teacherName,
            teacherId = teacher.teacherId!!,
            institutionId = teacher.institutionId,
            startMillisecondTime = this.startMillisecondTime,
            duration = this.duration,
            status = this.status
        )
    }

    // ========== 预约记录服务 ==========
    suspend fun createReservation(reservationReq: CreateReservationReq): ApiResponse<Long> {
        return try {
            dbQuery {
                val localStudent = studentDAO.findByUserInfo(
                    reservationReq.studentName,
                    reservationReq.phone,
                    reservationReq.institutionId)
                if (localStudent == null) throw NullPointerException("该学生信息不存在系统中")
                //更改为已预约状态
                if (courseClassDAO.updateStatus(reservationReq.classId, 1)) {
                    val studentReservation = StudentReservation(
                        classId = reservationReq.classId,
                        studentId = localStudent.studentId!!,
                        institutionId = reservationReq.institutionId,
                        bookedAtms = System.currentTimeMillis()
                    )
                    val id = reservationDAO.create(studentReservation)
                    if (id > 0) {
                        ApiResponse(success = true, data = id, message = "预约成功")
                    } else {
                        ApiResponse(success = false, data = id, message = "预约失败")
                    }
                } else {
                    ApiResponse(success = false, code= course_status_has_change, data = -1, message = "预约失败，当前课时状态已变化")
                }
            }
        } catch (e: NullPointerException) {
            ApiResponse(success = false, code = student_has_not_exit,message = "${e.message}")
        } catch (e: ExposedSQLException) {
            ApiResponse(success = false, code = error_other, message = "预约失败：${e.message}")
        } catch (e: Exception) {
            ApiResponse(success = false, code = error_other, message = "预约失败：${e.message}")
        }
    }

    suspend fun deleteReservation(id: Long): ApiResponse<Boolean> {
        return try {
            val success = reservationDAO.delete(id)
            ApiResponse(success = success, message = if (success) "取消预约成功" else "预约记录不存在")
        } catch (e: Exception) {
            ApiResponse(success = false, message = "取消失败：${e.message}")
        }
    }

    suspend fun updateReservationStatus(id: Long, status: Int): ApiResponse<Boolean> {
        return try {
            val success = reservationDAO.updateStatus(id, status)
            ApiResponse(success = success, message = if (success) "状态更新成功" else "预约记录不存在")
        } catch (e: Exception) {
            ApiResponse(success = false, message = "更新失败：${e.message}")
        }
    }

    suspend fun getReservationById(id: Long): ApiResponse<StudentReservation?> {
        return try {
            val reservation = reservationDAO.findById(id)
            ApiResponse(success = true, data = reservation, message = if (reservation == null) "预约记录不存在" else "查询成功")
        } catch (e: Exception) {
            ApiResponse(success = false, message = "查询失败：${e.message}")
        }
    }

    suspend fun getAllReservations(): ApiResponse<List<StudentReservation>> {
        return try {
            val list = reservationDAO.findAll()
            ApiResponse(success = true, data = list)
        } catch (e: Exception) {
            ApiResponse(success = false, message = "查询失败：${e.message}")
        }
    }

    suspend fun getReservationsByStudent(studentId: Long): ApiResponse<List<StudentReservation>> {
        return try {
            val list = reservationDAO.findByStudent(studentId)
            ApiResponse(success = true, data = list)
        } catch (e: Exception) {
            ApiResponse(success = false, message = "查询失败：${e.message}")
        }
    }

    // ========== 核心业务：抢占课时（原子操作）【补全完整代码】 ==========
    suspend fun reserveClass(studentId: Long, classId: Long): ApiResponse<Boolean> {
        return try {
            dbQuery {
                // 1. 检查课时是否存在且可预约（状态=1）
                val courseClass = courseClassDAO.findById(classId)
                    ?: throw IllegalArgumentException("课时不存在")
                if (courseClass.status != 1) {
                    throw IllegalArgumentException("课时不可预约（已被抢占/取消）")
                }

                // 2. 检查学生是否存在
                val student = studentDAO.findById(studentId)
                    ?: throw IllegalArgumentException("学生不存在")

                // 3. 检查学生是否已预约该课时（避免重复预约）
                val existingReservation = reservationDAO.findByStudent(studentId).firstOrNull { it.classId == classId }
                if (existingReservation != null) {
                    throw IllegalArgumentException("已预约该课时，无需重复抢占")
                }

                // 4. 原子操作：更新课时状态 + 创建预约记录
                // 4.1 更新课时状态为「已抢占」（status=2）
                val updateClassSuccess = courseClassDAO.updateStatus(classId, 2)
                if (!updateClassSuccess) {
                    throw RuntimeException("课时状态更新失败")
                }

                // 4.2 创建预约记录（状态=1：已预约）
//                val reservation = StudentReservation(
//                    studentId = studentId,
//                    classId = classId,
//                    reservationTimestampMs = System.currentTimeMillis(), // 抢占时间
//                    status = 1
//                )
//                reservationDAO.create(reservation)

                // 5. 事务提交（dbQuery 自动处理事务，无异常则提交）
                true
            }.let {
                ApiResponse(success = true, data = true, message = "课时抢占成功")
            }
        } catch (e: IllegalArgumentException) {
            // 业务异常（参数/数据不存在）
            ApiResponse(success = false, data = false, message = e.message ?: "抢占失败")
        } catch (e: ExposedSQLException) {
            // 数据库异常（如唯一键冲突）
            ApiResponse(success = false, data = false, message = "数据库异常：${e.message}")
        } catch (e: Exception) {
            // 其他异常
            ApiResponse(success = false, data = false, message = "抢占失败：${e.message}")
        }
    }
}