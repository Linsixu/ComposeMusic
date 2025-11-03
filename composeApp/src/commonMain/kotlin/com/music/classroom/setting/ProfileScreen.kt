import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.music.classroom.SPKeyUtils
import com.music.classroom.SPKeyUtils.DEFAULT_LESSON_DEFAULT_TIME
import com.music.classroom.SPKeyUtils.DEFAULT_MUSIC_TOOLS
import com.music.classroom.SPKeyUtils.DEFAULT_TEACHER_NAME
import com.music.classroom.color.bgPrimaryColor
import com.music.classroom.navi.NavRoutes
import com.music.classroom.storage.spStorage
import musicclassroom.composeapp.generated.resources.Res
import musicclassroom.composeapp.generated.resources.setting_arrow_right
import org.jetbrains.compose.resources.painterResource

// 列表项数据类
data class ProfileItem(
    val title: String,
    val route: String // 跳转路由
)

@Composable
fun ProfileScreen(
    navController: NavController
) {
    LaunchedEffect(Unit) {
        val lessonTime = spStorage.getString(DEFAULT_LESSON_DEFAULT_TIME)
        if (lessonTime.toIntOrNull() != null && lessonTime.toLong() > 0L) {
            SPKeyUtils.currentLessonTime.value = lessonTime.toLong()
        }

        val defaultMusicTools = spStorage.getString(DEFAULT_MUSIC_TOOLS)
        if (defaultMusicTools.isNotBlank()) {
            SPKeyUtils.currentMusicTools.value = defaultMusicTools
        }

        val defaultTeacherName = spStorage.getString(DEFAULT_TEACHER_NAME)
        if (defaultTeacherName.isNotBlank()) {
            SPKeyUtils.currentTeacherName.value = defaultTeacherName
        }
    }
    val defaultLessonTime = remember { SPKeyUtils.currentLessonTime }
    val defaultShowLessonTime = if (defaultLessonTime.value > 0) {
        "课时默认值:（${defaultLessonTime.value}分钟）"
    } else {
        "课时默认值"
    }

    val defaultMusicTools = remember { SPKeyUtils.currentMusicTools }
    // 动态生成“默认乐器”的标题：有值时显示为“默认乐器（XXX）”
    val defaultMusicTitle = if (defaultMusicTools.value.isNotBlank()) {
        "默认乐器（${defaultMusicTools.value}）"
    } else {
        "默认乐器"
    }

    val defaultTeacherName = remember { SPKeyUtils.currentTeacherName }
    // 动态生成“默认乐器”的标题：有值时显示为“默认乐器（XXX）”
    val defaultTeacherNameTitle = if (defaultTeacherName.value.isNotBlank()) {
        "默认教师（${defaultTeacherName.value}）"
    } else {
        "默认教师"
    }
    // 第一组数据：课时默认值、提示功能
    val group1 = listOf(
        ProfileItem(defaultShowLessonTime, route = NavRoutes.ProfileDefault.route),
        ProfileItem(defaultMusicTitle, route = NavRoutes.ProfileMusicTools.route),
        ProfileItem(defaultTeacherNameTitle, route = NavRoutes.ProfileTeacherName.route),
    )

    // 第二组数据：数据传输、excel表格生成
    val group2 = listOf(
        ProfileItem("提示功能", route = NavRoutes.ProfileNotification.route),
        ProfileItem("数据传输", route = NavRoutes.ProfileDataTransfer.route),
        ProfileItem("excel表格生成", route = NavRoutes.ProfileExcelGenerate.route)
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // 列表（占满屏幕）
        Column(
            modifier = Modifier.fillMaxSize().background(bgPrimaryColor)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp), // Item 上下间距8dp
//            contentPadding = PaddingValues(16.dp) // 列表左右内边距
        ) {
            // 标题栏
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "个人中心",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // 第一组卡片（白色背景+圆角+阴影）
            GroupCard(items = group1) { route ->
                navController.navigate(route)
            }

            // 第二组卡片（白色背景+圆角+阴影）
            GroupCard(items = group2) { route ->
                navController.navigate(route)
            }
        }
    }

//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp) // 整体外间距
//            .background(color = bgPrimaryColor), // 页面背景色（灰色）
//        verticalArrangement = Arrangement.spacedBy(16.dp) // 两组卡片之间的间距
//    ) {
//
//    }
}

/**
 * 分组卡片组件：包含一组列表项，带白色背景、圆角和阴影
 * @param items 列表项数据
 * @param onItemClick 点击列表项的回调（传入路由）
 */
@Composable
private fun GroupCard(
    items: List<ProfileItem>,
    onItemClick: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        // 白色背景、8dp圆角、阴影效果
        colors = CardDefaults.cardColors(containerColor = androidx.compose.ui.graphics.Color.White),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp, // 阴影高度
            pressedElevation = 2.dp
        )
    ) {
        Column {
            items.forEachIndexed { index, item ->
                // 列表项
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clickable { onItemClick(item.route) }
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.title,
                        fontSize = 16.sp
                    )
                    Image(
                        painter = painterResource(Res.drawable.setting_arrow_right),
                        contentDescription = "进入详情",
                        modifier = Modifier.size(24.dp)
                    )
                }

                // 分割线（最后一项不加）
                if (index != items.lastIndex) {
                    Divider(
                        modifier = Modifier.padding(horizontal = 16.dp).height(0.5.dp),
                        color = androidx.compose.ui.graphics.Color(0xFFEEEEEE) // 浅灰色分割线
                    )
                }
            }
        }
    }
}