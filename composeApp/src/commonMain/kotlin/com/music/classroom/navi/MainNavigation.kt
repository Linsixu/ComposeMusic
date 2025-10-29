package com.music.classroom.navi

import ProfileScreen
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import com.music.classroom.color.bgPrimaryColor
import com.music.classroom.color.primaryColor
import com.music.classroom.page.HomeScreen
import com.music.classroom.allpage.AllCourse
import com.music.classroom.setting.DataTransferScreen
import com.music.classroom.setting.ExcelGenerateScreen
import com.music.classroom.setting.LessonDefaultScreen
import com.music.classroom.setting.NotificationScreen
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
    // 1. 获取当前路由（监听导航栈顶部页面）
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route

    Scaffold(
        modifier = Modifier.fillMaxSize().background(bgPrimaryColor),
        // 2. 传递当前路由给 BottomNavigationBar，控制 Tab 显示/隐藏
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                currentRoute = currentRoute
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            androidx.navigation.compose.NavHost(
                navController = navController,
                startDestination = NavRoutes.Home.route
            ) {
                composable(NavRoutes.Home.route) { HomeScreen(navController) }
                composable(NavRoutes.Course.route) { AllCourse() }
                composable(NavRoutes.Profile.route) { ProfileScreen(navController) }
                // 设置页面路由（确保与 NavRoutes 一致）
                composable(NavRoutes.ProfileDefault.route) { LessonDefaultScreen(navController) }
                composable(NavRoutes.ProfileNotification.route) { NotificationScreen(navController) }
                composable(NavRoutes.ProfileDataTransfer.route) { DataTransferScreen(navController) }
                composable(NavRoutes.ProfileExcelGenerate.route) { ExcelGenerateScreen(navController) }
            }
        }
    }
}

@Composable
private fun BottomNavigationBar(
    navController: NavController,
    currentRoute: String? // 3. 接收当前路由，用于判断是否隐藏 Tab
) {
    // 4. 定义需要隐藏 Tab 的路由列表（添加 LessonDefaultScreen 的路由）
    val hideTabRoutes = listOf(
        NavRoutes.ProfileDefault.route, // 核心：LessonDefaultScreen 对应的路由
        // 后续其他需要隐藏 Tab 的页面，直接添加路由即可（如通知、数据传输）
        NavRoutes.ProfileNotification.route,
        NavRoutes.ProfileDataTransfer.route,
        NavRoutes.ProfileExcelGenerate.route
    )

    // 5. 条件渲染：若当前路由在隐藏列表中，不显示 Tab 栏
    if (currentRoute in hideTabRoutes) {
        return // 直接返回，不渲染 Tab 栏
    }

    // 以下是原有的 Tab 栏逻辑（仅当不需要隐藏时才执行）
    val items = listOf(
        NavigationItem(
            route = NavRoutes.Home.route,
            selectIconRes = Res.drawable.compose_home_icon,
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

    NavigationBar(
        containerColor = bgPrimaryColor,
        modifier = Modifier.padding(horizontal = 0.dp, vertical = 0.dp)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRouteInner = navBackStackEntry?.destination?.route

        items.forEach { item ->
            val isSelected = currentRouteInner == item.route
            NavigationBarItem(
                icon = {
                    Image(
                        painter = if (isSelected) painterResource(item.selectIconRes)
                        else painterResource(item.unSelectIconRes),
                        contentDescription = item.label,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = { Text(text = item.label) },
                selected = isSelected,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = primaryColor,
                    selectedTextColor = primaryColor,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.Transparent
                ),
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

data class NavigationItem(
    val route: String,
    val selectIconRes: DrawableResource,
    val unSelectIconRes: DrawableResource,
    val label: String
)