package com.music.classroom.storage

/**
 * @author: linsixu@ruqimobility.com
 * @date:2025/10/28
 * 用途：
 */
// 预期接口：定义SP存储的基本操作
expect class SPStorage {
    // 保存字符串
    fun saveString(key: String, value: String)
    // 获取字符串（默认值为""）
    fun getString(key: String, defaultValue: String = ""): String
    // 保存布尔值
    fun saveBoolean(key: String, value: Boolean)
    // 获取布尔值（默认值为false）
    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean
    // 保存整数
    fun saveInt(key: String, value: Int)
    // 获取整数（默认值为0）
    fun getInt(key: String, defaultValue: Int = 0): Int
    // 删除某个键值对
    fun remove(key: String)
    // 清空所有数据
    fun clear()
}

// 提供全局访问实例（单例）
expect val spStorage: SPStorage