package com.goodfood.app.models.request_dtos


/**
 * Created by Lalit N. Hajare (Android Developer) on 21/03/21.
 *
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */
data class SignupRequestDTO(
    private val firstName: String,
    private val lastName: String,
    private val mobileNumber: String,
    private val email: String,
    private val password: String,
    private val confirmPassword: String,
    private val imageUrl: String
)