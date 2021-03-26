package com.goodfood.app.common

import android.content.Context
import com.goodfood.app.models.domain.User
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created by Lalit N. Hajare (Android Developer) on 21/03/21.
 *
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */

@Singleton
class Prefs @Inject constructor(@ApplicationContext private val appContext: Context) {

    companion object {
        private const val APP_PREFS_NAME = "goodfood_prefs"
        private const val ACCESS_TOKEN = "access_token"
        private const val USER_MODEL = "user_model"
    }

    private val mPreferences =
        appContext.getSharedPreferences(APP_PREFS_NAME, Context.MODE_PRIVATE)

    var accessToken: String? = null
        set(value) {
            field = value
            mPreferences.edit().putString(ACCESS_TOKEN, value).apply()
        }
        get() {
            return mPreferences.getString(ACCESS_TOKEN, "")!!
        }

    var user: User? = null
        set(value) {
            field = value
            mPreferences.edit().putString(USER_MODEL, Gson().toJson(field)).apply()
        }
        get() {
            return Gson().fromJson(mPreferences.getString(USER_MODEL, ""), User::class.java)
        }

}