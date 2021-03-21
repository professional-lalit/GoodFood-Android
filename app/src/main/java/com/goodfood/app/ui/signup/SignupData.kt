package com.goodfood.app.ui.signup


import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.Observable
import com.goodfood.app.models.request_dtos.SignupRequestDTO
import com.goodfood.app.utils.Utils


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
 * Represents all the data on signup screen
 */

data class SignupData(
    var firstName: String,
    var lastName: String,
    var mobileNumber: String,
    var email: String,
    var password: String,
    var confirmPassword: String
) : BaseObservable() {

    @Bindable
    var loading: Boolean = false
        set(value) {
            field = value
            notifyChange()
        }

    enum class ValidationCode {
        FIRST_NAME_EMPTY, LAST_NAME_EMPTY, MOBILE_NUMBER_EMPTY,
        MOBILE_NUMBER_INVALID, EMAIL_EMPTY, PASSWORD_EMPTY,
        CONFIRM_PASSWORD_EMPTY, EMAIL_INVALID, PASSWORD_INVALID,
        PASSWORD_LENGTH_INVALID, PASSWORDS_NOT_MATCHING, VALIDATED
    }

    fun getSignupRequestDTO(): SignupRequestDTO {
        return SignupRequestDTO(firstName, lastName, mobileNumber, email, password, confirmPassword)
    }

    fun isValid(): ValidationCode {
        if (firstName.isEmpty()) {
            return ValidationCode.FIRST_NAME_EMPTY
        } else if (lastName.isEmpty()) {
            return ValidationCode.LAST_NAME_EMPTY
        } else if (mobileNumber.isEmpty()) {
            return ValidationCode.MOBILE_NUMBER_EMPTY
        } else if (!Utils.isMobileNumberValid(mobileNumber)) {
            return ValidationCode.MOBILE_NUMBER_INVALID
        } else if (email.isEmpty()) {
            return ValidationCode.EMAIL_EMPTY
        } else if (!Utils.isEmailValid(email)) {
            return ValidationCode.EMAIL_INVALID
        } else if (password.isEmpty()) {
            return ValidationCode.PASSWORD_EMPTY
        } else if (!Utils.isPasswordValid(password)) {
            return ValidationCode.PASSWORD_INVALID
        } else if (!Utils.isPasswordLengthValid(password)) {
            return ValidationCode.PASSWORD_LENGTH_INVALID
        } else if (confirmPassword.isEmpty()) {
            return ValidationCode.CONFIRM_PASSWORD_EMPTY
        } else if (password != confirmPassword) {
            return ValidationCode.PASSWORDS_NOT_MATCHING
        } else {
            return ValidationCode.VALIDATED
        }
    }

}