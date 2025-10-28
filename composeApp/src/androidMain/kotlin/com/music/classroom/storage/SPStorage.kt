package com.music.classroom.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.music.classroom.AppUtils

// Android 平台的SP实现（基于SharedPreferences）
actual class SPStorage {
    companion object {
        const val MUSIC_FILE_NAME = "music_class"
    }
    private val sp: SharedPreferences = AppUtils.application.getSharedPreferences(
        MUSIC_FILE_NAME, Context.MODE_PRIVATE
    )

    actual fun saveString(key: String, value: String) {
        sp.edit { putString(key, value) }
    }

    actual fun getString(key: String, defaultValue: String): String {
        return sp.getString(key, defaultValue) ?: defaultValue
    }

    actual fun saveBoolean(key: String, value: Boolean) {
        sp.edit { putBoolean(key, value) }
    }

    actual fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return sp.getBoolean(key, defaultValue)
    }

    actual fun saveInt(key: String, value: Int) {
        sp.edit { putInt(key, value) }
    }

    actual fun getInt(key: String, defaultValue: Int): Int {
        return sp.getInt(key, defaultValue)
    }

    actual fun remove(key: String) {
        sp.edit { remove(key) }
    }

    actual fun clear() {
        sp.edit { clear() }
    }
}

// 全局单例实例
actual val spStorage: SPStorage = SPStorage()