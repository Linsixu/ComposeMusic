package com.music.classroom.allpage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.music.classroom.color.bgPrimaryColor
import com.music.classroom.home.adapter.SwipeToDeleteItem
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import musicclassroom.composeapp.generated.resources.Res
import musicclassroom.composeapp.generated.resources.select_arrow_down_gray_icon
import org.jetbrains.compose.resources.painterResource

// 课程数据类（使用kotlinx.datetime处理时间）
data class CourseItem(
    val studentName: String,    // 学生姓名
    val musicToolName: String, //乐器种类
    val gradeRange: String,     // 考级范围
    val startTime: LocalDateTime, // 开始时间（LocalDateTime类型）
    val lessonMinute: Int        // 课时（单位：分钟）
) {
    // 计算结束时间：开始时间 + 课时（基于kotlinx.datetime的plus方法）
    val startMilliSeconds =
        startTime.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    val endSecondTime = startMilliSeconds + lessonMinute * 60L * 1000L
    val endDateTime = Instant.fromEpochMilliseconds(endSecondTime)
        .toLocalDateTime(TimeZone.currentSystemDefault())

    // 时间格式化（统一格式：yyyy-MM-dd HH:mm）
    @OptIn(FormatStringsInDatetimeFormats::class)
    val dateTimeFormat = LocalDateTime.Format {
        byUnicodePattern("yyyy-MM-dd HH:mm")
    }
    private val timeFormatter = dateTimeFormat

    // 格式化后的开始时间字符串
    val formattedStartTime: String = timeFormatter.format(startTime)

    // 格式化后的结束时间字符串
    val formattedEndTime: String = timeFormatter.format(endDateTime)
}

@Composable
fun AllCourse() {
    // 当前时间（kotlinx.datetime）
    val currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val currentYear = currentDateTime.year.toString()
    val currentMonth = currentDateTime.monthNumber.toString()

    // 年月选择状态
    var selectedYear by remember { mutableStateOf(currentYear) }
    var selectedMonth by remember { mutableStateOf(currentMonth) }
    var yearMenuExpanded by remember { mutableStateOf(false) }
    var monthMenuExpanded by remember { mutableStateOf(false) }

    // 选项列表
    val yearOptions = (currentDateTime.year - 5..currentDateTime.year).map { it.toString() }
    val monthOptions = (1..12).map { it.toString() }

    // 课程列表数据
    var courseList by remember { mutableStateOf<List<CourseItem>>(emptyList()) }

    // 刷新列表
    fun refreshCourseList(selectedYear: String, selectedMonth: String) {
        val year = selectedYear.toIntOrNull() ?: currentDateTime.year
        val month = selectedMonth.toIntOrNull()?.coerceIn(1, 12) ?: currentDateTime.monthNumber

        courseList = listOf(
            CourseItem("张三", "钢琴", "5级", LocalDateTime(year, month, 5, 14, 0), 40),
            CourseItem("李四", "小提琴", "3级", LocalDateTime(year, month, 12, 10, 30), 40),
            CourseItem("王五", "古筝", "6级", LocalDateTime(year, month, 18, 16, 0), 40),
            CourseItem("赵六", "吉他", "4级", LocalDateTime(year, month, 25, 9, 0), 40)
        )
    }

    // 初始化刷新
    remember(selectedYear, selectedMonth) {
        refreshCourseList(selectedYear, selectedMonth)
    }

    Column(
        modifier = Modifier.fillMaxSize().background(bgPrimaryColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 顶部年月菜单栏（带背景色和边框）
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalArrangement = Arrangement.Center,
            maxItemsInEachRow = 2
        ) {
            // 年份选择器（带背景色+边框+点击反馈）
            Box(modifier = Modifier.padding(end = 16.dp)) {
                Box(
                    modifier = Modifier
                        .clickable { yearMenuExpanded = true }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .background(
                            color = Color(0xFFF5F5F5), // 浅灰色背景，突出可点击
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(10.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = Color(0xFFEEEEEE), // 浅灰色边框，增强边界感
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(10.dp)
                        )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "年份：$selectedYear", fontSize = 16.sp)
                        // 下拉箭头，强化“可展开”提示
                        Image(
                            painter = painterResource(Res.drawable.select_arrow_down_gray_icon),
                            contentDescription = "展开年份选择",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // 年份下拉菜单
                DropdownMenu(
                    expanded = yearMenuExpanded,
                    onDismissRequest = { yearMenuExpanded = false },
                    modifier = Modifier
                        .background(Color.White)
                        .border(
                            1.dp,
                            Color(0xFFEEEEEE),
                            androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                        )
                ) {
                    yearOptions.forEach { year ->
                        DropdownMenuItem(
                            text = { Text(text = year, fontSize = 14.sp) },
                            onClick = {
                                selectedYear = year
                                yearMenuExpanded = false
                                refreshCourseList(selectedYear, selectedMonth)
                            }
                        )
                    }
                }
            }

            // 月份选择器（同年份样式，保持一致）
            Box {
                Box(
                    modifier = Modifier
                        .clickable { monthMenuExpanded = true }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .background(
                            color = Color(0xFFF5F5F5),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(10.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = Color(0xFFEEEEEE),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(10.dp)
                        )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "月份：$selectedMonth", fontSize = 16.sp)
                        Image(
                            painter = painterResource(Res.drawable.select_arrow_down_gray_icon),
                            contentDescription = "展开月份选择",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // 月份下拉菜单
                DropdownMenu(
                    expanded = monthMenuExpanded,
                    onDismissRequest = { monthMenuExpanded = false },
                    modifier = Modifier
                        .background(Color.White)
                        .border(
                            1.dp,
                            Color(0xFFEEEEEE),
                            androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                        )
                ) {
                    monthOptions.forEach { month ->
                        DropdownMenuItem(
                            text = { Text(text = month, fontSize = 14.sp) },
                            onClick = {
                                selectedMonth = month
                                monthMenuExpanded = false
                                refreshCourseList(selectedYear, selectedMonth)
                            }
                        )
                    }
                }
            }
        }

        // 课程列表（保持不变）
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(top = 16.dp).background(bgPrimaryColor),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            if (courseList.isEmpty()) {
                item {
                    Text(
                        text = "暂无课程数据",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.fillMaxWidth().padding(24.dp),
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                items(courseList) { course ->
                    CourseListItem(course = course)
                }
            }
        }
    }
}

// 课程列表Item
@Composable
private fun CourseListItem(course: CourseItem) {
    SwipeToDeleteItem(
        onDelete = { /* 执行删除逻辑，如从列表中移除 item */ }
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "${course.studentName} | ${course.musicToolName + course.gradeRange}",
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Text(
                    "开始时间：${course.formattedStartTime}",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    "结束时间：${course.formattedEndTime}",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Text(
                    "课时：${course.lessonMinute}分钟",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}