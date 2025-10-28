package com.music.classroom.home.menu

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// 菜单选择框（替换原有的 OutlinedTextField）
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuSelectionBox(
    // 定义回调函数，参数为选中的选项（字符串类型）
    onOptionSelected: (String) -> Unit
) {
    // 选项列表（可根据需求修改）
    val options = listOf("1级", "2级", "3级", "4级", "5级", "6级", "7级", "8级", "9级", "10级")

    // 状态管理：当前选中项、菜单是否展开
    var selectedOption by remember { mutableStateOf(options[0]) } // 默认选中第一项
    var expanded by remember { mutableStateOf(false) }

    // 暴露式下拉菜单（外观类似输入框，点击展开选项）
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }, // 点击切换展开状态
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        // 选择框主体（类似输入框的外观）
        OutlinedTextField(
            value = selectedOption, // 显示当前选中项
            onValueChange = { /* 禁止手动输入，仅通过菜单选择 */ },
            label = { Text("考级内容") }, // 提示文字
            readOnly = true, // 设为只读，防止手动输入
            trailingIcon = {
                // 下拉箭头图标（自动根据展开状态旋转）
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor() // 关键：关联下拉菜单的锚点
                .fillMaxWidth()
        )

        // 下拉菜单选项列表
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false } // 点击外部关闭菜单
        ) {
            // 遍历选项列表，生成菜单项
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        selectedOption = option // 选中当前项
                        onOptionSelected(option)
                        expanded = false // 选中后关闭菜单
                    }
                )
            }
        }
    }
}