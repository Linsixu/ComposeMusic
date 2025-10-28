package com.music.classroom.navi

/**
 * @author: linsixu@ruqimobility.com
 * @date:2025/10/17
 * 用途：
 */
sealed class NavRoutes(val route: String) {
    object Home : NavRoutes("home")
    object Course : NavRoutes("class")
    object Profile : NavRoutes("setting")

    //设置页面
    object ProfileDefault: NavRoutes("setting_lessonDefault") //课时默认值
    object ProfileNotification: NavRoutes("setting_notification") //提示功能
    object ProfileDataTransfer: NavRoutes("setting_dataTransfer")//数据传输
    object ProfileExcelGenerate: NavRoutes("setting_excel_generate")//excel表生成
    //
}