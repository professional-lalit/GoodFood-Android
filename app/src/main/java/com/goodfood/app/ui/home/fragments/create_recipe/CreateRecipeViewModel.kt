package com.goodfood.app.ui.home.fragments.create_recipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.goodfood.app.models.domain.RecipePhoto
import com.goodfood.app.models.domain.RecipeVideo
import com.goodfood.app.ui.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
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

    val createRecipeUI = CreateRecipeUI()

    fun uploadRecipeData() {

    }

    private val photoUploadProgressFlow: Flow<Int> = flow {
        var count = 0
        while (count == 100) {
            count++
            emit(count)
            Log.d(javaClass.simpleName,"EMITTED PHOTO PROGRESS: $count")
            delay(500L)
        }
    }

    private val videoUploadProgressFlow: Flow<Int> = flow {
        var count = 0
        while (count == 100) {
            count++
            emit(count)
            Log.d(javaClass.simpleName,"EMITTED VIDEO PROGRESS: $count")
            delay(500L)
        }
    }

    fun uploadRecipePhotos(photos: List<RecipePhoto>) {
        viewModelScope.launch {
            photos.forEach { photo ->
                uploadPhoto(photo)
            }
        }
    }

    private fun uploadPhoto(photo: RecipePhoto) {
        _currentUploadingPhoto.postValue(photo)
        viewModelScope.launch {
            photoUploadProgressFlow.collect {
                val recipePhoto = _currentUploadingPhoto.value
                recipePhoto?.uploadProgress = it
                _currentUploadingPhoto.postValue(recipePhoto!!)
            }
        }
    }

    fun uploadRecipeVideos(videos: List<RecipeVideo>) {
        viewModelScope.launch {
            videos.forEach { video ->
                uploadVideo(video)
            }
        }
    }

    private fun uploadVideo(video: RecipeVideo) {
        _currentUploadingVideo.postValue(video)
        viewModelScope.launch {
            videoUploadProgressFlow.collect {
                val recipeVideo = _currentUploadingVideo.value
                recipeVideo?.uploadProgress = it
                _currentUploadingVideo.postValue(recipeVideo!!)
            }
        }
    }

}