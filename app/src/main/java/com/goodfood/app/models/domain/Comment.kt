package com.goodfood.app.models.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Lalit N. Hajare (Android Developer) on 30/4/21.
 *
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */
@Serializable
@Parcelize
data class Comment(
    val user: User,
    val comment: String,
    val commentedAt: String,
    val reactions: List<Reaction>?
) : Parcelable {

    fun formatCommentTime(): Date? {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        try {
            return format.parse(commentedAt)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }

}