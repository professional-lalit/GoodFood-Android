package com.goodfood.app.repositories

import com.goodfood.app.common.Prefs
import com.goodfood.app.models.response_dtos.ErrorResponseDTO
import com.goodfood.app.models.response_dtos.LoginResponseDTO
import com.goodfood.app.models.response_dtos.SignupResponseDTO
import com.goodfood.app.networking.ServerInterface
import com.goodfood.app.networking.NetworkResponse
import com.goodfood.app.ui.login.LoginData
import com.goodfood.app.ui.signup.SignupData
import com.goodfood.app.utils.Utils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject


/**
 * Created by Lalit N. Hajare (Android Developer) on 21/03/21.
 *
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */

/**
 * Has all the APIs & operations necessary to on board the user into app.
 * Sign up, Login, Forgot Password, Change Password - APIs & their respective processing
 */
class AuthRepository @Inject constructor(
    private val serverInterface: ServerInterface,
    private val prefs: Prefs
) {

    suspend fun signUp(signUpData: SignupData): NetworkResponse {
        val requestDTO = signUpData.getSignupRequestDTO()
        val response = serverInterface.signup(requestDTO)
        return if (response.code() in 200..210) {
            val signupResponseDTO = Gson().fromJson(
                Gson().toJson(response.body()),
                SignupResponseDTO::class.java
            )
            val model = signupResponseDTO.getDomainModel()
            model.status = response.code()
            prefs.accessToken = signupResponseDTO.token
            NetworkResponse.NetworkSuccess(model)
        } else {
            NetworkResponse.NetworkError(Utils.parseError(response))
        }
    }

    suspend fun login(loginData: LoginData): NetworkResponse {
        val requestDTO = loginData.getLoginRequestDTO()
        val response = serverInterface.login(requestDTO)
        return if (response.code() in 200..210) {
            val loginResponseDTO = Gson().fromJson(
                Gson().toJson(response.body()),
                LoginResponseDTO::class.java
            )
            val model = loginResponseDTO.getDomainModel()
            model.status = response.code()
            prefs.accessToken = loginResponseDTO.token
            NetworkResponse.NetworkSuccess(model)
        } else {
            NetworkResponse.NetworkError(Utils.parseError(response))
        }
    }

}