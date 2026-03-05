package org.example.project.route

import org.example.project.db.manager.CourseReservationService
import org.example.project.db.model.CourseClass
import org.example.project.db.model.CourseTemplate
import org.example.project.db.model.EduInstitution
import org.example.project.db.model.ReserveRequest
import org.example.project.db.model.Student
import org.example.project.db.model.StudentReservation
import org.example.project.db.model.Teacher

/**
 * @author: linsixu@ruqimobility.com
 * @date:2026/3/4
 * 用途：
 */
// src/main/kotlin/com/example/routes/CourseReservationRoutes.kt
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

// 封装所有路由，依赖注入服务层
fun Route.courseReservationRoutes(service: CourseReservationService) {
    // ====================== 1. 机构相关接口 ======================
    route("/institutions") {
        // 1.1 创建机构（POST /institutions）
        post {
            val institution = call.receive<EduInstitution>()
            val response = service.createInstitution(institution)
            call.respond(response)
        }

        // 1.2 删除机构（DELETE /institutions/{id}）
        delete("{id}") {
            val id = call.parameters["id"]?.toLong() ?: throw IllegalArgumentException("机构ID不能为空")
            val response = service.deleteInstitution(id)
            call.respond(response)
        }

        // 1.3 更新机构（PUT /institutions/{id}）
        put("{id}") {
            val id = call.parameters["id"]?.toLong() ?: throw IllegalArgumentException("机构ID不能为空")
            val institution = call.receive<EduInstitution>()
            val response = service.updateInstitution(id, institution)
            call.respond(response)
        }

        // 1.4 根据ID查询机构（GET /institutions/{id}）
        get("{id}") {
            val id = call.parameters["id"]?.toLong() ?: throw IllegalArgumentException("机构ID不能为空")
            val response = service.getInstitutionById(id)
            call.respond(response)
        }

        // 1.5 查询所有机构（GET /institutions）
        get {
            val response = service.getAllInstitutions()
            call.respond(response)
        }
    }

    // ====================== 2. 老师相关接口 ======================
    route("/teachers") {
        // 2.1 创建老师（POST /teachers）
        post {
            val teacher = call.receive<Teacher>()
            val response = service.createTeacher(teacher)
            call.respond(response)
        }

        // 2.2 删除老师（DELETE /teachers/{id}）
        delete("{id}") {
            val id = call.parameters["id"]?.toLong() ?: throw IllegalArgumentException("老师ID不能为空")
            val response = service.deleteTeacher(id)
            call.respond(response)
        }

        // 2.3 更新老师（PUT /teachers/{id}）
        put("{id}") {
            val id = call.parameters["id"]?.toLong() ?: throw IllegalArgumentException("老师ID不能为空")
            val teacher = call.receive<Teacher>()
            val response = service.updateTeacher(id, teacher)
            call.respond(response)
        }

        // 2.4 根据ID查询老师（GET /teachers/{id}）
        get("{id}") {
            val id = call.parameters["id"]?.toLong() ?: throw IllegalArgumentException("老师ID不能为空")
            val response = service.getTeacherById(id)
            call.respond(response)
        }

        // 2.5 查询所有老师（GET /teachers）
        get {
            val response = service.getAllTeachers()
            call.respond(response)
        }

        // 2.6 根据机构ID查询老师（GET /teachers/institution/{institutionId}）
        get("institution/{institutionId}") {
            val institutionId = call.parameters["institutionId"]?.toLong() ?: throw IllegalArgumentException("机构ID不能为空")
            val response = service.getTeachersByInstitution(institutionId)
            call.respond(response)
        }
    }

    // ====================== 3. 学生相关接口 ======================
    route("/students") {
        // 3.1 创建学生（POST /students）
        post {
            val student = call.receive<Student>()
            val response = service.createStudent(student)
            call.respond(response)
        }

        // 3.2 删除学生（DELETE /students/{id}）
        delete("{id}") {
            val id = call.parameters["id"]?.toLong() ?: throw IllegalArgumentException("学生ID不能为空")
            val response = service.deleteStudent(id)
            call.respond(response)
        }

        // 3.3 更新学生（PUT /students/{id}）
        put("{id}") {
            val id = call.parameters["id"]?.toLong() ?: throw IllegalArgumentException("学生ID不能为空")
            val student = call.receive<Student>()
            val response = service.updateStudent(id, student)
            call.respond(response)
        }

        // 3.4 根据ID查询学生（GET /students/{id}）
        get("{id}") {
            val id = call.parameters["id"]?.toLong() ?: throw IllegalArgumentException("学生ID不能为空")
            val response = service.getStudentById(id)
            call.respond(response)
        }

        // 3.5 查询所有学生（GET /students）
        get {
            val response = service.getAllStudents()
            call.respond(response)
        }
    }

    // ====================== 4. 课程模板相关接口 ======================
    route("/course-templates") {
        // 4.1 创建课程模板（POST /course-templates）
        post {
            val template = call.receive<CourseTemplate>()
            val response = service.createCourseTemplate(template)
            call.respond(response)
        }

        // 4.2 删除课程模板（DELETE /course-templates/{id}）
        delete("{id}") {
            val id = call.parameters["id"]?.toLong() ?: throw IllegalArgumentException("模板ID不能为空")
            val response = service.deleteCourseTemplate(id)
            call.respond(response)
        }

        // 4.3 更新课程模板（PUT /course-templates/{id}）
        put("{id}") {
            val id = call.parameters["id"]?.toLong() ?: throw IllegalArgumentException("模板ID不能为空")
            val template = call.receive<CourseTemplate>()
            val response = service.updateCourseTemplate(id, template)
            call.respond(response)
        }

        // 4.4 根据ID查询模板（GET /course-templates/{id}）
        get("{id}") {
            val id = call.parameters["id"]?.toLong() ?: throw IllegalArgumentException("模板ID不能为空")
            val response = service.getCourseTemplateById(id)
            call.respond(response)
        }

        // 4.5 查询所有模板（GET /course-templates）
        get {
            val response = service.getAllCourseTemplates()
            call.respond(response)
        }

        // 4.6 根据老师ID查询模板（GET /course-templates/teacher/{teacherId}）
        get("teacher/{teacherId}") {
            val teacherId = call.parameters["teacherId"]?.toLong() ?: throw IllegalArgumentException("老师ID不能为空")
            val response = service.getTemplatesByTeacher(teacherId)
            call.respond(response)
        }
    }

    // ====================== 5. 课时相关接口 ======================
    route("/course-classes") {
        // 5.1 创建课时（POST /course-classes）
        post {
            val courseClass = call.receive<CourseClass>()
            val response = service.createCourseClass(courseClass)
            call.respond(response)
        }

        // 5.2 删除课时（DELETE /course-classes/{id}）
        delete("{id}") {
            val id = call.parameters["id"]?.toLong() ?: throw IllegalArgumentException("课时ID不能为空")
            val response = service.deleteCourseClass(id)
            call.respond(response)
        }

        // 5.3 更新课时状态（PUT /course-classes/{id}/status/{status}）
        put("{id}/status/{status}") {
            val id = call.parameters["id"]?.toLong() ?: throw IllegalArgumentException("课时ID不能为空")
            val status = call.parameters["status"]?.toInt() ?: throw IllegalArgumentException("状态值不能为空")
            val response = service.updateClassStatus(id, status)
            call.respond(response)
        }

        // 5.4 根据ID查询课时（GET /course-classes/{id}）
        get("{id}") {
            val id = call.parameters["id"]?.toLong() ?: throw IllegalArgumentException("课时ID不能为空")
            val response = service.getCourseClassById(id)
            call.respond(response)
        }

        // 5.5 查询所有课时（GET /course-classes）
        get {
            val response = service.getAllCourseClasses()
            call.respond(response)
        }

        // 5.6 查询模板下可预约课时（GET /course-classes/available/{templateId}）
        get("available/{templateId}") {
            val templateId = call.parameters["templateId"]?.toLong() ?: throw IllegalArgumentException("模板ID不能为空")
            val response = service.getAvailableClassesByTemplate(templateId)
            call.respond(response)
        }
    }

    // ====================== 6. 预约记录相关接口 ======================
    route("/reservations") {
        // 6.1 创建预约记录（POST /reservations）
        post {
            val reservation = call.receive<StudentReservation>()
            val response = service.createReservation(reservation)
            call.respond(response)
        }

        // 6.2 删除预约记录（取消预约）（DELETE /reservations/{id}）
        delete("{id}") {
            val id = call.parameters["id"]?.toLong() ?: throw IllegalArgumentException("预约ID不能为空")
            val response = service.deleteReservation(id)
            call.respond(response)
        }

        // 6.3 更新预约状态（PUT /reservations/{id}/status/{status}）
        put("{id}/status/{status}") {
            val id = call.parameters["id"]?.toLong() ?: throw IllegalArgumentException("预约ID不能为空")
            val status = call.parameters["status"]?.toInt() ?: throw IllegalArgumentException("状态值不能为空")
            val response = service.updateReservationStatus(id, status)
            call.respond(response)
        }

        // 6.4 根据ID查询预约记录（GET /reservations/{id}）
        get("{id}") {
            val id = call.parameters["id"]?.toLong() ?: throw IllegalArgumentException("预约ID不能为空")
            val response = service.getReservationById(id)
            call.respond(response)
        }

        // 6.5 查询所有预约记录（GET /reservations）
        get {
            val response = service.getAllReservations()
            call.respond(response)
        }

        // 6.6 根据学生ID查询预约记录（GET /reservations/student/{studentId}）
        get("student/{studentId}") {
            val studentId = call.parameters["studentId"]?.toLong() ?: throw IllegalArgumentException("学生ID不能为空")
            val response = service.getReservationsByStudent(studentId)
            call.respond(response)
        }
    }

    // ====================== 7. 核心业务：抢占课时接口 ======================
    route("/reserve") {
        // 7.1 抢占课时（POST /reserve/class）
        post("class") {
            val request = call.receive<ReserveRequest>() // 接收学生ID和课时ID
            val response = service.reserveClass(request.studentId, request.classId)
            call.respond(response)
        }
    }
}