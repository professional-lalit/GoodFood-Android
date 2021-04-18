package com.goodfood.app.models.response_dtos

import com.goodfood.app.models.DomainMapper
import com.goodfood.app.models.domain.User


/**
 * Created by Lalit N. Hajare (Android Developer) on 27/03/21.
 *
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */
data class UserResponseDTO(private val user: UserDTO? = null) : BaseResponseDTO() {

    data class UserDTO(
        val _id: String? = null,
        val email: String? = null,
        val firstName: String? = null,
        val lastName: String? = null,
        val bio: String? = null,
        val createdAt: String? = null,
        val updatedAt: String? = null,
        val address: String? = null,
        val imageUrl: String? = null,
        val videoUrl: String? = null
    ): BaseResponseDTO() {

        override fun getDomainModel(): User {
            return User(
                userId = _id ?: "",
                firstName = firstName ?: "",
                lastName = lastName ?: "",
                imageUrl = imageUrl ?: "",
                email = email ?: "",
                bio = bio ?: "",
                videoUrl = videoUrl ?: ""
            )
        }
    }



    override fun getDomainModel(): User {
        return User(
            userId = user?._id ?: "",
            firstName = user?.firstName ?: "",
            lastName = user?.lastName ?: "",
            imageUrl = user?.imageUrl ?: "",
            email = user?.email ?: "",
            bio = user?.bio ?: "",
            videoUrl = user?.videoUrl ?: ""
        )
    }
}