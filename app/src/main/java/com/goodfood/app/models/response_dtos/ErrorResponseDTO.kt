package com.goodfood.app.models.response_dtos

import com.goodfood.app.models.DomainMapper
import com.goodfood.app.models.domain.Error
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
data class ErrorResponseDTO(
    private val data: List<ErrorDetailDTO>?,
    private val message: String? = "",
) : DomainMapper {

    override fun getDomainModel(): Error {
        val message = if (this.data?.isNotEmpty() == true) {
            this.data[0].msg
        } else {
            this.message
        }
        return Error(message ?: "Something went wrong")
    }

    data class ErrorDetailDTO(
        val location: String,
        val param: String,
        val value: String,
        val msg: String
    )
}