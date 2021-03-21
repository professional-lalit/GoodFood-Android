package com.goodfood.app.di.modules

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.goodfood.app.common.DirectoryManager
import com.goodfood.app.common.ImageManager
import com.goodfood.app.ui.common.dialogs.DialogManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext


/**
 * Created by Lalit N. Hajare (Android Developer) on 20/03/21.
 *
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */

/**
 * Consists of all the necessary dependencies an activity should possess
 */
@Module
@InstallIn(ActivityComponent::class)
class ActivityHelperModule {

    @Provides
    fun provideDialogManager(@ActivityContext context: Context): DialogManager {
        return DialogManager(context)
    }

    @Provides
    fun provideDirectoryManager(@ActivityContext context: Context): DirectoryManager {
        return DirectoryManager(context)
    }

    @Provides
    fun provideImageManager(
        @ActivityContext context: Context,
        directoryManager: DirectoryManager
    ): ImageManager {
        return ImageManager(context as AppCompatActivity, directoryManager)
    }
}