package org.example.project.request.course

/**
 * @author: linsixu@ruqimobility.com
 * @date:2026/3/11
 * 用途：创建课时
 */
data class CreateCourseReq(
    val templateId: Long,
    val teacherName: String,
    /**
     * 毫秒时间戳
     */
    val startMillisecondTime: Long,
    val duration: Int,
    val status: Int
)