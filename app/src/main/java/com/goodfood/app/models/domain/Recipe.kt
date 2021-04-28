package com.goodfood.app.models.domain

import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.EpoxyModel
import com.goodfood.app.R
import java.io.Serializable


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
    val recipeId: String,
    val recipeTitle: String,
    val recipeBrief: String,
    val recipePoster: String? = null,
    val discount: Int? = 0,
    val imgUrls: List<String>? = null,
    val videos: List<Video>? = null,
    val profile: User? = null,
    val price: Int? = 0
) : Serializable {

    data class Video(
        val videoId: String,
        val thumbUrl: String,
        val url: String,
        val title: String
    )

    fun getPhotoCount(): Int {
        return imgUrls?.size ?: 0
    }

    fun getVideoCount(): Int {
        return videos?.size ?: 0
    }

    fun getPhotoCountText(): String {
        return if (imgUrls?.isNotEmpty() == true) {
            "${imgUrls.size} Photos"
        } else {
            "No Photos"
        }
    }

    fun getVideoCountText(): String {
        return if (videos?.isNotEmpty() == true) {
            "${videos.size} Videos"
        } else {
            "No Videos"
        }
    }

    fun getPriceText(): String {
        return "$ $price"
    }

}