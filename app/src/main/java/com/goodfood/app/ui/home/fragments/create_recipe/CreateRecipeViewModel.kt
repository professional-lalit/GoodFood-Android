package com.goodfood.app.ui.home.fragments.create_recipe

import android.util.Log
import androidx.core.net.toFile
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.goodfood.app.models.domain.RecipePhoto
import com.goodfood.app.models.domain.RecipeVideo
import com.goodfood.app.models.domain.ServerMessage
import com.goodfood.app.models.response_dtos.CreateRecipeResponseDTO
import com.goodfood.app.networking.NetworkResponse
import com.goodfood.app.repositories.RecipeRepository
import com.goodfood.app.ui.common.BaseViewModel
import com.goodfood.app.ui.home.fragments.create_recipe.usecases.RecipeDataUploadUsecase
import com.goodfood.app.ui.home.fragments.create_recipe.usecases.RecipeImageUploadUsecase
import com.goodfood.app.ui.home.fragments.create_recipe.usecases.RecipeVideoUploadUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


/**
 * Created by Lalit N. Hajare (Android Developer) on 02/04/21.
 *
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */
@HiltViewModel
class CreateRecipeViewModel @Inject constructor(
    private val recipeImageUploadUsecase: RecipeImageUploadUsecase,
    private val recipeVideoUploadUsecase: RecipeVideoUploadUsecase,
    private val recipeDataUploadUsecase: RecipeDataUploadUsecase
) : BaseViewModel() {

    val photos = mutableListOf<RecipePhoto>()
    val videos = mutableListOf<RecipeVideo>()

    enum class RecipeUploadState {
        INIT, PHOTOS_UPLOAD_INIT, PHOTOS_UPLOADED, VIDEOS_UPLOAD_INIT, VIDEOS_UPLOADED, SUCCESS, FAILED
    }

    private val _currentUploadingVideo = recipeVideoUploadUsecase.currentUploadingVideo
    val currentUploadingVideo: LiveData<RecipeVideo> = _currentUploadingVideo

    private val _currentUploadingPhoto = recipeImageUploadUsecase.currentUploadingPhoto
    val currentUploadingPhoto: LiveData<RecipePhoto> = _currentUploadingPhoto

    private val _recipeUploadState = MutableLiveData<RecipeUploadState>()
    val recipeUploadState: LiveData<RecipeUploadState> = _recipeUploadState

    val createRecipeUI = CreateRecipeUI()


    fun uploadRecipe() {
        viewModelScope.launch {
            createRecipeUI.isRecipeDataUploading = true
            _recipeUploadState.postValue(RecipeUploadState.INIT)
            val recipeDataResponse = withContext(coroutineContext) {
                recipeDataUploadUsecase.uploadRecipeData(createRecipeUI.getRequestDTO())
            }
            if (recipeDataResponse?.recipeId?.isNotEmpty() == true) {
                if (photos.isNotEmpty()) {
                    _recipeUploadState.postValue(RecipeUploadState.PHOTOS_UPLOAD_INIT)
                    withContext(coroutineContext) {
                        recipeImageUploadUsecase.uploadRecipePhotos(recipeDataResponse.recipeId, photos)
                    }
                    _recipeUploadState.postValue(RecipeUploadState.PHOTOS_UPLOADED)
                }
                if (videos.isNotEmpty()) {
                    _recipeUploadState.postValue(RecipeUploadState.VIDEOS_UPLOAD_INIT)
                    withContext(coroutineContext) {
                        recipeVideoUploadUsecase.uploadRecipeVideos(recipeDataResponse.recipeId, videos)
                    }
                    _recipeUploadState.postValue(RecipeUploadState.VIDEOS_UPLOADED)
                }
                _recipeUploadState.postValue(RecipeUploadState.SUCCESS)
            } else {
                _recipeUploadState.postValue(RecipeUploadState.FAILED)
            }
            createRecipeUI.isRecipeDataUploading = false
        }
    }

}