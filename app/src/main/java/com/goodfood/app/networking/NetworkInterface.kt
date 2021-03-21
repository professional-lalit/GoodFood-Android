package com.goodfood.app.networking

import com.goodfood.app.models.request_dtos.LoginRequestDTO
import com.goodfood.app.models.request_dtos.SignupRequestDTO
import com.goodfood.app.models.response_dtos.LoginResponseDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


/**
 * Created by Lalit N. Hajare (Android Developer) on 21/03/21.
 *
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */
interface NetworkInterface {

    @POST("auth/signup")
    suspend fun signup(@Body requestDTO: SignupRequestDTO): Response<Any?>

    @POST("auth/login")
    suspend fun login(@Body requestDTO: LoginRequestDTO): Response<Any?>

    @POST("auth/forgot-password")
    suspend fun forgotPassword(@Body requestDTO: SignupRequestDTO): Response<Any?>

    @POST("auth/change-password")
    suspend fun changePassword(@Body requestDTO: SignupRequestDTO): Response<Any?>

}