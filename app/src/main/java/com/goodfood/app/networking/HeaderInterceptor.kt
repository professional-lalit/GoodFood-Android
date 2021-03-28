package com.goodfood.app.networking

import com.goodfood.app.common.Prefs
import com.goodfood.app.models.response_dtos.ErrorResponseDTO
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.net.ConnectException
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created by Lalit N. Hajare (Android Developer) on 28/03/21.
 *
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */

/**
 * This class checks every request's with authorization token and handles network disconnection.
 */

@Singleton
class HeaderInterceptor @Inject constructor(val prefs: Prefs) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var contentLength = 0L
        if (chain.request().body != null) {
            contentLength = chain.request().body!!.contentLength()
        }
        val requestBuilder: Request.Builder = chain.request()
            .newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("Content-Length", contentLength.toString())
        if (prefs.accessToken != null && prefs.accessToken!!.isNotEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer " + prefs.accessToken!!)
        }
        return try {
            chain.proceed(requestBuilder.build())
        } catch (ex: ConnectException) {
            createResponse(requestBuilder.build())
        }
    }

    private fun createResponse(request: Request): Response {
        val errorBody = ErrorResponseDTO(null, "Could not connect to server")
        return Response.Builder()
            .code(408)
            .request(request)
            .protocol(Protocol.HTTP_1_0)
            .message("Please check internet connection")
            .body(
                Gson().toJson(errorBody).toResponseBody("application/json".toMediaTypeOrNull())
            ).build()
    }
}
