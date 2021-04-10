package com.goodfood.app.utils

import androidx.lifecycle.*


/**
 * Created by Lalit N. Hajare (Android Developer) on 10/4/21.
 *
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */

/**
 * As `onActivityCreated` is deprecated in fragment, by our intelligent google developers, we now
 * need to find this hack in order to make the things work
 */
class ActivityLifeObserver(private val callback: () -> Unit) : DefaultLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        owner.lifecycle.removeObserver(this)
        callback()
    }

}