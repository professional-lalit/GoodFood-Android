package com.goodfood.app.models.response_dtos

import com.goodfood.app.models.domain.Recipe


/**
 * Created by Lalit N. Hajare (Android Developer) on 28/4/21.
 *
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */
data class VideoDTO(
    val _id: String?,
    val creator: String?,
    val thumbUrl: String?,
    val url: String?,
    val title: String?
) : BaseResponseDTO() {

    override fun getDomainModel(): Recipe.Video {
        return Recipe.Video(
            videoId = _id ?: "",
            thumbUrl = thumbUrl ?: "",
            url = url ?: "",
            title = title ?: ""
        )
    }

    companion object {
        fun getVideos(list: List<VideoDTO>?): List<Recipe.Video> {
            val videoList = mutableListOf<Recipe.Video>()
            list?.forEach {
                it?.let { videoList.add(it.getDomainModel()) }
            }
            return videoList
        }
    }

}