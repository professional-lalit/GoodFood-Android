package com.goodfood.app.models.domain

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
data class Reaction(
    val username: String,
    val reaction: String,
    val reactedAt: String,
    val reactions: List<Reaction>?
) {
    fun formatReactionTime(): Date? {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        try {
            return format.parse(reactedAt)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }
}