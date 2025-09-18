package com.starter.app.ui.auth.viewmodel

import com.starter.app.data.pojo.User
import com.starter.app.data.pojo.request.LoginRequest
import com.starter.app.data.repository.UserRepository
import com.starter.app.ui.base.APILiveData
import com.starter.app.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val userRepository: UserRepository) :
    BaseViewModel() {

    val loginLiveData = APILiveData<User>()

    fun login(request: LoginRequest) = launch {
        val result = userRepository.login(request)
        loginLiveData.value = result
    }
}