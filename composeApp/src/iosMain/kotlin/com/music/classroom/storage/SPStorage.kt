package com.music.classroom.storage

import platform.Foundation.NSUserDefaults

// iOS 平台的SP实现（基于UserDefaults）
actual class SPStorage {
    // 获取UserDefaults实例（默认实例即可）
    private val userDefaults = NSUserDefaults.standardUserDefaults

    actual fun saveString(key: String, value: String) {
        userDefaults.setObject(value, forKey = key)
        userDefaults.synchronize() // 同步到磁盘（可选，iOS 12+ 自动同步，但显式调用更保险）
    }

    actual fun getString(key: String, defaultValue: String): String {
        return userDefaults.stringForKey(key) ?: defaultValue
    }

    actual fun saveBoolean(key: String, value: Boolean) {
        userDefaults.setBool(value, forKey = key)
        userDefaults.synchronize()
    }

    actual fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return userDefaults.boolForKey(key) ?: defaultValue
    }

    actual fun saveInt(key: String, value: Int) {
        userDefaults.setInteger(value.toLong(), forKey = key) // iOS中Int对应Long
        userDefaults.synchronize()
    }

    actual fun getInt(key: String, defaultValue: Int): Int {
        return userDefaults.integerForKey(key).toInt() ?: defaultValue
    }

    actual fun remove(key: String) {
        userDefaults.removeObjectForKey(key)
        userDefaults.synchronize()
    }

    actual fun clear() {
        userDefaults.dictionaryRepresentation().keys.forEach { key ->
            (key as? String)?.let {
                userDefaults.removeObjectForKey(key)
            }
        }
        userDefaults.synchronize()
    }
}

// 全局单例实例
actual val spStorage: SPStorage = SPStorage()