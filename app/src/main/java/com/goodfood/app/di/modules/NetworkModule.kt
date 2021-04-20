package com.goodfood.app.di.modules

import android.content.Context
import android.content.Intent
import com.goodfood.app.common.Constants
import com.goodfood.app.di.qualifiers.MultimediaHttpClient
import com.goodfood.app.di.qualifiers.MultimediaRetrofit
import com.goodfood.app.di.qualifiers.MultimediaServerInterface
import com.goodfood.app.events.EventConstants
import com.goodfood.app.events.sendSticky
import com.goodfood.app.networking.HeaderInterceptor
import com.goodfood.app.networking.ServerInterface
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * Created by Lalit N. Hajare (Android Developer) on 21/03/21.
 *
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 *
 * The retrofit instances for binary uploads/downloads are managed separately
 * because retrofit logs the binary data which could lead to `OutOfMemory` Exception
 * I have disabled it in multimedia retrofit instance.
 *
 */

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    companion object {
        private const val TIMEOUT_INTERVAL_SECS = 15L
    }

    /**
     * ***************************************************************************************
     * ***************************** RETROFIT FOR MULTIMEDIA/BINARY DATA *********************
     * ***************************************************************************************
     */

    @MultimediaHttpClient
    @Provides
    fun provideMMAPIClient(headerInterceptor: HeaderInterceptor): OkHttpClient {
        val logInterceptor = HttpLoggingInterceptor()
        logInterceptor.level = HttpLoggingInterceptor.Level.NONE
        return OkHttpClient().newBuilder()
            .connectTimeout(TIMEOUT_INTERVAL_SECS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_INTERVAL_SECS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_INTERVAL_SECS, TimeUnit.SECONDS)
            .addInterceptor(logInterceptor)
            .addInterceptor(headerInterceptor)
            .build()
    }

    @MultimediaRetrofit
    @Provides
    fun provideMMRetrofit(@MultimediaHttpClient apiClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(apiClient)
            .baseUrl(Constants.BASE_URL)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @MultimediaServerInterface
    @Provides
    fun provideMMAPI(@MultimediaRetrofit retrofit: Retrofit): ServerInterface {
        return retrofit.create(ServerInterface::class.java)
    }

    /**
     * ***************************************************************************************
     */

    /**
     * ***************************************************************************************
     * ******************************** RETROFIT FOR NORMAL DATA *****************************
     * ***************************************************************************************
     */

    @Provides
    fun provideAPIClient(
        @ApplicationContext context: Context,
        headerInterceptor: HeaderInterceptor
    ): OkHttpClient {
        val logInterceptor = HttpLoggingInterceptor()
        logInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient().newBuilder()
            .connectTimeout(TIMEOUT_INTERVAL_SECS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_INTERVAL_SECS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_INTERVAL_SECS, TimeUnit.SECONDS)
            .addInterceptor(logInterceptor)
            .addInterceptor(headerInterceptor)
            .addInterceptor { chain ->
                val request = chain.request()
                val response = chain.proceed(request)
                if (response.code == 403) {
                    //handleForbiddenResponse
                } else if (response.code == 401) {
                    context.sendBroadcast(Intent("unauthenticated"))
                }
                response
            }
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
     * ***************************************************************************************
     */

}