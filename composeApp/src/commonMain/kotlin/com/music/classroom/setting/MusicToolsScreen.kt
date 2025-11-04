package com.music.classroom.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.music.classroom.SPKeyUtils
import com.music.classroom.SPKeyUtils.DEFAULT_MUSIC_TOOLS
import com.music.classroom.color.bgPrimaryColor
import com.music.classroom.color.primaryColor
import com.music.classroom.color.unselectPrimaryColor
import com.music.classroom.storage.spStorage
import com.music.classroom.util.showToast
import musicclassroom.composeapp.generated.resources.Res
import musicclassroom.composeapp.generated.resources.today_class_back
import org.jetbrains.compose.resources.painterResource

/**
 * @author: linsixu@ruqimobility.com
 * @date:2025/10/29
 * 用途：
 */
@Composable
fun MusicToolsScreen(
    navController: NavController
) {
    var inputValue by remember { mutableStateOf("") }
    var submitTips by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    LaunchedEffect(Unit) {
        val savedValue = spStorage.getString(DEFAULT_MUSIC_TOOLS)
        if (savedValue.isNotBlank()) {
            inputValue = savedValue
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // 列表（占满屏幕）
        Column(
            modifier = Modifier.fillMaxSize().background(bgPrimaryColor)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp), // Item 上下间距8dp
//            contentPadding = PaddingValues(16.dp) // 列表左右内边距
        ) {
// 顶部栏：返回按钮 + 居中标题（同一行）
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp), // 保持原高度
                verticalAlignment = Alignment.CenterVertically // 垂直居中对齐
            ) {
                // 左侧返回按钮（不占权重，固定在左侧）
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.size(48.dp) // 增大点击区域，提升体验
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.today_class_back),
                        contentDescription = "返回",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // 中间标题（占满剩余空间，实现居中）
                Text(
                    text = "设置默认乐器值",
                    fontSize = 20.sp,
                    modifier = Modifier.weight(1f), // 权重1，占据剩余空间
                    textAlign = TextAlign.Center // 文字在自身区域内居中
                )

                // 右侧占位（宽度与返回按钮一致，确保标题水平居中）
                Box(modifier = Modifier.size(48.dp))
            }

            // 输入区域（保持不变）
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = 16.dp), // 与顶部栏保持间距
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                OutlinedTextField(
                    value = inputValue,
                    onValueChange = {
                        inputValue = it
                        if (submitTips.isNotBlank()) submitTips = ""
                    },
                    label = { Text("请输入默认乐器（如：钢琴）") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        when {
                            inputValue.isBlank() -> submitTips = "请输入默认乐器"
//                            inputValue.toIntOrNull() == null -> submitTips = "请输入有效的数字"
                            else -> {
                                focusManager.clearFocus()
                                SPKeyUtils.currentMusicTools.value = inputValue
                                spStorage.saveString(DEFAULT_MUSIC_TOOLS, inputValue)
                                submitTips = "保存成功"
                                showToast("保存成功")
                                navController.popBackStack()
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    colors = ButtonColors(
                        contentColor = primaryColor,
                        containerColor = primaryColor,
                        disabledContainerColor = unselectPrimaryColor,
                        disabledContentColor = unselectPrimaryColor
                    ),
                ) {
                    Text(text = "提交", fontSize = 16.sp, color = Color.White)
                }

                if (submitTips.isNotBlank()) {
                    Text(
                        text = submitTips,
                        color = if (submitTips == "提交成功") {
                            Color(0xFF00C853)
                        } else {
                            Color(0xFFD32F2F)
                        },
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }
            }
        }
    }
}