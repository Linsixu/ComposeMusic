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
    object InfoInput: NavRoutes("infoInput")
}