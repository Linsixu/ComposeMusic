package org.example.project

/**
 * @author: linsixu@ruqimobility.com
 * @date:2026/3/6
 * 用途：
 */
object SqlCode {
    const val success_code = 200
    //机构
    const val institution_has_exit = 5000

    const val institution_not_exit = 5001


    //学生
    const val student_has_exit = 6000
    const val student_has_not_exit = 6001

    const val error_other = 9999

    //老师
    const val teacher_has_exit = 7000
    const val teacher_has_not_exit = 7001

    //课程模版
    const val template_course_has_exit = 8000

    //课时
    const val course_has_exit_at_same_time = 9000
    //课时id不存在
    const val course_id_has_not_exit = 9001
    //课时状态已经变化，无法修改
    const val course_status_has_change = 9002
}