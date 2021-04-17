package com.goodfood.app.utils

import android.content.Context
import android.graphics.Bitmap
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.media.CamcorderProfile
import android.media.MediaMetadataRetriever
import android.util.Log
import android.util.Patterns
import android.webkit.MimeTypeMap
import androidx.appcompat.app.AppCompatActivity
import com.goodfood.app.models.domain.Error
import com.goodfood.app.models.response_dtos.ErrorResponseDTO
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Response
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException


/**
 * Created by Lalit N. Hajare (Android Developer) on 21/03/21.
 *
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */
object Utils {

    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isPasswordValid(password: String): Boolean {
        return password.matches(Regex("[a-zA-Z0-9]+"))
    }

    fun isMobileNumberValid(mobileNumber: String): Boolean {
        return mobileNumber.length == 10
    }

    fun isPasswordLengthValid(password: String): Boolean {
        return password.length >= 6
    }

    fun getProfile(): CamcorderProfile {
        return when {
            CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_1080P) -> {
                CamcorderProfile.get(CamcorderProfile.QUALITY_1080P)
            }
            CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_720P) -> {
                CamcorderProfile.get(CamcorderProfile.QUALITY_720P)
            }
            CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_480P) -> {
                CamcorderProfile.get(CamcorderProfile.QUALITY_480P)
            }
            else -> {
                CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH)
            }
        }
    }

    fun isCameraApi2Supported(activity: AppCompatActivity): Boolean {
        try {
            val manager: CameraManager =
                activity.getSystemService(Context.CAMERA_SERVICE) as CameraManager
            for (cameraId in manager.cameraIdList) {
                val chars: CameraCharacteristics =
                    manager.getCameraCharacteristics(cameraId)
                val facing = chars.get(CameraCharacteristics.LENS_FACING)
                if (facing != null && facing == CameraCharacteristics.LENS_FACING_BACK) {
                    val camApi2SupportLevel =
                        chars.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL)
                    if (camApi2SupportLevel in CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_FULL..CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_3)
                        return true
                }
            }
        } catch (ex: Exception) {
            Log.e(javaClass.simpleName, ex.localizedMessage ?: "")
        }
        return false
    }

    fun getVideoFrame(videoFile: File): Bitmap? {
        try {
            val retriever = MediaMetadataRetriever()
            try {
                retriever.setDataSource(videoFile.absolutePath)
                val timeInSeconds = 1
                return retriever.getFrameAtTime(
                    timeInSeconds * 1000000.toLong(),
                    MediaMetadataRetriever.OPTION_CLOSEST_SYNC
                )
            } catch (ex: java.lang.Exception) {
                Log.i(javaClass.simpleName, "MediaMetadataRetriever got exception:$ex")
            }
            Log.d(javaClass.simpleName, "thumbnail saved successfully")
        } catch (e: FileNotFoundException) {
            Log.d(javaClass.simpleName, "File Not Found Exception : check directory path")
            e.printStackTrace()
        } catch (e: IOException) {
            Log.d(javaClass.simpleName, "IOException while closing the stream")
            e.printStackTrace()
        }
        return null
    }

    fun getMimeType(file: File): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(file.path)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }

    fun parseError(response: Response<Any?>): Error {
        val type = object : TypeToken<ErrorResponseDTO>() {}.type
        val errorResponseDTO: ErrorResponseDTO =
            Gson().fromJson(response.errorBody()!!.charStream(), type)
        val errorData = errorResponseDTO.getDomainModel()
        errorData.status = response.code()
        return errorData
    }

}