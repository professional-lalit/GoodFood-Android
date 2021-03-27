package com.goodfood.app.repositories

import android.service.autofill.UserData
import android.webkit.MimeTypeMap
import com.goodfood.app.common.Prefs
import com.goodfood.app.models.domain.User
import com.goodfood.app.models.response_dtos.ErrorResponseDTO
import com.goodfood.app.models.response_dtos.UploadProfileImageResponseDTO
import com.goodfood.app.models.response_dtos.UserResponseDTO
import com.goodfood.app.networking.NetworkResponse
import com.goodfood.app.networking.ServerInterface
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.scopes.ViewModelScoped
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject


/**
 * Created by Lalit N. Hajare (Android Developer) on 21/03/21.
 *
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */

/**
 * Consists of user specific APIs & operations
 */
@ViewModelScoped
class UserRepository @Inject constructor(
    private val serverInterface: ServerInterface,
    private val prefs: Prefs
) {


    private fun getMimeType(file: File): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(file.path)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }

    suspend fun uploadUserImage(userId: String, file: File): NetworkResponse {

        val requestBody: RequestBody = file.asRequestBody(getMimeType(file)!!.toMediaType())
        val partFile: MultipartBody.Part = createFormData("profileImage", file.name, requestBody)

        val response = serverInterface.uploadUserImage(userId, partFile)
        return if (response.code() in 200..210) {
            val uploadProfileImageResponseDTO = Gson().fromJson(
                Gson().toJson(response.body()),
                UploadProfileImageResponseDTO::class.java
            )
            val serverMessage = uploadProfileImageResponseDTO.getDomainModel()
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

    suspend fun fetchMeDetails(): NetworkResponse {
        val response = serverInterface.fetchMeDetails()
        return if (response.code() in 200..210) {
            val userDTO: UserResponseDTO? = Gson().fromJson(
                Gson().toJson(response.body()),
                UserResponseDTO::class.java
            )
            val user = userDTO?.getDomainModel()
            prefs.user = user
            NetworkResponse.NetworkSuccess(user)
        } else {
            val type = object : TypeToken<ErrorResponseDTO>() {}.type
            val errorResponseDTO: ErrorResponseDTO =
                Gson().fromJson(response.errorBody()!!.charStream(), type)
            val errorData = errorResponseDTO.getDomainModel()
            errorData.status = response.code()
            NetworkResponse.NetworkError(errorData)
        }
    }

    fun logout(){
        prefs.clearPrefs()
    }

}