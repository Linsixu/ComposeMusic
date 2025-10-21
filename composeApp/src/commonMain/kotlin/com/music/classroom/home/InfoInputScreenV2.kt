package com.music.classroom.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import musicclassroom.composeapp.generated.resources.Res
import musicclassroom.composeapp.generated.resources.today_class_back
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoInputScreen(navController: NavController) {
    // 输入框状态
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    // 日期和时间状态（分开存储）
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var selectedTime by remember { mutableStateOf<LocalTime?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    // 日期选择器状态（基础组件，大多数版本都有）
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Clock.System.now().toEpochMilliseconds()
    )

    // 时间选择器状态（基础组件）
    val timePickerState = rememberTimePickerState(
        initialHour = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).hour,
        initialMinute = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).minute,
        is24Hour = true // 24小时制
    )

    // 转换：毫秒值 → LocalDate
    fun convertMillisToLocalDate(millis: Long): LocalDate {
        return Instant.fromEpochMilliseconds(millis)
            .toLocalDateTime(TimeZone.currentSystemDefault()).date
    }

    // 合并日期和时间为 LocalDateTime
    fun combineDateTime(): LocalDateTime? {
        return if (selectedDate != null && selectedTime != null) {
            LocalDateTime(
                year = selectedDate!!.year,
                monthNumber = selectedDate!!.monthNumber,
                dayOfMonth = selectedDate!!.dayOfMonth,
                hour = selectedTime!!.hour,
                minute = selectedTime!!.minute
            )
        } else null
    }

    // 格式化显示：日期+时间 → "yyyy-MM-dd HH:mm"
    fun formatDateTime(): String {
        val dateTime = combineDateTime() ?: return ""
        return with(dateTime) {
            "${year}-${monthNumber.toString().padStart(2, '0')}-${dayOfMonth.toString().padStart(2, '0')} " +
                    "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"
        }
    }

    // 确认选择日期（自动弹出时间选择器）
    fun onConfirmDate() {
        datePickerState.selectedDateMillis?.let {
            selectedDate = convertMillisToLocalDate(it)
        }
        showDatePicker = false
        if (selectedDate != null) {
            showTimePicker = true // 日期选完自动跳时间选择
        }
    }

    // 确认选择时间
    fun onConfirmTime() {
        selectedTime = LocalTime(
            hour = timePickerState.hour,
            minute = timePickerState.minute
        )
        showTimePicker = false
    }

    // 返回和提交逻辑
    fun onBackClick() = navController.popBackStack()
    fun onSubmitClick() {
        if (title.isNotBlank() && content.isNotBlank() && combineDateTime() != null) {
            // 提交逻辑（使用 combineDateTime() 获取完整日期时间）
            navController.popBackStack()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // 顶部导航栏
        TopAppBar(
            title = { Text("填写信息") },
            navigationIcon = {
                IconButton(onClick = ::onBackClick) {
                    Image(
                        painterResource(Res.drawable.today_class_back),
                        contentDescription = "返回",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        )

        // 表单内容
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            // 标题输入框
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("请输入标题") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            // 内容输入框
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("请输入内容") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                maxLines = 5
            )

            // 日期+时间输入框（一个输入框显示）
            OutlinedTextField(
                value = formatDateTime(),
                onValueChange = {}, // 禁止手动输入
                label = { Text("选择日期和时间") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .clickable { showDatePicker = true }, // 点击触发选择流程
                readOnly = true,
                trailingIcon = { Text("⏰", modifier = Modifier.clickable { showDatePicker = true }) }
            )

            // 提交按钮
            Button(
                onClick = ::onSubmitClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                enabled = title.isNotBlank() && content.isNotBlank() && combineDateTime() != null
            ) {
                Text("提交")
            }
        }
    }

    // 1. 日期选择弹窗（使用原生 DatePickerDialog）
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = { TextButton(onClick = ::onConfirmDate) { Text("确认") } },
            dismissButton = { TextButton(onClick = { showDatePicker = false }) { Text("取消") } }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // 2. 时间选择弹窗（用 AlertDialog 包裹 TimePicker，替代 TimePickerDialog）
    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            title = { Text("选择时间") },
            text = {
                // 时间选择器本体
                TimePicker(
                    state = timePickerState,
                    modifier = Modifier.padding(8.dp)
                )
            },
            confirmButton = {
                TextButton(onClick = ::onConfirmTime) {
                    Text("确认")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text("取消")
                }
            }
        )
    }
}