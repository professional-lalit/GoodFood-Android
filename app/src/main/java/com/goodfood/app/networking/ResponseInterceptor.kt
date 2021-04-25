package com.goodfood.app.networking

import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.goodfood.app.common.Constants
import com.goodfood.app.common.Prefs
import com.goodfood.app.events.EventConstants
import com.goodfood.app.events.sendEvent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created by Lalit N. Hajare (Android Developer) on 25/4/21.
 *
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */
@Singleton
class ResponseInterceptor @Inject constructor(
    @ApplicationContext private val context: Context,
    private val prefs: Prefs
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        if (response.code == 403) {
            //handleForbiddenResponse
        } else if (response.code == 401) {
            prefs.clearPrefs()
            LocalBroadcastManager.getInstance(context).sendBroadcast(
                Intent(Constants.UNAUTHENTICATED)
            )
        }
        return response
    }

}