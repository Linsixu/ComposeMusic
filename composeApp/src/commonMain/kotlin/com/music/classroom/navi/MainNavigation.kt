package com.music.classroom.navi

import ProfileScreen
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.music.classroom.SPKeyUtils.DEFAULT_LESSON_DEFAULT_TIME
import com.music.classroom.SPKeyUtils.TEACHER_ID_VALUE
import com.music.classroom.SPKeyUtils.TEACHER_NAME
import com.music.classroom.SPKeyUtils.TEACHER_PHONE
import com.music.classroom.allpage.AllCourse
import com.music.classroom.appConfig.appLogo
import com.music.classroom.appConfig.appName
import com.music.classroom.appConfig.isStudentApp
import com.music.classroom.color.bgPrimaryColor
import com.music.classroom.color.primaryColor
import com.music.classroom.network.viewmodel.LoginViewModel
import com.music.classroom.page.HomeScreen
import com.music.classroom.setting.DataTransferScreen
import com.music.classroom.setting.DefaultTeacherNameScreen
import com.music.classroom.setting.ExcelGenerateScreen
import com.music.classroom.setting.LessonDefaultScreen
import com.music.classroom.setting.MusicToolsScreen
import com.music.classroom.setting.NotificationScreen
import com.music.classroom.status.FailureLoginStatus
import com.music.classroom.status.SuccessLoginStatus
import com.music.classroom.status.UnknowLoginStatus
import com.music.classroom.storage.spStorage
import com.music.classroom.util.showToast
import musicclassroom.composeapp.generated.resources.Res
import musicclassroom.composeapp.generated.resources.compose_course_icon
import musicclassroom.composeapp.generated.resources.compose_home_icon
import musicclassroom.composeapp.generated.resources.compose_my_self_icon
import musicclassroom.composeapp.generated.resources.select_arrow_down_gray_icon
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

    // 添加登录状态管理
    var isLoggedIn by remember { mutableStateOf(false) }

    if (!isLoggedIn) {
        LoginScreen(
            onLoginSuccess = { isLoggedIn = true },
            navController = navController
        )
    } else {
        MainAppContent(
            navController = navController,
            currentRoute = currentRoute
        )
    }
}

@Composable
fun MainAppContent(
    navController: NavHostController,
    currentRoute: String?
) {
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
            NavHost(
                navController = navController,
                startDestination = NavRoutes.Home.route
            ) {
                composable(NavRoutes.Home.route) { HomeScreen(navController) }
                composable(NavRoutes.Course.route) { AllCourse() }
                composable(NavRoutes.Profile.route) { ProfileScreen(navController) }
                // 设置页面路由（确保与 NavRoutes 一致）
                composable(NavRoutes.ProfileDefault.route) { LessonDefaultScreen(navController) }
                composable(NavRoutes.ProfileMusicTools.route) { MusicToolsScreen(navController) }
                composable(NavRoutes.ProfileTeacherName.route) { DefaultTeacherNameScreen(navController) }
                composable(NavRoutes.ProfileNotification.route) { NotificationScreen(navController) }
                composable(NavRoutes.ProfileDataTransfer.route) { DataTransferScreen(navController) }
                composable(NavRoutes.ProfileExcelGenerate.route) { ExcelGenerateScreen(navController) }
            }
        }
    }
}

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    navController: NavController,
    loginViewModel: LoginViewModel = viewModel()
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
//    var isLoading by remember { mutableStateOf(false) }

    val isLoading by loginViewModel.loadingState.collectAsStateWithLifecycle()

    val requestResult by loginViewModel.loginState.collectAsStateWithLifecycle()

//    val requestResultV2 by remember(requestResult) {
//        derivedStateOf { requestResult }
//    }
    var showError by remember { mutableStateOf(false) }

    // 定义浅色系主题色
    val mainBrandColor = primaryColor // 更有活力的蓝色
    val backgroundColor = bgPrimaryColor // 你要求的背景色

    when(requestResult) {
        is UnknowLoginStatus -> {

        }
        is SuccessLoginStatus -> {
            onLoginSuccess()
            showToast("登陆成功")
        }
        is FailureLoginStatus -> {
            showToast("登陆失败，请检查登陆信息")
        }
    }

    LaunchedEffect(Unit) {
        val teacherId = spStorage.getString(TEACHER_ID_VALUE)
        val teacherName = spStorage.getString(TEACHER_NAME)
        val teacherPhone = spStorage.getString(TEACHER_PHONE)
        if (!teacherId.isNullOrBlank() && !teacherName.isNullOrBlank() && !teacherPhone.isNullOrBlank()) {
            onLoginSuccess()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {

        // 背景装饰：浅色背景下用淡淡的彩色光晕增加灵动感
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(mainBrandColor.copy(alpha = 0.08f), Color.Transparent),
                    center = Offset(size.width * 0.8f, size.height * 0.1f),
                    radius = 1000f
                )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. 顶部 Logo 部分
            Surface(
                modifier = Modifier.size(80.dp),
                shape = RoundedCornerShape(24.dp),
                color = mainBrandColor.copy(alpha = 0.1f)
            ) {
                Image(
                    painter = painterResource(appLogo),
                    contentDescription = "logo",
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = appName,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1C1E)
                )
            )

            Text(
                text = "开启你的艺术之旅",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.Gray,
                    letterSpacing = 1.sp
                )
            )

            Spacer(modifier = Modifier.height(40.dp))

            // 2. 登录输入区域（改用带轻微阴影的卡片感）
            ModernTextField(
                value = username,
                onValueChange = { username = it; showError = false },
                label = "姓名",
                icon = painterResource(Res.drawable.select_arrow_down_gray_icon)
            )

            Spacer(modifier = Modifier.height(16.dp))

            ModernTextField(
                value = password,
                onValueChange = { password = it; showError = false },
                label = "手机号",
                icon = painterResource(Res.drawable.select_arrow_down_gray_icon),
                isPassword = true
            )

            if (showError) {
                Text(
                    text = "请输入完整的账号和密码",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.align(Alignment.Start).padding(start = 4.dp, top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 3. 登录按钮（高亮品牌色）
            Button(
                onClick = {
                    if (username.isNotBlank() && password.isNotBlank()) {
                        if (isStudentApp) {
                            loginViewModel.loginByStudent(username, password)
                        } else {
                            loginViewModel.loginByTeacher(username, password)
                        }
                    } else {
                        showError = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = mainBrandColor,
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Text("登录", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

//            // 4. 底部辅助功能
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                TextButton(onClick = {}) {
//                    Text("忘记密码？", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
//                }
//                TextButton(onClick = {}) {
//                    Text("新用户注册", color = mainBrandColor, style = MaterialTheme.typography.bodySmall)
//                }
//            }
        }
    }
}

@Composable
fun ModernTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: Painter,
    isPassword: Boolean = false
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(label, color = Color.LightGray) },
        leadingIcon = {icon },
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                shadowElevation = 2.dp.toPx()
                shape = RoundedCornerShape(12.dp)
                clip = true
            },
        singleLine = true,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = if (isPassword) KeyboardOptions(keyboardType = KeyboardType.Password) else KeyboardOptions.Default,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedTextColor = Color(0xFF1A1C1E),
            unfocusedTextColor = Color(0xFF1A1C1E)
        ),
        shape = RoundedCornerShape(12.dp)
    )
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
        NavRoutes.ProfileMusicTools.route,
        NavRoutes.ProfileTeacherName.route,
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

// 登录相关的路由
object LoginNavRoutes {
    const val Login = "login"
}