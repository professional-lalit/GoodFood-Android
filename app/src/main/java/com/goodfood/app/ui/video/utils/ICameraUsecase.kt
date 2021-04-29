package com.goodfood.app.ui.video.utils

import androidx.lifecycle.LifecycleObserver
import java.io.File

interface ICameraUsecase: LifecycleObserver {

    fun init(vararg params: Any?)

    fun onScreenPaused()

    fun onScreenResumed()

    fun startPreview()

    fun startVideoRecording()
    
    fun stopVideoRecording()

    fun closeCamera()

    interface CameraCallback {
        fun onPreview()
        fun onError(exception: Exception?)
    }

    class CameraApi2NotSupportedException(override val message: String?): java.lang.Exception(message)

}