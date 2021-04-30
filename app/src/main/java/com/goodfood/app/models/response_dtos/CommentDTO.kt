package com.goodfood.app.models.response_dtos

import com.goodfood.app.models.domain.Comment
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
data class CommentDTO(
    val reactions: List<ReactionDTO>?,
    val _id: String?,
    val imageUrl: String?,
    val recipeId: String?,
    val commentator: UserResponseDTO.UserDTO?,
    val description: String?,
    val rating: Int?,
    val createdAt: String?,
    val updatedAt: String?
) : BaseResponseDTO() {


    override fun getDomainModel(): Comment {

        fun getDomainReactions(list: List<ReactionDTO>?): List<Reaction>? {
            return list?.map { it.getDomainModel() }
        }

        return Comment(
            user = commentator!!.getDomainModel(),
            comment = description ?: "",
            commentedAt = updatedAt ?: "2021-04-30T18:07:58.958Z",
            reactions = getDomainReactions(reactions)
        )
    }

}