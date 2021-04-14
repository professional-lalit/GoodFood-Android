package com.goodfood.app.ui.home.fragments.create_recipe

import android.util.Log
import androidx.core.net.toFile
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.goodfood.app.models.domain.RecipePhoto
import com.goodfood.app.models.domain.RecipeVideo
import com.goodfood.app.models.domain.ServerMessage
import com.goodfood.app.models.response_dtos.CreateRecipeResponseDTO
import com.goodfood.app.models.response_dtos.LoginResponseDTO
import com.goodfood.app.networking.NetworkResponse
import com.goodfood.app.repositories.RecipeRepository
import com.goodfood.app.ui.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
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
    private val recipeRepository: RecipeRepository
) : BaseViewModel() {

    private val _recipeUploadResponse = MutableLiveData<CreateRecipeResponseDTO>()
    val recipeUploadResponse: LiveData<CreateRecipeResponseDTO> = _recipeUploadResponse

    private val _currentUploadingVideo = MutableLiveData<RecipeVideo>()
    val currentUploadingVideo: LiveData<RecipeVideo> = _currentUploadingVideo

    private val _currentUploadingPhoto = MutableLiveData<RecipePhoto>()
    val currentUploadingPhoto: LiveData<RecipePhoto> = _currentUploadingPhoto

    private val _isMultimediaUploadInProgress = MutableLiveData<Boolean>()
    val isMultimediaUploadInProgress: LiveData<Boolean> = _isMultimediaUploadInProgress

    val createRecipeUI = CreateRecipeUI()

    private lateinit var photoUploadProgressFlow: Flow<Int>
    private lateinit var videoUploadProgressFlow: Flow<Int>

    init {
        setUpPhotosUploadFlow()
        setUpVideosUploadFlow()
    }

    private fun setUpPhotosUploadFlow() {
        photoUploadProgressFlow = flow {
            (0..100).forEach {
                delay(50)
                emit(it)
            }
        }.flowOn(Dispatchers.IO)
    }

    private fun setUpVideosUploadFlow() {
        videoUploadProgressFlow = flow {
            (0..100).forEach {
                delay(50)
                emit(it)
            }
        }.flowOn(Dispatchers.IO)
    }

    fun startMultimediaUpload(recipeId: String, photos: List<RecipePhoto>, videos: List<RecipeVideo>) {
        viewModelScope.launch(Dispatchers.IO) {
            _isMultimediaUploadInProgress.postValue(true)
            withContext(coroutineContext) { uploadRecipePhotos(recipeId, photos) }
            withContext(coroutineContext) { uploadRecipeVideos(videos) }
            _isMultimediaUploadInProgress.postValue(false)
        }
    }

    private suspend fun uploadRecipePhotos(recipeId: String, photos: List<RecipePhoto>) {
        photos.forEach { photo ->
            uploadPhoto(recipeId, photo)
        }
    }

    private suspend fun uploadPhoto(recipeId: String, photo: RecipePhoto) {
        val fileToUpload = photo.imgUri?.toFile()
        val imageResponse = recipeRepository.uploadRecipeImage(recipeId, fileToUpload!!)
        if (imageResponse is NetworkResponse.NetworkSuccess) {
            val data = imageResponse.data as ServerMessage
            Log.d(javaClass.simpleName, "file uploaded")
        } else {
            val errorData = (imageResponse as NetworkResponse.NetworkError).error
            _errorData.postValue(errorData)
        }

        photoUploadProgressFlow.collect {
            photo.uploadProgress = it
            _currentUploadingPhoto.postValue(photo)
        }
    }

    private suspend fun uploadRecipeVideos(videos: List<RecipeVideo>) {
        videos.forEach { video ->
            uploadVideo(video)
        }
    }

    private suspend fun uploadVideo(video: RecipeVideo) {
        videoUploadProgressFlow.collect {
            video.uploadProgress = it
            _currentUploadingVideo.postValue(video)
        }
    }

    fun uploadRecipeData() {
        viewModelScope.launch {
            createRecipeUI.isRecipeDataUploading = true
            val result = recipeRepository.addRecipe(createRecipeUI.getRequestDTO())
            createRecipeUI.isRecipeDataUploading = false
            if (result is NetworkResponse.NetworkSuccess) {
                val data = result.data as CreateRecipeResponseDTO
                _recipeUploadResponse.postValue(data)
            } else {
                val errorData = (result as NetworkResponse.NetworkError).error
                _errorData.postValue(errorData)
            }
        }
    }

}