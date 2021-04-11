package com.goodfood.app.utils

import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.media.CamcorderProfile
import android.util.DisplayMetrics
import android.util.Log
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.goodfood.app.common.CustomApplication


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

}