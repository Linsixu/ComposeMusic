package com.music.classroom

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.music.classroom.color.bgPrimaryColor
import com.music.classroom.navi.MainNavigation
import com.music.classroom.util.StatusBarController
import com.music.classroom.util.isLightColor
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import musicclassroom.composeapp.generated.resources.Res
import musicclassroom.composeapp.generated.resources.compose_multiplatform

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