package com.goodfood.app.events


/**
 * Created by Lalit N. Hajare (Android Developer) on 31/03/21.
 *
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */
object EventConstants {

    enum class Event(val id: Int) {
        OPEN_CREATE_RECIPE_SCREEN(1),
        COPYING_VIDEO_FILE(2),
        RECIPE_UPLOADED(3),
        NOT_AUTHENTICATED(4),
        UPDATE_BOTTOM_NOTIFICATION_TITLE(5),
        COMMENTS_COUNT_CLICKED(6)
    }

}