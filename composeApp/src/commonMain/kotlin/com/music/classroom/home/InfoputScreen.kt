//package com.music.classroom.home
//
////import androidx.compose.material.icons.Icons
////import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material3.Button
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBar
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//import musicclassroom.composeapp.generated.resources.Res
//import musicclassroom.composeapp.generated.resources.today_class_back
//import org.jetbrains.compose.resources.painterResource
//
///**
// * @author: linsixu@ruqimobility.com
// * @date:2025/10/20
// * 用途：
// */
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun InfoInputScreen(navController: NavController) {
//    // 输入框状态
//    var title by remember { mutableStateOf("") }
//    var content by remember { mutableStateOf("") }
//
//    // 返回按钮点击事件
//    fun onBackClick() {
//        navController.popBackStack()
//    }
//
//    // 提交按钮点击事件（返回首页并携带数据）
//    fun onSubmitClick() {
//        if (title.isNotBlank() && content.isNotBlank()) {
//            // 这里可以通过ViewModel保存数据，或直接通过导航返回时传递
//            // 实际项目中建议使用ViewModel+状态管理
//            navController.popBackStack() // 返回首页
//        }
//    }
//
//    Column(
//        modifier = Modifier.fillMaxSize()
//    ) {
//        // 顶部导航栏（带返回按钮）
//        TopAppBar(
//            title = { Text(text = "填写信息") },
//            navigationIcon = {
//                IconButton(onClick = ::onBackClick) {
//                    Image(painterResource(Res.drawable.today_class_back), null)
//                }
//            }
//        )
//
//        // 输入表单
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp),
//            verticalArrangement = Arrangement.Top
//        ) {
//            // 标题输入框
//            OutlinedTextField(
//                value = title,
//                onValueChange = { title = it },
//                label = { Text("请输入标题") },
//                modifier = Modifier.fillMaxWidth(),
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
//            )
//
//            // 内容输入框（距离标题有间距）
//            OutlinedTextField(
//                value = content,
//                onValueChange = { content = it },
//                label = { Text("请输入内容") },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 16.dp),
//                maxLines = 5, // 多行输入
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
//            )
//
//            // 提交按钮（距离内容有间距）
//            Button(
//                onClick = ::onSubmitClick,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 24.dp),
//                enabled = title.isNotBlank() && content.isNotBlank() // 输入不为空才可用
//            ) {
//                Text(text = "提交")
//            }
//        }
//    }
//}