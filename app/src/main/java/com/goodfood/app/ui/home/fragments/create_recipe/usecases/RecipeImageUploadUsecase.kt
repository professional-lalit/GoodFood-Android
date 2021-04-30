package com.goodfood.app.ui.home.fragments.create_recipe.usecases

import android.util.Log
import androidx.core.net.toFile
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.goodfood.app.models.domain.Error
import com.goodfood.app.models.domain.RecipePhoto
import com.goodfood.app.models.domain.ServerMessage
import com.goodfood.app.networking.NetworkResponse
import com.goodfood.app.repositories.RecipeRepository
import dagger.hilt.android.scopes.ViewModelScoped
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
@ViewModelScoped
class RecipeImageUploadUsecase @Inject constructor(private val recipeRepository: RecipeRepository) {

    val errorData = MutableLiveData<Error>()
    val currentUploadingPhoto = MutableLiveData<RecipePhoto>()
    val failedUploads = mutableListOf<RecipePhoto>()

    suspend fun uploadRecipePhotos(recipeId: String, photos: List<RecipePhoto>) {
        Log.d(javaClass.simpleName, "TEST::uploading photos")
        photos.forEach { photo ->
            uploadPhoto(recipeId, photo)
        }
    }

    private suspend fun uploadPhoto(recipeId: String, photo: RecipePhoto) {
        val fileToUpload = photo.imgUri?.toFile()
        Log.d(javaClass.simpleName, "TEST::uploading photo")
        val imageResponse = recipeRepository.uploadRecipeImage(recipeId, fileToUpload!!)
        { progress ->
            photo.uploadProgress = progress
            currentUploadingPhoto.postValue(photo)
        }
        if (imageResponse is NetworkResponse.NetworkSuccess) {
            val data = imageResponse.data as ServerMessage
            Log.d(javaClass.simpleName, "file uploaded")
        } else {
            failedUploads.add(photo)
            val error = (imageResponse as NetworkResponse.NetworkError).error
            errorData.postValue(error)
        }
    }

}