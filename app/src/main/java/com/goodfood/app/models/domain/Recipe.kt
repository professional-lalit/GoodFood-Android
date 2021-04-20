package com.goodfood.app.models.domain

import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.EpoxyModel
import com.goodfood.app.R


/**
 * Created by Lalit N. Hajare (Android Developer) on 30/03/21.
 *
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */
data class Recipe(
    val recipeTitle: String,
    val recipeBrief: String,
    val recipePoster: String? = null,
    val discount: Int? = 0,
    val imgUrls: List<String>? = null,
    val videoUrls: List<String>? = null,
    val profile: User? = null,
    val price: Int? = 0
) {

    fun getPhotoCount(): Int {
        return imgUrls?.size ?: 0
    }

    fun getVideoCount(): Int {
        return videoUrls?.size ?: 0
    }

    fun getPhotoCountText(): String {
        return if (imgUrls?.isNotEmpty() == true) {
            "${imgUrls.size} Photos"
        } else {
            "No Photos"
        }
    }

    fun getVideoCountText(): String {
        return if (videoUrls?.isNotEmpty() == true) {
            "${videoUrls.size} Videos"
        } else {
            "No Videos"
        }
    }

    fun getPriceText(): String {
        return "$ $price"
    }

}