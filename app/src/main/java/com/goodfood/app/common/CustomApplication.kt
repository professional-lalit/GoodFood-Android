package com.goodfood.app.common

import android.app.Application
import android.util.DisplayMetrics
import dagger.hilt.android.HiltAndroidApp


/**
 * Created by Lalit N. Hajare (Android Developer) on 20/03/21.
 *
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */

@HiltAndroidApp
class CustomApplication : Application() {

    companion object {
        private lateinit var mInstance: CustomApplication
    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this
    }

}