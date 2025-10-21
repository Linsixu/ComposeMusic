package com.music.classroom.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import musicclassroom.composeapp.generated.resources.compose_dialog_close_icon
import musicclassroom.composeapp.generated.resources.today_class_back
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoInputBottomSheet(
    navController: NavController,
    onDismiss: () -> Unit, // 关闭弹窗的回调
    onSubmit: (title: String, content: String, dateTime: String) -> Unit
) {
    // 底部弹窗状态（可滑动关闭、设置最大高度）
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true, // 跳过半展开状态
//        hideOnScroll = true // 向下滑动时关闭
    )

    // 输入框状态
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    // 日期和时间状态
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var selectedTime by remember { mutableStateOf<LocalTime?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    // 日期/时间选择器状态
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Clock.System.now().toEpochMilliseconds()
    )
    val timePickerState = rememberTimePickerState(
        initialHour = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).hour,
        initialMinute = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).minute,
        is24Hour = true
    )

    // 日期转换与合并逻辑（复用之前的实现）
    fun convertMillisToLocalDate(millis: Long): LocalDate {
        return Instant.fromEpochMilliseconds(millis)
            .toLocalDateTime(TimeZone.currentSystemDefault()).date
    }
    fun combineDateTime(): LocalDateTime? {
        return if (selectedDate != null && selectedTime != null) {
            LocalDateTime(
                selectedDate!!.year, selectedDate!!.monthNumber, selectedDate!!.dayOfMonth,
                selectedTime!!.hour, selectedTime!!.minute
            )
        } else null
    }
    fun formatDateTime(): String {
        val dateTime = combineDateTime() ?: return ""
        return with(dateTime) {
            "${year}-${monthNumber.toString().padStart(2, '0')}-${dayOfMonth.toString().padStart(2, '0')} " +
                    "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"
        }
    }

    // 确认选择日期/时间
    fun onConfirmDate() {
        datePickerState.selectedDateMillis?.let { selectedDate = convertMillisToLocalDate(it) }
        showDatePicker = false
        if (selectedDate != null) showTimePicker = true
    }
    fun onConfirmTime() {
        selectedTime = LocalTime(timePickerState.hour, timePickerState.minute)
        showTimePicker = false
    }

    // 提交逻辑
    fun onSubmit() {
        if (title.isNotBlank() && content.isNotBlank() && combineDateTime() != null) {
            // 处理提交（如保存数据）
            onDismiss() // 提交后关闭底部弹窗
        }
    }

    // 底部弹窗容器
    ModalBottomSheet(
        sheetState = bottomSheetState,
        onDismissRequest = onDismiss, // 点击外部或滑动关闭
        modifier = Modifier.fillMaxSize()
    ) {
        // 弹窗内容（滚动布局，避免内容过长溢出）
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 标题栏（带关闭按钮）
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "填写信息",
                    style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.Center)
                )
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Image(
                        painterResource(Res.drawable.compose_dialog_close_icon),
                        contentDescription = "关闭",
                        modifier = Modifier.size(48.dp)
                    )
                }
            }

            // 输入表单（垂直排列）
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
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
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )

                // 日期+时间输入框
                OutlinedTextField(
                    value = formatDateTime(),
                    onValueChange = {},
                    label = { Text("选择日期和时间") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDatePicker = true },
                    readOnly = true,
                    trailingIcon = { Text("⏰", modifier = Modifier.clickable { showDatePicker = true }) }
                )

                // 提交按钮
                Button(
                    onClick = ::onSubmit,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = title.isNotBlank() && content.isNotBlank() && combineDateTime() != null
                ) {
                    Text("提交")
                }
            }
        }
    }

    // 日期选择弹窗（叠加显示在底部弹窗上）
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = { TextButton(onClick = ::onConfirmDate) { Text("确认") } },
            dismissButton = { TextButton(onClick = { showDatePicker = false }) { Text("取消") } }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // 时间选择弹窗（用 AlertDialog 兼容低版本）
    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            title = { Text("选择时间") },
            text = { TimePicker(state = timePickerState) },
            confirmButton = { TextButton(onClick = ::onConfirmTime) { Text("确认") } },
            dismissButton = { TextButton(onClick = { showTimePicker = false }) { Text("取消") } }
        )
    }
}