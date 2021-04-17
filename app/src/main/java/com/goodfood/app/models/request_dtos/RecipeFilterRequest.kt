package com.goodfood.app.models.request_dtos


/**
 * Created by Lalit N. Hajare (Android Developer) on 17/4/21.
 *
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */

data class RecipeFilterRequest(
    val filter: RecipeFilterData
) {
    data class RecipeFilterData(
        val title: String,
        val priceRange: Range,
        val avgRatingRange: Range,
        val creatorId: String
    ) {
        data class Range(
            val lowEnd: Int? = 0,
            val highEnd: Int? = 0
        )
    }
}