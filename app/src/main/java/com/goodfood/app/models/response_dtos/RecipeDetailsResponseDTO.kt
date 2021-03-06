package com.goodfood.app.models.response_dtos

import com.goodfood.app.models.domain.Comment
import com.goodfood.app.models.domain.Recipe
import com.goodfood.app.models.domain.RecipeDetails
import com.goodfood.app.models.response_dtos.VideoDTO.Companion.getVideos


/**
 * Created by Lalit N. Hajare (Android Developer) on 23/4/21.
 *
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */
data class RecipeDetailsResponseDTO(
    val recipe: RecipeDetailsDTO? = null
) : BaseResponseDTO() {

    data class RecipeDetailsDTO(
        val imageUrls: List<String>? = null,
        val videos: List<VideoDTO>? = null,
        val avgRating: Int,
        val comments: List<CommentDTO>? = null,
        val isFeatured: Boolean? = false,
        val _id: String? = "",
        val description: String? = "",
        val price: Int? = 0,
        val title: String? = "",
        val creator: UserResponseDTO.UserDTO? = null,
        val createdAt: String? = "",
        val updatedAt: String? = "",
        val likeCount: Int? = 0,
        val smileyCount: Int? = 0,
        val prayerCount: Int? = 0
    )

    override fun getDomainModel(): RecipeDetails {

        fun getImages(list: List<String>?): String {
            return if (list?.isNotEmpty() == true) {
                list[0]
            } else {
                ""
            }
        }

        fun getDomainComments(list: List<CommentDTO>?): List<Comment>? {
            return list?.map { it.getDomainModel() }
        }

        return RecipeDetails(
            Recipe(
                recipeId = recipe?._id ?: "",
                recipeTitle = recipe?.title ?: "",
                recipeBrief = recipe?.description ?: "",
                recipePoster = getImages(recipe?.imageUrls),
                discount = 0,
                imgUrls = recipe?.imageUrls,
                videos = getVideos(recipe?.videos),
                profile = recipe?.creator?.getDomainModel(),
                price = recipe?.price,
                comments = getDomainComments(recipe?.comments)
            )
        )
    }
}