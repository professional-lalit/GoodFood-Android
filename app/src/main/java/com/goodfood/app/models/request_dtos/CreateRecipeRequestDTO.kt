package com.goodfood.app.models.request_dtos


/**
 * Created by Lalit N. Hajare (Android Developer) on 15/4/21.
 *
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */
data class CreateRecipeRequestDTO(
    val recipeData: RecipeData
) {
    data class RecipeData(
        val title: String,
        val description: String,
        val price: Double,
    )
}