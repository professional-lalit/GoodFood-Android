package com.goodfood.app.utils

import android.util.Patterns


/**
 * Created by Lalit N. Hajare (Android Developer) on 21/03/21.
 *
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */
object Utils {

    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isPasswordValid(password: String): Boolean {
        return password.matches(Regex("[a-zA-Z0-9]+"))
    }

    fun isMobileNumberValid(mobileNumber: String): Boolean {
        return mobileNumber.length == 10
    }

    fun isPasswordLengthValid(password: String): Boolean {
        return password.length >= 6
    }


}