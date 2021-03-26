package com.goodfood.app.models.domain


/**
 * Created by Lalit N. Hajare (Android Developer) on 26/03/21.
 *
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */
data class User(
    val userId: String,
    val firstName: String,
    val lastName: String,
    val imageUrl: String,
    val email: String,
    val bio: String,
    val videoUrl: String
) {
    val fullname = "$firstName $lastName"
}