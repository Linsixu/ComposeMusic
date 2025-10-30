package com.music.classroom.page


//import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.music.classroom.allpage.CourseItem
import com.music.classroom.color.bgPrimaryColor
import com.music.classroom.color.primaryColor
import com.music.classroom.home.InfoInputBottomSheet
import com.music.classroom.home.adapter.CustomListItem
import com.music.classroom.home.adapter.SwipeToDeleteItem
import kotlinx.datetime.LocalDateTime
import musicclassroom.composeapp.generated.resources.Res
import musicclassroom.composeapp.generated.resources.add_message_icon
import org.jetbrains.compose.resources.painterResource

/**
 * @author: linsixu@ruqimobility.com
 * @date:2025/10/17
 * 用途：
 */
@Composable
fun HomeScreen(navController: NavController) {
    // 课程列表数据
    var courseList by remember { mutableStateOf<ArrayList<CourseItem>>(arrayListOf()) }
    courseList = arrayListOf(
        CourseItem("张三", "钢琴", "5级", LocalDateTime(2025, 11, 5, 14, 0), 40),
        CourseItem("李四", "小提琴", "3级", LocalDateTime(2025, 11, 12, 10, 30), 40),
        CourseItem("王五", "古筝", "6级", LocalDateTime(2025, 11, 18, 16, 0), 40),
        CourseItem("赵六", "吉他", "4级", LocalDateTime(2025, 11, 25, 9, 0), 40)
    )

    // 新增：控制底部弹窗显示/隐藏的状态
    val (showSheet, setShowSheet) = remember { mutableStateOf(false) }

    // 关键：用 Box 容器包裹列表和悬浮按钮
    // Box 会让子元素堆叠显示，便于悬浮按钮覆盖在列表上方
    Box(modifier = Modifier.fillMaxSize().background(bgPrimaryColor)) {
        // 列表（占满屏幕）
        LazyColumn(
//            modifier = Modifier.fillMaxSize().background(bgPrimaryColor),
//            verticalArrangement = Arrangement.spacedBy(8.dp), // Item 上下间距8dp
//            contentPadding = PaddingValues(16.dp) // 列表左右内边距

            modifier = Modifier.fillMaxSize().padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(courseList, key = { it.startMilliSeconds }) { item ->
                SwipeToDeleteItem(
                    onDelete = { /* 执行删除逻辑，如从列表中移除 item */ }
                ) {
                    CustomListItem(
                        item,
                        onButton1Click = { /* 编辑逻辑 */ },
                        onButton2Click = { /* 详情逻辑 */ }
                    )
                }
            }
        }

        // 悬浮按钮（固定在右下角，设置合适大小和边距）
        FloatingActionButton(
            onClick = { setShowSheet(true) }, // 改为显示弹窗
            containerColor = primaryColor,
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd) // 固定在右下角
                .padding(24.dp) // 与屏幕边缘保持距离
                .size(56.dp) // 显式设置大小（默认也是56dp，可按需调整）
        ) {
            Icon(
                painter = painterResource(Res.drawable.add_message_icon),
                contentDescription = "添加信息",
                tint = Color.White,
                modifier = Modifier.size(18.dp) // 图标大小（建议24dp）
            )
        }

        // 条件显示底部弹窗（核心修改）
        if (showSheet) {
            InfoInputBottomSheet(
                navController = navController,
                onDismiss = { setShowSheet(false) }, // 关闭弹窗
                // 新增：接收表单提交的数据（替代原来的 savedStateHandle 传递）
                onSubmit = { title, content, dateTime ->
                    // 处理提交的数据（如更新列表、保存到数据库）
                    // ...
                    setShowSheet(false) // 提交后关闭弹窗
                }
            )
        }
    }
}