package com.goodfood.app.ui.login

import androidx.lifecycle.ViewModel
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
class LoginViewModel: BaseViewModel() {

    fun navigateToSignUp(){
        _screenToNav.postValue(LoginNav.SIGNUP)
    }

    fun navigateToForgotPassword(){
        _screenToNav.postValue(LoginNav.FORGOT_PWD)
    }

    fun login(){

    }

}