package com.music.classroom.status

/**
 * @author: linsixu@ruqimobility.com
 * @date:2026/3/14
 * 用途：
 */
sealed class LoginStatus
class UnknowLoginStatus: LoginStatus()

class SuccessLoginStatus(): LoginStatus()

class FailureLoginStatus(): LoginStatus()