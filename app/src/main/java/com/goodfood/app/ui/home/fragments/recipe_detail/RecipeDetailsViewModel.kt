package com.goodfood.app.ui.home.fragments.recipe_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.goodfood.app.models.domain.Recipe
import com.goodfood.app.models.domain.RecipeDetails
import com.goodfood.app.models.request_dtos.RecipeFilterRequest
import com.goodfood.app.models.response_dtos.RecipeDetailsResponseDTO
import com.goodfood.app.models.response_dtos.RecipeListResponseDTO
import com.goodfood.app.networking.NetworkResponse
import com.goodfood.app.repositories.RecipeRepository
import com.goodfood.app.ui.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Created by Lalit N. Hajare (Android Developer) on 23/4/21.
 *
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */
@HiltViewModel
class RecipeDetailsViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository
) : BaseViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _recipeDetails = MutableLiveData<RecipeDetails>()
    val recipeDetails: LiveData<RecipeDetails> = _recipeDetails

    fun loadRecipeDetails(recipeId: String){
        viewModelScope.launch {
            _loading.postValue(true)
            val response = recipeRepository.getRecipeDetails(recipeId)
            if (response is NetworkResponse.NetworkSuccess) {
                response.data as RecipeDetailsResponseDTO
                _recipeDetails.postValue(response.data.getDomainModel())
            } else {
                val errorData = (response as NetworkResponse.NetworkError).error
                _errorData.postValue(errorData)
            }
            _loading.postValue(false)
        }
    }

}