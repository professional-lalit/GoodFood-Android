@file:Suppress("DEPRECATION")

package com.goodfood.app.ui.video.utils

import android.content.Context
import android.hardware.Camera
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.media.CamcorderProfile
import android.media.MediaRecorder
import android.util.Log
import android.view.Surface
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.goodfood.app.BuildConfig
import com.goodfood.app.ui.video.widgets.CameraPreview
import java.io.File
import java.io.IOException

class CameraApi1Usecase(
    private val context: AppCompatActivity,
    private val preview: FrameLayout,
    private val cameraCallback: ICameraUsecase.CameraCallback,
    private val videoFile: File
) : ICameraUsecase {

    private var mCharacteristics: CameraCharacteristics? = null
    private var mSensorOrientation: Int? = CameraApi2Utilities.SENSOR_ORIENTATION_DEFAULT_DEGREES

    private var mediaRecorder: MediaRecorder? = null
    private var mCamera: Camera? = null
    private var mPreview: CameraPreview? = null

    private fun getCameraInstance(): Camera? {
        return try {
            val manager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
            val cameraId = manager.cameraIdList[0]
            mCharacteristics = manager.getCameraCharacteristics(cameraId)
            mSensorOrientation = mCharacteristics!!.get(CameraCharacteristics.SENSOR_ORIENTATION)
            Camera.open() // attempt to get a Camera instance
        } catch (e: Exception) {
            // Camera is not available (in use or does not exist)
            null // returns null if camera is unavailable
        }
    }

    private fun prepareVideoRecorder(): Boolean {
        mediaRecorder = MediaRecorder()

        mCamera?.let { camera ->
            camera.unlock()
            mediaRecorder?.run {
                setCamera(camera)
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setVideoSource(MediaRecorder.VideoSource.CAMERA)
                val profile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH)
                setProfile(profile)
                setOutputFile(videoFile.path)
                setPreviewDisplay(mPreview?.holder?.surface)
                setVideoEncodingBitRate(profile.videoBitRate / BuildConfig.VIDEO_BITRATE_DIVISOR)
                val rotation = context.windowManager.defaultDisplay.rotation
                when (mSensorOrientation) {
                    CameraApi2Utilities.SENSOR_ORIENTATION_DEFAULT_DEGREES -> {
                        setOrientationHint(
                            CameraApi2Utilities.DEFAULT_ORIENTATIONS[rotation]
                        )
                    }
                    CameraApi2Utilities.SENSOR_ORIENTATION_INVERSE_DEGREES -> setOrientationHint(
                        CameraApi2Utilities.INVERSE_ORIENTATIONS[rotation]
                    )
                }
                return try {
                    prepare()
                    true
                } catch (e: IllegalStateException) {
                    Log.d(
                        javaClass.simpleName,
                        "IllegalStateException preparing MediaRecorder: ${e.message}"
                    )
                    releaseMediaRecorder()
                    false
                } catch (e: IOException) {
                    Log.d(javaClass.simpleName, "IOException preparing MediaRecorder: ${e.message}")
                    releaseMediaRecorder()
                    false
                }
            }
        }
        return false
    }

    private fun releaseMediaRecorder() {
        mediaRecorder?.reset() // clear recorder configuration
        mediaRecorder?.release() // release the recorder object
        mediaRecorder = null
        mCamera?.lock() // lock camera for later use
    }

    private fun releaseCamera() {
        mCamera?.release() // release the camera for other applications
        mCamera = null
    }

    override fun init(vararg params: Any?) {
    }

    override fun onScreenPaused() {
        closeCamera()
    }

    override fun onScreenResumed() {
    }

    override fun startPreview() {
        // Create an instance of Camera
        mCamera = getCameraInstance()
        setCameraRotation()
        setCameraAutoFocus()
        mPreview = mCamera?.let {
            // Create our Preview view
            CameraPreview(context, it)
        }
        // Set the Preview view as the content of our activity.
        mPreview?.also {
            preview.addView(it)
        }
        cameraCallback.onPreview()
    }

    private fun setCameraRotation() {
        val info = Camera.CameraInfo()
        val rotation: Int = context.windowManager.defaultDisplay.rotation
        var degrees = 0
        when (rotation) {
            Surface.ROTATION_0 -> degrees = 0
            Surface.ROTATION_90 -> degrees = 90
            Surface.ROTATION_180 -> degrees = 270
            Surface.ROTATION_270 -> degrees = 180
        }
        val result = (info.orientation - degrees + 360) % 360
        mCamera?.setDisplayOrientation(result)
    }

    private fun setCameraAutoFocus() {
        val parameters: Camera.Parameters = mCamera!!.parameters
        val focusModes: List<String> = parameters.supportedFocusModes
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.focusMode = Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE
        } else if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            parameters.focusMode = Camera.Parameters.FOCUS_MODE_AUTO
        }
        mCamera!!.parameters = parameters
    }

    override fun startVideoRecording() {
        // initialize video camera
        if (prepareVideoRecorder()) {
            // Camera is available and unlocked, MediaRecorder is prepared,
            // now you can start recording
            mediaRecorder?.start()
        } else {
            // prepare didn't work, release the camera
            releaseMediaRecorder()
            // inform user
        }
    }

    override fun stopVideoRecording() {
        // stop recording and release camera
        try {
            mediaRecorder?.stop() // stop the recording
        } catch (ex: Exception) {
            Log.e(javaClass.simpleName, ex.localizedMessage ?: "")
        }
        releaseMediaRecorder() // release the MediaRecorder object
        mCamera?.lock() // take camera access back from MediaRecorder
    }

    override fun closeCamera() {
        releaseMediaRecorder() // if you are using MediaRecorder, release it first
        releaseCamera()
    }
}