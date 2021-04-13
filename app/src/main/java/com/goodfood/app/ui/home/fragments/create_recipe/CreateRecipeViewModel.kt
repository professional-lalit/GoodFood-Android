package com.goodfood.app.ui.home.fragments.create_recipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.goodfood.app.models.domain.RecipePhoto
import com.goodfood.app.models.domain.RecipeVideo
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
class CreateRecipeViewModel @Inject constructor() : BaseViewModel() {

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

    fun startMultimediaUpload(photos: List<RecipePhoto>, videos: List<RecipeVideo>) {
        viewModelScope.launch(Dispatchers.IO) {
            _isMultimediaUploadInProgress.postValue(true)
            withContext(coroutineContext) { uploadRecipePhotos(photos) }
            withContext(coroutineContext) { uploadRecipeVideos(videos) }
            _isMultimediaUploadInProgress.postValue(false)
        }
    }

    private suspend fun uploadRecipePhotos(photos: List<RecipePhoto>) {
        photos.forEach { photo ->
            uploadPhoto(photo)
        }
    }

    private suspend fun uploadPhoto(photo: RecipePhoto) {
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

}