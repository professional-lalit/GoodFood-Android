package com.goodfood.app.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.goodfood.app.ui.common.BaseViewModel


/**
 * Created by Lalit N. Hajare (Android Developer) on 20/03/21.
 *
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */
class SignupViewModel : BaseViewModel() {

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

    fun submit() {
        val validationCode = signupData.isValid()
        _validationMessage.postValue(validationCode)
        if (signupData.isValid() == SignupData.ValidationCode.VALIDATED) {
            TODO("call sign up API")
        }
    }
}