package com.goodfood.app.repositories

import com.goodfood.app.models.request_dtos.CreateRecipeRequestDTO
import com.goodfood.app.models.response_dtos.*
import com.goodfood.app.networking.NetworkResponse
import com.goodfood.app.networking.ServerInterface
import com.goodfood.app.utils.Utils.getMimeType
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject


/**
 * Created by Lalit N. Hajare (Android Developer) on 15/4/21.
 *
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */
class RecipeRepository @Inject constructor(private val serverInterface: ServerInterface) {

    suspend fun addRecipe(recipeRequestDTO: CreateRecipeRequestDTO): NetworkResponse {
        val response = serverInterface.createRecipe(recipeRequestDTO)
        return when {
            response.code() in 200..210 -> {
                val responseDTO: CreateRecipeResponseDTO? = Gson().fromJson(
                    Gson().toJson(response.body()),
                    CreateRecipeResponseDTO::class.java
                )
                NetworkResponse.NetworkSuccess(responseDTO)
            }
            else -> {
                val type = object : TypeToken<ErrorResponseDTO>() {}.type
                val errorResponseDTO: ErrorResponseDTO =
                    Gson().fromJson(response.errorBody()!!.charStream(), type)
                val errorData = errorResponseDTO.getDomainModel()
                errorData.status = response.code()
                NetworkResponse.NetworkError(errorData)
            }
        }
    }

    suspend fun uploadRecipeImage(recipeId: String, file: File): NetworkResponse {

        val requestBody: RequestBody = file.asRequestBody(getMimeType(file)!!.toMediaType())
        val partFile: MultipartBody.Part =
            MultipartBody.Part.createFormData("recipeImage", file.name, requestBody)

        val response = serverInterface.uploadRecipeImage(recipeId, partFile)
        return if (response.code() in 200..210) {
            val uploadRecipeImageResponseDTO = Gson().fromJson(
                Gson().toJson(response.body()),
                UploadRecipeImageResponseDTO::class.java
            )
            val serverMessage = uploadRecipeImageResponseDTO.getDomainModel()
            NetworkResponse.NetworkSuccess(serverMessage)
        } else {
            val type = object : TypeToken<ErrorResponseDTO>() {}.type
            val errorResponseDTO: ErrorResponseDTO =
                Gson().fromJson(response.errorBody()!!.charStream(), type)
            val errorData = errorResponseDTO.getDomainModel()
            errorData.status = response.code()
            NetworkResponse.NetworkError(errorData)
        }
    }

}