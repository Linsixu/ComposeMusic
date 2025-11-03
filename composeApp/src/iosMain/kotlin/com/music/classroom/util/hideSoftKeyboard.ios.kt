package com.music.classroom.util

import platform.UIKit.UIApplication
import platform.UIKit.endEditing

/**
 * @author: linsixu@ruqimobility.com
 * @date:2025/11/3
 * 用途：
 */
actual fun hideSoftKeyboard() {
    UIApplication.sharedApplication().keyWindow?.endEditing(true)
}