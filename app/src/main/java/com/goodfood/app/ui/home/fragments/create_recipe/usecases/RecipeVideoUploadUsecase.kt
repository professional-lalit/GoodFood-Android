package com.goodfood.app.ui.home.fragments.create_recipe.usecases

import android.util.Log
import androidx.core.net.toFile
import androidx.lifecycle.MutableLiveData
import com.goodfood.app.models.domain.Error
import com.goodfood.app.models.domain.RecipePhoto
import com.goodfood.app.models.domain.RecipeVideo
import com.goodfood.app.models.domain.ServerMessage
import com.goodfood.app.networking.NetworkResponse
import com.goodfood.app.repositories.RecipeRepository
import javax.inject.Inject


/**
 * Created by Lalit N. Hajare (Android Developer) on 30/4/21.
 *
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */
class RecipeVideoUploadUsecase @Inject constructor(private val recipeRepository: RecipeRepository) {

    val errorData = MutableLiveData<Error>()
    val currentUploadingVideo = MutableLiveData<RecipeVideo>()

    suspend fun uploadRecipeVideos(recipeId: String, videos: List<RecipeVideo>) {
        videos.forEach { video ->
            uploadVideo(recipeId, video)
        }
    }

    private suspend fun uploadVideo(recipeId: String, video: RecipeVideo) {
        val fileToUpload = video.videoUri?.toFile()
        val thumbFileToUpload = video.videoThumbUri?.toFile()
        val imageResponse =
            recipeRepository.uploadRecipeVideo(recipeId, fileToUpload!!, thumbFileToUpload!!)
            { progress ->
                video.uploadProgress = progress
                currentUploadingVideo.postValue(video)
            }
        if (imageResponse is NetworkResponse.NetworkSuccess) {
            val data = imageResponse.data as ServerMessage
            Log.d(javaClass.simpleName, "file uploaded")
        } else {
            val error = (imageResponse as NetworkResponse.NetworkError).error
            errorData.postValue(error)
        }
    }


}