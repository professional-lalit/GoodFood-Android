package com.goodfood.app.models.domain

import android.graphics.Bitmap
import android.net.Uri


/**
 * Created by Lalit N. Hajare (Android Developer) on 02/04/21.
 *
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */
data class RecipeVideo(
    var videoThumbUri: Uri? = null,
    var videoUri: Uri? = null,
    var state: MediaState
) {
    var uploadProgress: Int = 0
}