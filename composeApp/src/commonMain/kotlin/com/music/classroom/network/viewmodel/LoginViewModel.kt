package com.music.classroom.network.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.music.classroom.SPKeyUtils.TEACHER_ID_VALUE
import com.music.classroom.SPKeyUtils.TEACHER_NAME
import com.music.classroom.SPKeyUtils.TEACHER_PHONE
import com.music.classroom.network.api.ILoginApi
import com.music.classroom.status.FailureLoginStatus
import com.music.classroom.status.LoginStatus
import com.music.classroom.status.SuccessLoginStatus
import com.music.classroom.status.UnknowLoginStatus
import com.music.classroom.storage.spStorage
import com.music.classroom.util.showToast
import io.ktor.util.logging.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * @author: linsixu@ruqimobility.com
 * @date:2026/3/13
 * 用途：
 */
class LoginViewModel: ViewModel() {
    val loginApi = ILoginApi()

    private val mLoginState = MutableStateFlow<LoginStatus>(UnknowLoginStatus())

    val loginState = mLoginState.asStateFlow()

    private val mLoading = MutableStateFlow<Boolean>(false)

    val loadingState = mLoading.asStateFlow()

    fun loginByTeacher(
        account: String,
        password: String
    ) {
        viewModelScope.launch {
            mLoading.value = true
            try {
                val result = loginApi.requestTeacherLogin(account, password)
                if (result != null) {
                    mLoginState.value = SuccessLoginStatus()
                    spStorage.saveString(TEACHER_ID_VALUE, result.teacherId.toString())
                    spStorage.saveString(TEACHER_NAME, result.teacherName.toString())
                    spStorage.saveString(TEACHER_PHONE, result.teacherPhone.toString())
                } else {
                    mLoginState.value = FailureLoginStatus()
                }
                mLoading.value = false
            } catch (e: Exception) {
                mLoading.value = false
                mLoginState.value = FailureLoginStatus()
            }
        }
    }

    fun loginByStudent(
        account: String,
        password: String
    ) {
        viewModelScope.launch {
            mLoading.value = true
            try {
                val result = loginApi.requestStudentLogin(account, password)
                if (result) {
                    mLoginState.value = SuccessLoginStatus()
                } else {
                    mLoginState.value = FailureLoginStatus()
                }
                mLoading.value = false
            } catch (e: Exception) {
                mLoginState.value = FailureLoginStatus()
                mLoading.value = false
            }
        }
    }
}