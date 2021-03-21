package com.goodfood.app.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.goodfood.app.models.domain.ServerMessage
import com.goodfood.app.networking.NetworkInterface
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
class SignupViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : BaseViewModel() {

    fun navigateToProfilePicSelection() {
        _screenToNav.postValue(SignupNav.PROFILE_PIC)
    }

    fun navigateToLogin() {
        _screenToNav.postValue(SignupNav.LOGIN)
    }

    val signupData = SignupData(
        "", "", "",
        "", "", "",
    )

    private val _validationMessage = MutableLiveData<SignupData.ValidationCode>()
    val validationMessage: LiveData<SignupData.ValidationCode> = _validationMessage

    private val _serverMessage = MutableLiveData<ServerMessage>()
    val serverMessage: LiveData<ServerMessage> = _serverMessage

    fun submit() {
        val validationCode = signupData.isValid()
        _validationMessage.postValue(validationCode)
        if (signupData.isValid() == SignupData.ValidationCode.VALIDATED) {
            viewModelScope.launch {
                val result = authRepository.signup(signupData)
                if (result is NetworkResponse.NetworkSuccess) {
                    val data = result.data as ServerMessage
                    _serverMessage.postValue(data)
                } else {
                    val errorData = (result as NetworkResponse.NetworkError).error
                    _errorData.postValue(errorData)
                }
            }
        }
    }
}