package com.goodfood.app.ui.home.fragments.create_recipe.usecases

import androidx.lifecycle.MutableLiveData
import com.goodfood.app.models.domain.Error
import com.goodfood.app.models.request_dtos.CreateRecipeRequestDTO
import com.goodfood.app.models.response_dtos.CreateRecipeResponseDTO
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
class RecipeDataUploadUsecase @Inject constructor(private val recipeRepository: RecipeRepository) {

    val errorData = MutableLiveData<Error>()

    suspend fun uploadRecipeData(requestDTO: CreateRecipeRequestDTO): CreateRecipeResponseDTO? {
        val result = recipeRepository.addRecipe(requestDTO)
        return if (result is NetworkResponse.NetworkSuccess) {
            result.data as CreateRecipeResponseDTO
        } else {
            val error = (result as NetworkResponse.NetworkError).error
            errorData.postValue(error)
            null
        }
    }


}