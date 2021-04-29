package com.goodfood.app.ui.video.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Matrix
import android.graphics.RectF
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.media.MediaRecorder
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.util.Range
import android.util.Size
import android.view.Surface
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.goodfood.app.BuildConfig
import com.goodfood.app.R
import com.goodfood.app.utils.Utils
import com.goodfood.app.ui.video.utils.CameraApi2Utilities.DEFAULT_ORIENTATIONS
import com.goodfood.app.ui.video.utils.CameraApi2Utilities.INVERSE_ORIENTATIONS
import com.goodfood.app.ui.video.utils.CameraApi2Utilities.SENSOR_ORIENTATION_DEFAULT_DEGREES
import com.goodfood.app.ui.video.utils.CameraApi2Utilities.SENSOR_ORIENTATION_INVERSE_DEGREES
import com.goodfood.app.ui.video.widgets.AutoFitTextureView
import java.io.File
import java.io.IOException
import java.util.*
import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit

class CameraApi2Usecase(
    private val context: AppCompatActivity,
    private val cameraCallback: ICameraUsecase.CameraCallback,
    private val videoFile: File
) : ICameraUsecase {

    private var textureView: AutoFitTextureView? = null

    private var mCharacteristics: CameraCharacteristics? = null


    private var mCameraDevice: CameraDevice? = null
    private var mPreviewSession: CameraCaptureSession? = null
    private var previewSurface: Surface? = null
    private var mPreviewSize: Size? = null
    private var mVideoSize: Size? = null
    private var mSensorOrientation: Int? = SENSOR_ORIENTATION_DEFAULT_DEGREES
    private var mDeviceRotation = 0
    private var mPreviewBuilder: CaptureRequest.Builder? = null

    private var mMediaRecorder: MediaRecorder? = null
    private var mBackgroundThread: HandlerThread? = null
    private var mBackgroundHandler: Handler? = null
    private val mCameraOpenCloseLock = Semaphore(1)

    private val mStateCallback: CameraDevice.StateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(cameraDevice: CameraDevice) {
            mCameraDevice = cameraDevice
            cameraCallback.onPreview()
            mCameraOpenCloseLock.release()
        }

        override fun onDisconnected(cameraDevice: CameraDevice) {
            mCameraOpenCloseLock.release()
            cameraDevice.close()
            mCameraDevice = null
        }

        override fun onError(cameraDevice: CameraDevice, error: Int) {
            mCameraOpenCloseLock.release()
            cameraDevice.close()
            mCameraDevice = null
            cameraCallback.onError(null)
        }
    }

    /**
     * Starts a background thread and its [Handler].
     */
    private fun startBackgroundThread() {
        mBackgroundThread = HandlerThread("CameraBackground")
        mBackgroundThread!!.start()
        mBackgroundHandler = Handler(mBackgroundThread!!.looper)
    }

    /**
     * Stops the background thread and its [Handler].
     */
    private fun stopBackgroundThread() {
        if (mBackgroundThread != null) {
            mBackgroundThread!!.quitSafely()
            try {
                mBackgroundThread!!.join()
                mBackgroundThread = null
                mBackgroundHandler = null
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * In this sample, we choose a video size with 3x4 for  aspect ratio. for more perfectness 720
     * as well Also, we don't use sizes
     * larger than 1080p, since MediaRecorder cannot handle such a high-resolution video.
     *
     * @param choices The list of available sizes
     * @return The video size 1080p,720px
     */
    private fun chooseVideoSize(choices: Array<Size>): Size? {
        for (size in choices) {
            if (1920 == size.width && 1080 == size.height) {
                return size
            }
        }
        for (size in choices) {
            if (size.width == size.height * 4 / 3 && size.width <= 1080) {
                return size
            }
        }
        Log.e(javaClass.simpleName, "Couldn't find any suitable video size")
        return choices[choices.size - 1]
    }

    /**
     * Given `choices` of `Size`s supported by a camera, chooses the smallest one whose
     * width and height are at least as large as the respective requested values, and whose aspect
     * ratio matches with the specified value.
     *
     * @param choices     The list of sizes that the camera supports for the intended output class
     * @param width       The minimum desired width
     * @param height      The minimum desired height
     * @param aspectRatio The aspect ratio
     * @return The optimal `Size`, or an arbitrary one if none were big enough
     */
    private fun chooseOptimalSize(
        choices: Array<Size>,
        width: Int,
        height: Int,
        aspectRatio: Size?
    ): Size {
        // Collect the supported resolutions that are at least as big as the preview Surface
        val bigEnough: MutableList<Size> = ArrayList()
        val w = aspectRatio!!.width
        val h = aspectRatio.height
        for (option in choices) {
            if (option.height == option.width * h / w && option.width >= width && option.height >= height) {
                bigEnough.add(option)
            }
        }
        // Pick the smallest of those, assuming we found any
        return if (bigEnough.size > 0) {
            Collections.min(bigEnough, CompareSizesByArea())
        } else {
            Log.e(javaClass.simpleName, "Couldn't find any suitable preview size")
            choices[0]
        }
    }


    /**
     * Tries to open a [CameraDevice]. The result is listened by `mStateCallback`.
     */
    @SuppressLint("MissingPermission")
    fun openCamera(width: Int, height: Int) {
        val manager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            Log.d(javaClass.simpleName, "tryAcquire")
            if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                throw RuntimeException("Time out waiting to lock camera opening.")
            }
            val cameraId = manager.cameraIdList[0]
            mCharacteristics = manager.getCameraCharacteristics(cameraId)
            val map = mCharacteristics!!
                .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)

            mSensorOrientation = mCharacteristics!!.get(CameraCharacteristics.SENSOR_ORIENTATION)
            mVideoSize = chooseVideoSize(
                map!!.getOutputSizes(
                    MediaRecorder::class.java
                )
            )
            mPreviewSize = chooseOptimalSize(
                map.getOutputSizes(
                    SurfaceTexture::class.java
                ),
                width, height, mVideoSize
            )

//            if (mSensorOrientation == Configuration.ORIENTATION_LANDSCAPE) {
//                textureView?.setAspectRatio(mPreviewSize!!.width, mPreviewSize!!.height)
//            } else {
//                textureView?.setAspectRatio(mPreviewSize!!.height, mPreviewSize!!.width)
//            }
            configureTransform(width, height)

            mMediaRecorder = MediaRecorder()
            manager.openCamera(cameraId, mStateCallback, null)
        } catch (e: CameraAccessException) {
            Log.e(javaClass.simpleName, "openCamera: Cannot access the camera.")
        } catch (e: NullPointerException) {
            Log.e(javaClass.simpleName, "Camera2API is not supported on the device.")
        } catch (e: InterruptedException) {
            throw RuntimeException("Interrupted while trying to lock camera opening.")
        }
    }

    override fun init(vararg params: Any?) {
        if (params.isNotEmpty() && params[0] != null && params[0] is AutoFitTextureView) {
            this.textureView = params[0] as AutoFitTextureView
        }
    }

    override fun onScreenPaused() {
        closeCamera()
        stopBackgroundThread()
    }

    override fun onScreenResumed() {
        startBackgroundThread()
    }

    override fun startVideoRecording() {

        if (null == mCameraDevice || !textureView?.isAvailable!! || null == mPreviewSize) {
            return
        }
        try {
            closePreviewSession()
            if (setUpMediaRecorder()) {
                val texture = textureView?.surfaceTexture!!
                texture.setDefaultBufferSize(mPreviewSize!!.width, mPreviewSize!!.height)
                mPreviewBuilder = mCameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                val surfaces: MutableList<Surface> = ArrayList()
                /**
                 * Surface for the camera preview set up
                 */
                val previewSurface = Surface(texture)
                surfaces.add(previewSurface)
                mPreviewBuilder!!.addTarget(previewSurface)
                //MediaRecorder setup for surface
                val recorderSurface = mMediaRecorder!!.surface
                surfaces.add(recorderSurface)
                mPreviewBuilder!!.addTarget(recorderSurface)
                mCameraDevice!!.createCaptureSession(
                    surfaces,
                    object : CameraCaptureSession.StateCallback() {
                        override fun onConfigured(session: CameraCaptureSession) {
                            mPreviewSession = session
                            updatePreview()
                            // Start recording
                            mMediaRecorder!!.start()
                        }

                        override fun onConfigureFailed(session: CameraCaptureSession) {
                            Log.e(javaClass.simpleName, "onConfigureFailed: Failed")
                            cameraCallback.onError(
                                ICameraUsecase.CameraApi2NotSupportedException(
                                    "Camera Api 2 does not work properly on this device"
                                )
                            )
                        }
                    },
                    mBackgroundHandler
                )
            } else {
                Toast.makeText(
                    context,
                    context.resources.getString(R.string.unable_to_record),
                    Toast.LENGTH_SHORT
                ).show()
            }
        } catch (ex: CameraAccessException) {
            ex.printStackTrace()
            Toast.makeText(
                context,
                context.resources.getString(R.string.unable_to_record) + " " + ex.localizedMessage,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun stopVideoRecording() {
        try {
            try {
                mPreviewSession!!.stopRepeating()
                mPreviewSession!!.abortCaptures()
            } catch (e: CameraAccessException) {
                e.printStackTrace()
            }
            mMediaRecorder!!.stop()
            mMediaRecorder!!.reset()
            mMediaRecorder = null
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    /**
     * close camera and release object
     */
    override fun closeCamera() {
        try {
            mCameraOpenCloseLock.acquire()
            closePreviewSession()
            if (null != mCameraDevice) {
                mCameraDevice!!.close()
                mCameraDevice = null
            }
            if (null != mMediaRecorder) {
                mMediaRecorder!!.release()
                mMediaRecorder = null
            }
        } catch (e: InterruptedException) {
            throw RuntimeException("Interrupted while trying to lock camera closing.")
        } finally {
            mCameraOpenCloseLock.release()
        }
    }


    override fun startPreview() {
        val matrix = textureView?.width?.let { configureTransform(it, textureView?.height ?: 0) }
        textureView?.setTransform(matrix)
        if (null == mCameraDevice || !textureView?.isAvailable!! || null == mPreviewSize) {
            return
        }
        try {
            closePreviewSession()
            val texture = textureView?.surfaceTexture!!
            texture.setDefaultBufferSize(mPreviewSize!!.width, mPreviewSize!!.height)
            mPreviewBuilder = mCameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            previewSurface = Surface(texture)
            mPreviewBuilder!!.addTarget(previewSurface!!)
            mPreviewBuilder!!.set(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, Range(25, 60))

//            updateAutoFocus()

            mCameraDevice!!.createCaptureSession(
                listOf(previewSurface!!),
                object : CameraCaptureSession.StateCallback() {
                    override fun onConfigured(session: CameraCaptureSession) {
                        mPreviewSession = session
                        updatePreview()
                    }

                    override fun onConfigureFailed(session: CameraCaptureSession) {
                        Log.e(javaClass.simpleName, "onConfigureFailed: Failed ")
                        cameraCallback.onError(
                            ICameraUsecase.CameraApi2NotSupportedException(
                                "Camera Api 2 does not work properly on this device"
                            )
                        )
                    }
                }, mBackgroundHandler
            )
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    /**
     * Update the camera preview. [.startPreview] needs to be called in advance.
     */
    private fun updatePreview() {
        if (null == mCameraDevice) {
            return
        }
        try {
            setUpCaptureRequestBuilder(mPreviewBuilder)
            val thread = HandlerThread("CameraPreview")
            thread.start()
            mPreviewSession!!.setRepeatingRequest(
                mPreviewBuilder!!.build(),
                null,
                mBackgroundHandler
            )
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun setUpCaptureRequestBuilder(builder: CaptureRequest.Builder?) {
        builder!!.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)
    }

    /**
     * Configures the necessary [Matrix] transformation to `mTextureView`.
     * This method should not to be called until the camera preview size is determined in
     * openCamera, or until the size of `mTextureView` is fixed.
     *
     * @param viewWidth  The width of `mTextureView`
     * @param viewHeight The height of `mTextureView`
     */
    fun configureTransform(viewWidth: Int, viewHeight: Int): Matrix {
        val rotation = context.windowManager.defaultDisplay.rotation
        val matrix = Matrix()
        val viewRect = RectF(0F, 0F, viewWidth.toFloat(), viewHeight.toFloat())
        val bufferRect = RectF(
            0F, 0F, mPreviewSize!!.height.toFloat(), mPreviewSize!!.width
                .toFloat()
        )
        val centerX = viewRect.centerX()
        val centerY = viewRect.centerY()
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY())
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL)
            val scale = Math.max(
                viewHeight.toFloat() / mPreviewSize!!.height,
                viewWidth.toFloat() / mPreviewSize!!.width
            )
            matrix.postScale(scale, scale, centerX, centerY)
            matrix.postRotate(90 * (rotation - 2).toFloat(), centerX, centerY)
        }
        return matrix
    }

    private fun setUpMediaRecorder(): Boolean {
        mMediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        mMediaRecorder!!.setVideoSource(MediaRecorder.VideoSource.SURFACE)
        mMediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)

        mMediaRecorder!!.setOutputFile(videoFile.absolutePath)

        val profile = Utils.getProfile()

        mMediaRecorder!!.setVideoFrameRate(30)
        mMediaRecorder!!.setVideoSize(profile.videoFrameWidth, profile.videoFrameHeight)
        mMediaRecorder!!.setVideoEncodingBitRate(profile.videoBitRate / BuildConfig.VIDEO_BITRATE_DIVISOR)
        mMediaRecorder!!.setVideoEncoder(MediaRecorder.VideoEncoder.H264)
        mMediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mMediaRecorder!!.setAudioEncodingBitRate(profile.audioBitRate)
        mMediaRecorder!!.setAudioSamplingRate(profile.audioSampleRate)

        val rotation = context.windowManager.defaultDisplay.rotation
        when (mSensorOrientation) {
            SENSOR_ORIENTATION_DEFAULT_DEGREES -> {
                mMediaRecorder!!.setOrientationHint(
                    DEFAULT_ORIENTATIONS[rotation]
                )
            }
            SENSOR_ORIENTATION_INVERSE_DEGREES -> mMediaRecorder!!.setOrientationHint(
                INVERSE_ORIENTATIONS[rotation]
            )
        }

        try {
            mMediaRecorder!!.prepare()
        } catch (e: IllegalStateException) {
            Log.d("ERROR", "IllegalStateException preparing MediaRecorder: " + e.message)
            stopVideoRecording()
            return false
        } catch (e: IOException) {
            Log.d("ERROR", "IOException preparing MediaRecorder: " + e.message)
            stopVideoRecording()
            return false
        }
        return true
    }

    private fun closePreviewSession() {
        if (mPreviewSession != null) {
            mPreviewSession!!.close()
            mPreviewSession = null
        }
    }

    /**
     * Compares two `Size`s based on their areas.
     */
    internal class CompareSizesByArea : Comparator<Size> {
        override fun compare(lhs: Size, rhs: Size): Int {
            // We cast here to ensure the multiplications won't overflow
            return java.lang.Long.signum(
                lhs.width.toLong() * lhs.height -
                        rhs.width.toLong() * rhs.height
            )
        }
    }
}