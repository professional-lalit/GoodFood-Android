package com.goodfood.app.models.response_dtos

import com.goodfood.app.models.domain.Recipe


/**
 * Created by Lalit N. Hajare (Android Developer) on 17/4/21.
 *
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */
data class RecipeListResponseDTO(
    val list: List<RecipeDTO>
) : BaseResponseDTO() {

    data class RecipeDTO(
        val recipeId: String,
        val title: String,
        val description: String,
        val price: Double? = 0.00,
        val imageUrls: List<String>? = null,
        val videoUrls: List<String>? = null,
        val avgRating: Int = 0,
        val isFeatured: Boolean? = false,
        val creator: UserResponseDTO.UserDTO? = null
    ) : BaseResponseDTO() {
        override fun getDomainModel(): Recipe {

            fun getImages(list: List<String>?): String {
                return if (list?.isNotEmpty() == true) {
                    list[0]
                } else {
                    ""
                }
            }

            return Recipe(
                recipeTitle = title,
                recipeBrief = description,
                recipePoster = getImages(imageUrls),
                discount = 0,
                imgUrls = imageUrls,
                videoUrls = videoUrls,
                profile = creator?.getDomainModel()
            )
        }
    }

    override fun getDomainModel(): List<Recipe> {
        val recipeDomainList = mutableListOf<Recipe>()
        list.forEach { recipeDTO ->
            recipeDomainList.add(recipeDTO.getDomainModel())
        }
        return recipeDomainList
    }

}