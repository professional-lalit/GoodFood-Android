package com.goodfood.app.di.modules

import com.goodfood.app.common.Constants
import com.goodfood.app.common.CustomApplication
import com.goodfood.app.common.Prefs
import com.goodfood.app.models.response_dtos.ErrorResponseDTO
import com.goodfood.app.networking.ServerInterface
import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.ConnectException
import java.net.NetworkInterface
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit
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

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun provideAPIClient(prefs: Prefs): OkHttpClient {
        val logInterceptor = HttpLoggingInterceptor()
        logInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient().newBuilder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .addInterceptor(logInterceptor)
            .addInterceptor(HeaderInterceptor(prefs))
            .build()
    }

    @Provides
    fun provideRetrofit(apiClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(apiClient)
            .baseUrl(Constants.BASE_URL)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideAPI(retrofit: Retrofit): ServerInterface {
        return retrofit.create(ServerInterface::class.java)
    }

    /**
     * This class is used to write headers at run time, when APIs are called
     */
    class HeaderInterceptor(val prefs: Prefs) : Interceptor {
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

}