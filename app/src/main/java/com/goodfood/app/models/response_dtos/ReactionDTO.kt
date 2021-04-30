package com.goodfood.app.models.response_dtos

import com.goodfood.app.models.domain.Reaction


/**
 * Created by Lalit N. Hajare (Android Developer) on 30/4/21.
 *
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */
data class ReactionDTO(
    val _id: String?,
    val commentId: String?,
    val reactor: UserResponseDTO.UserDTO?,
    val description: String?,
    val imageUrl: String?,
    val createdAt: String?,
    val updatedAt: String?,
    val reactions: List<ReactionDTO>?
) : BaseResponseDTO() {

    override fun getDomainModel(): Reaction {

        fun getDomainReactions(list: List<ReactionDTO>?): List<Reaction>? {
            return list?.map { it.getDomainModel() }
        }

        return Reaction(
            username = reactor?.firstName + reactor?.lastName,
            reaction = description ?: "",
            reactedAt = updatedAt ?: "",
            reactions = getDomainReactions(reactions)
        )
    }

}