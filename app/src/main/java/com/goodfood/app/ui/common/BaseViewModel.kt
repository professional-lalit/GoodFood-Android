package com.goodfood.app.ui.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.goodfood.app.interfaces.Navigable
import com.goodfood.app.models.domain.Error
import dagger.hilt.android.lifecycle.HiltViewModel
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

open class BaseViewModel : ViewModel() {

    protected val _screenToNav = MutableLiveData<Navigable>()
    val screenToNav: LiveData<Navigable> = _screenToNav

    protected val _errorData = MutableLiveData<Error>()
    val errorData: LiveData<Error> = _errorData

    enum class Navigables : Navigable {
        FORGOT_PWD, SIGNUP, TERMS_AND_COND, PRIVACY, HOME, LOGIN
    }

    enum class LoginNav : Navigable {
        FORGOT_PWD, SIGNUP, TERMS_AND_COND, PRIVACY, HOME
    }

    enum class SignupNav : Navigable {
        LOGIN, TERMS_AND_COND, PRIVACY, PROFILE_PIC, HOME
    }

}