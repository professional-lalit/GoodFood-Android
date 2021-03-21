package com.goodfood.app.networking

import com.goodfood.app.models.request_dtos.LoginRequestDTO
import com.goodfood.app.models.request_dtos.SignupRequestDTO
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*


/**
 * Created by Lalit N. Hajare (Android Developer) on 21/03/21.
 *
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */
interface ServerInterface {

    @POST("auth/signup")
    suspend fun signup(@Body requestDTO: SignupRequestDTO): Response<Any?>

    @POST("auth/login")
    suspend fun login(@Body requestDTO: LoginRequestDTO): Response<Any?>

    @POST("auth/forgot-password")
    suspend fun forgotPassword(@Body requestDTO: SignupRequestDTO): Response<Any?>

    @POST("auth/change-password")
    suspend fun changePassword(@Body requestDTO: SignupRequestDTO): Response<Any?>

    @Multipart
    @POST("user/imageUpload")
    suspend fun uploadUserImage(
        @Query("userId") userId: String,
        @Part profileImage: MultipartBody.Part
    ): Response<Any?>

}