package com.goodfood.app.models.response_dtos

import com.goodfood.app.models.domain.LocalModel


/**
 * Created by Lalit N. Hajare (Android Developer) on 19/03/21.
 *
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */
data class LoginResponseDTO(private val status: Int,
                            private val message: String
): BaseResponseDTO(status, message) {

    override fun getDomainModel(): LocalModel {
        return super.getDomainModel()
    }
}