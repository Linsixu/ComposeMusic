package com.music.classroom

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform