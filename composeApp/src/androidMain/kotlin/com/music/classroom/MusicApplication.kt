package com.music.classroom

import android.app.Application
import com.music.classroom.db.AppContainer
import com.music.classroom.db.Factory

/**
 * @author: linsixu@ruqimobility.com
 * @date:2025/10/28
 * 用途：
 */
class MusicApplication: Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        AppUtils.application = this
        container = AppContainer(Factory(this))
    }
}