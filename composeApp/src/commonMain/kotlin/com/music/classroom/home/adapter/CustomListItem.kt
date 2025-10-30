package com.music.classroom.home.adapter

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.launch
import musicclassroom.composeapp.generated.resources.Res
import musicclassroom.composeapp.generated.resources.list_message_item_delete
import org.jetbrains.compose.resources.painterResource
import kotlin.math.roundToInt

/**
 * 自定义ListItem：显示名字、时间和底部两个按钮
 */
@Composable
fun CustomListItem(
    title: String,
    time: String,
    onButton1Click: () -> Unit,
    onButton2Click: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
// 1. 先设置阴影（外层阴影，避免被padding裁剪）
            .shadow(
                elevation = 2.dp, // 阴影高度（值越大阴影越明显）
                shape = RoundedCornerShape(12.dp), // 阴影与背景同圆角，视觉统一
                ambientColor = Color(0x1A000000), // 环境阴影色（淡黑色，透明度20%）
                spotColor = Color(0x33000000) // 光斑阴影色（深黑色，透明度30%）
            )
            // 2. 再设置白色背景（背景在阴影内层，阴影包裹背景）
            .background(
                color = Color.White, // 白色背景
                shape = RoundedCornerShape(12.dp) // 背景圆角（与阴影一致）
            )
            // 3. 最后设置内边距（padding在背景内部，避免内容紧贴边缘）
            .padding(16.dp)
    ) {
        // 名字和时间
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = time,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // 底部两个按钮
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onButton1Click,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("编辑")
            }
            Button(
                onClick = onButton2Click,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF44336),
                    contentColor = Color.White
                )
            ) {
                Text("详情")
            }
        }
    }
}

/**
 * 左滑删除容器（基于 rememberDraggableState 实现，适配低版本 Compose）
 */
@Composable
fun SwipeToDeleteItem(
    onDelete: () -> Unit,
    content: @Composable () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val deleteWidth = 80.dp // 删除区域宽度
    val localDensity = LocalDensity.current
    val deleteWidthPx = with(localDensity) { deleteWidth.toPx() }

    // todo 这种设置新的值也不会刷新
//    val (offsetX, setoffsetX) = remember { mutableStateOf(0f) }

    //todo 要导入：import androidx.compose.runtime.setValue
    var offsetX by remember { mutableStateOf(0f) }

    // 滑动背景色动画（左滑超过一半时变红）
    val backgroundColor by animateColorAsState(
        targetValue = if (offsetX < -deleteWidthPx / 2) {
            Color(0xFFF44336)
        } else {
            Color.Transparent
        },
        label = "删除背景色动画"
    )

    // 水平滑动手势检测
    val dragModifier = Modifier.pointerInput(Unit) {
        detectHorizontalDragGestures(
            onDragStart = { offset ->
                println("magic onDragStart offset=$offset")
            },
            // 拖动过程中更新偏移量
            onHorizontalDrag = { change, dragAmount ->
                println("magic onDrag dragAmount=$dragAmount")
                change.consumeAllChanges()
                // 直接更新 offsetX（委托语法生效）
                offsetX = (offsetX + dragAmount).coerceIn(-deleteWidthPx, 0f)
            },
            // 拖动结束后判断是否触发删除
            onDragEnd = {
                println("magic onDragEnd")
                coroutineScope.launch {
                    if (offsetX < -(deleteWidthPx / 2)) {
                        offsetX = -deleteWidthPx
//                        onDelete()
                    } else {
                        offsetX = 0f
                    }
//                    offsetX = 0f
                }
            },
            // 拖动取消时回弹
            onDragCancel = {
                println("magic onDragCancel")
                coroutineScope.launch {
                    offsetX = 0f
                }
            }
        )
    }

    // 布局结构：底层删除图标 + 上层可滑动内容
    Box(modifier = Modifier.fillMaxWidth()) {
        // 底层：删除图标（左滑时显示）
        Box(
            modifier = Modifier
                .matchParentSize().clickable {
                    onDelete()
                },
//                .background(backgroundColor),
            contentAlignment = Alignment.CenterEnd,
        ) {
            //todo Icon会把图片可以tint颜色，如果纯图片用Image
            Image(
                painter = painterResource(Res.drawable.list_message_item_delete),
                contentDescription = "删除",
//                tint = Color.White,
                modifier = Modifier.padding(end = 10.dp),
            )
        }

        // 上层：可滑动的 Item 内容
        Box(
            modifier = Modifier
                .offset {
                    println("current offsetX= ${offsetX.roundToInt()}")
                    IntOffset(offsetX.roundToInt(), 0)
                } // 跟随滑动偏移
                .then(dragModifier)
                .zIndex(1f) // 确保内容在删除图标上方
        ) {
            content()
        }
    }
}