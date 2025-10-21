package com.music.classroom.navi

/**
 * @author: linsixu@ruqimobility.com
 * @date:2025/10/17
 * 用途：
 */
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.music.classroom.color.primaryColor
import com.music.classroom.home.InfoInputScreen
import com.music.classroom.page.HomeScreen
import com.music.classroom.page.ProfileScreen
import com.music.classroom.page.OldCourse
import musicclassroom.composeapp.generated.resources.Res
import musicclassroom.composeapp.generated.resources.compose_course_icon
import musicclassroom.composeapp.generated.resources.compose_home_icon
import musicclassroom.composeapp.generated.resources.compose_my_self_icon
import musicclassroom.composeapp.generated.resources.unselect_compose_course_icon
import musicclassroom.composeapp.generated.resources.unselect_compose_home_icon
import musicclassroom.composeapp.generated.resources.unselect_compose_my_self_icon
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun MainNavigation() {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomNavigationBar(navController = navController) } // 底部导航栏
    ) {innerPadding ->
        // 内容区域（通过innerPadding避免被底部导航栏遮挡）
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding), // 应用内边距，防止内容被底部栏遮挡
            color = MaterialTheme.colorScheme.background
        ) {
            androidx.navigation.compose.NavHost(
                navController = navController,
                startDestination = NavRoutes.Home.route
            ) {
                composable(NavRoutes.Home.route) { HomeScreen(navController) }
                composable(NavRoutes.Course.route) { OldCourse() }
                composable(NavRoutes.Profile.route) { ProfileScreen() }
            }
        }
    }
//    Surface(
//        modifier = Modifier.fillMaxSize(),
//        color = MaterialTheme.colorScheme.background
//    ) {
//        androidx.navigation.compose.NavHost(
//            navController = navController,
//            startDestination = NavRoutes.Home.route
//        ) {
//            composable(NavRoutes.Home.route) { HomeScreen() }
//            composable(NavRoutes.Search.route) { SearchScreen() }
//            composable(NavRoutes.Profile.route) { ProfileScreen() }
//        }
//
//        BottomNavigationBar(navController = navController)
//    }
}

@Composable
private fun BottomNavigationBar(navController: NavController) {
    // 定义底部导航项（使用本地图片资源）
    val items = listOf(
        NavigationItem(
            route = NavRoutes.Home.route,
            selectIconRes = Res.drawable.compose_home_icon, // 本地图片资源
            unSelectIconRes = Res.drawable.unselect_compose_home_icon,
            label = "今日课程"
        ),
        NavigationItem(
            route = NavRoutes.Course.route,
            selectIconRes = Res.drawable.compose_course_icon,
            unSelectIconRes = Res.drawable.unselect_compose_course_icon,
            label = "全部课程"
        ),
        NavigationItem(
            route = NavRoutes.Profile.route,
            selectIconRes = Res.drawable.compose_my_self_icon,
            unSelectIconRes = Res.drawable.unselect_compose_my_self_icon,
            label = "我的"
        )
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            val isSelected = currentRoute == item.route
            NavigationBarItem(
                icon = {
                    if (isSelected) {
                        Image(
                            painter = painterResource(item.selectIconRes), // 加载本地图片
                            contentDescription = item.label,
                            modifier = Modifier.size(24.dp) // 图标大小
                        )
                    } else {
                        Image(
                            painter = painterResource(item.unSelectIconRes), // 加载本地图片
                            contentDescription = item.label,
                            modifier = Modifier.size(24.dp) // 图标大小
                        )
                    }
                },
                label = { Text(text = item.label) },
                selected = isSelected,
                // 选中/未选中状态的颜色区分
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = primaryColor, // 选中时图标颜色
                    selectedTextColor = primaryColor, // 选中时文字颜色
                    unselectedIconColor = Color.Gray, // 未选中时图标颜色
                    unselectedTextColor = Color.Gray, // 未选中时文字颜色
                    indicatorColor = Color.Transparent // 选中时背景指示器颜色（可选）
                ),
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true // 避免重复创建页面
                        restoreState = true // 恢复状态
                    }
                }
            )
        }
    }
}

// 导航项数据类（存储资源ID）
data class NavigationItem(
    val route: String,
    val selectIconRes: DrawableResource, // 资源ID（对应MR.drawable中的定义）
    val unSelectIconRes: DrawableResource, // 资源ID（对应MR.drawable中的定义）
    val label: String
)