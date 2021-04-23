package com.goodfood.app.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goodfood.app.models.response_dtos.LoginResponseDTO
import com.goodfood.app.models.response_dtos.SignupResponseDTO
import com.goodfood.app.networking.NetworkResponse
import com.goodfood.app.repositories.AuthRepository
import com.goodfood.app.ui.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Created by Lalit N. Hajare (Android Developer) on 20/03/21.
 *
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : BaseViewModel() {

    fun navigateToSignUp() {
        _screenToNav.postValue(Navigables.SIGNUP)
    }

    fun navigateToForgotPassword() {
        _screenToNav.postValue(Navigables.FORGOT_PWD)
    }

    fun navigateToHome() {
        _screenToNav.postValue(Navigables.HOME)
    }

    val loginData = LoginData("hajare.lalit@gmail.com", "123Abc")

    private val _loginResponse = MutableLiveData<LoginResponseDTO>()
    val loginResponse: LiveData<LoginResponseDTO> = _loginResponse

    fun login() {
        viewModelScope.launch {
            loginData.loading = true
            val result = authRepository.login(loginData)
            loginData.loading = false
            if (result is NetworkResponse.NetworkSuccess) {
                val data = result.data as LoginResponseDTO
                _loginResponse.postValue(data)
                navigateToHome()
            } else {
                val errorData = (result as NetworkResponse.NetworkError).error
                _errorData.postValue(errorData)
            }
        }
    }

}