package com.music.classroom

import android.app.Application

/**
 * @author: linsixu@ruqimobility.com
 * @date:2025/10/28
 * 用途：
 */
class MusicApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        AppUtils.application = this
    }
}