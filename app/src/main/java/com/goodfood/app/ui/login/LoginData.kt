package com.goodfood.app.ui.login

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.goodfood.app.models.request_dtos.LoginRequestDTO


/**
 * Created by Lalit N. Hajare (Android Developer) on 25/03/21.
 *
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */
data class LoginData(
    var email: String,
    var password: String
) : BaseObservable() {

    @Bindable
    var loading: Boolean = false
        set(value) {
            field = value
            notifyChange()
        }

    fun getLoginRequestDTO(): LoginRequestDTO {
        return LoginRequestDTO(email, password)
    }

}