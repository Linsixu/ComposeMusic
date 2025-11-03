package com.music.classroom

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.music.classroom.color.bgPrimaryColor
import com.music.classroom.navi.MainNavigation
import com.music.classroom.util.StatusBarController
import com.music.classroom.util.isLightColor
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        MainNavigation()
//        Column(
//            modifier = Modifier
//                .background(MaterialTheme.colorScheme.primaryContainer)
//                .safeContentPadding()
//                .fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally,
//        ) {

//            Button(onClick = { showContent = !showContent }) {
//                Text("Hello Magic, Click me!")
//            }
//            AnimatedVisibility(showContent) {
//                val greeting = remember { Greeting().greet() }
//                Column(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                ) {
//                    Image(painterResource(Res.drawable.compose_multiplatform), null)
//                    Text("Compose: $greeting")
//                }
//            }
//        }
    }
}

@Composable
@Preview
fun App(statusBarController: StatusBarController) {
    // 初始化状态栏颜色（页面启动即生效）
    SideEffect {
        // 设置状态栏背景色为 bgPrimaryColor
        statusBarController.setStatusBarColor(bgPrimaryColor)
        // 根据背景色自动判断文字颜色（浅色背景→深色文字，反之→白色文字）
        statusBarController.setStatusBarTextDark(isLightColor(bgPrimaryColor))
    }
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        MainNavigation()
    }
}