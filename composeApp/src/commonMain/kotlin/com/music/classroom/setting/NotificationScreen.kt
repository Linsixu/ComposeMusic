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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.music.classroom.color.bgPrimaryColor
import musicclassroom.composeapp.generated.resources.Res
import musicclassroom.composeapp.generated.resources.today_class_back
import org.jetbrains.compose.resources.painterResource

/**
 * @author: linsixu@ruqimobility.com
 * @date:2025/10/28
 * 用途：
 */
@Composable
fun NotificationScreen(
    navController: NavController
) {
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
                    text = "提示功能",
                    fontSize = 20.sp,
                    modifier = Modifier.weight(1f), // 权重1，占据剩余空间
                    textAlign = TextAlign.Center // 文字在自身区域内居中
                )

                // 右侧占位（宽度与返回按钮一致，确保标题水平居中）
                Box(modifier = Modifier.size(48.dp))
            }

            Text("该功能暂未开发", fontSize = 16.sp)
        }
    }
}