package com.goodfood.app.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.goodfood.app.models.domain.ServerMessage
import com.goodfood.app.models.response_dtos.SignupResponseDTO
import com.goodfood.app.networking.NetworkResponse
import com.goodfood.app.repositories.AuthRepository
import com.goodfood.app.repositories.UserRepository
import com.goodfood.app.ui.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
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
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : BaseViewModel() {

    fun navigateToProfilePicSelection() {
        _screenToNav.postValue(Navigables.PROFILE_PIC)
    }

    fun navigateToLogin() {
        _screenToNav.postValue(Navigables.LOGIN)
    }

    private fun navigateToHome() {
        _screenToNav.postValue(Navigables.HOME)
    }

    val signupData = SignupData(
        "Lalit", "Hajare", "9876543210",
        "hajare.lalit1@gmail.com", "123Abc", "123Abc",
    )

    private var profilePicFileToUpload: File? = null

    private val _validationMessage = MutableLiveData<SignupData.ValidationCode>()
    val validationMessage: LiveData<SignupData.ValidationCode> = _validationMessage

    private val _signUpResponse = MutableLiveData<SignupResponseDTO>()
    val serverMessage: LiveData<SignupResponseDTO> = _signUpResponse

    private val _imageUploadResponse = MutableLiveData<ServerMessage>()
    val imageUploadResponse: LiveData<ServerMessage> = _imageUploadResponse


    fun setFileToUpload(file: File) {
        profilePicFileToUpload = file
    }

    fun submit() {
        val validationCode = signupData.isValid()
        if (validationCode != SignupData.ValidationCode.VALIDATED)
            _validationMessage.postValue(validationCode)
        if (signupData.isValid() == SignupData.ValidationCode.VALIDATED) {
            viewModelScope.launch {

                //Submit User Data
                val userId = uploadUserData()

                //Upload Image
                userId?.let {
                    profilePicFileToUpload?.let {
                        uploadUserImage(userId)
                    }
                }
            }
        }
    }

    private suspend fun uploadUserData(): String? {
        val userId: String?
        signupData.loading = true
        val result = authRepository.signUp(signupData)
        signupData.loading = false
        if (result is NetworkResponse.NetworkSuccess) {
            val data = result.data as SignupResponseDTO
            userId = data.userId ?: ""
            _signUpResponse.postValue(data)
        } else {
            userId = null
            val errorData = (result as NetworkResponse.NetworkError).error
            _errorData.postValue(errorData)
        }
        return userId
    }

    private suspend fun uploadUserImage(userId: String) {
        profilePicFileToUpload?.let {
            signupData.loading = true
            val imageResponse =
                userRepository.uploadUserImage(userId, profilePicFileToUpload!!)
            signupData.loading = false
            if (imageResponse is NetworkResponse.NetworkSuccess) {
                val data = imageResponse.data as ServerMessage
                _imageUploadResponse.postValue(data)
                navigateToHome()
            } else {
                val errorData = (imageResponse as NetworkResponse.NetworkError).error
                _errorData.postValue(errorData)
            }
        }
    }
}