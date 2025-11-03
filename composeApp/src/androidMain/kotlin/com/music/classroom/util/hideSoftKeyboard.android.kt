package com.music.classroom.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.music.classroom.AppUtils

/**
 * @author: linsixu@ruqimobility.com
 * @date:2025/11/3
 * 用途：
 */
actual fun hideSoftKeyboard() {
    val context = AppUtils.application.baseContext
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val view = (context as? android.app.Activity)?.currentFocus ?: View(context)
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}