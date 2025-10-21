//package com.music.classroom.home
//
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.material3.DatePicker
//import androidx.compose.material3.DatePickerDialog
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Modifier
//import kotlinx.datetime.LocalDate
//import kotlinx.datetime.TimeZone
//import kotlinx.datetime.TimeZone.Companion.currentSystemDefault
//import kotlinx.datetime.todayAt
//import kotlin.time.Clock
//
//@Composable
//fun CommonDatePickerField(
//    modifier: Modifier = Modifier,
//    label: String = "选择日期"
//) {
//    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
//    var showDialog by remember { mutableStateOf(false) }
//    val timeZone = currentSystemDefault() // 获取系统默认时区
//    val today = Clock.System.todayAt(timeZone) // 替代 LocalDate.todayAt()
//
//    // 日期格式化（显示为 "yyyy-MM-dd"）
//    val dateText = selectedDate?.toString() ?: ""
//
//    // 输入框（点击弹出日历）
//    OutlinedTextField(
//        value = dateText,
//        onValueChange = {},
//        label = { Text(label) },
//        modifier = modifier
//            .fillMaxWidth()
//            .clickable { showDialog = true },
//        readOnly = true,
//        trailingIcon = { Text("📅") }
//    )
//
//    // 日历弹窗（跨平台统一实现）
//    if (showDialog) {
//        DatePickerDialog(
//            onDismissRequest = { showDialog = false },
//            confirmButton = {
//                Text("确认", modifier = Modifier.clickable {
//                    showDialog = false
//                })
//            },
//            dismissButton = {
//                Text("取消", modifier = Modifier.clickable {
//                    showDialog = false
//                })
//            }
//        ) {
//            DatePicker(
//                state = MaterialDatePickerDefaults.datePickerState(
//                    initialSelectedDate = selectedDate ?: today, // 初始选中当前日期
//                    minDate = today.minusYears(100), // 最小可选日期
//                    maxDate = today.plusYears(100)  // 最大可选日期
//                ),
//                onDateSelected = { selectedDate = it }
//            )
//        }
//    }
//}