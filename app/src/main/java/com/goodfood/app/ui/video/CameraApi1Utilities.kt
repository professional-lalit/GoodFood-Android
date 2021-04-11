@file:Suppress("DEPRECATION")

package com.goodfood.app.ui.video

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.util.Size
import android.util.SparseIntArray
import android.view.Surface
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

val MEDIA_TYPE_IMAGE = 1
val MEDIA_TYPE_VIDEO = 2

/** Create a file Uri for saving an image or video */
private fun getOutputMediaFileUri(type: Int): Uri {
    return Uri.fromFile(getOutputMediaFile(type))
}

/** Create a File for saving an image or video */
@SuppressLint("SimpleDateFormat")
fun getOutputMediaFile(type: Int): File? {
    // To be safe, you should check that the SDCard is mounted
    // using Environment.getExternalStorageState() before doing this.

    val mediaStorageDir = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
        "WHW_TEST_APP"
    )
    // This location works best if you want the created images to be shared
    // between applications and persist after your app has been uninstalled.

    // Create the storage directory if it does not exist
    mediaStorageDir.apply {
        if (!exists()) {
            if (!mkdirs()) {
                Log.d("WHW_TEST_APP", "failed to create directory")
                return null
            }
        }
    }

    // Create a media file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    return when (type) {
        MEDIA_TYPE_IMAGE -> {
            File("${mediaStorageDir.path}${File.separator}IMG_$timeStamp.jpg")
        }
        MEDIA_TYPE_VIDEO -> {
            File("${mediaStorageDir.path}${File.separator}VID_$timeStamp.mp4")
        }
        else -> null
    }
}


object CameraApi1Utilities {

    const val SENSOR_ORIENTATION_INVERSE_DEGREES = 270
    const val SENSOR_ORIENTATION_DEFAULT_DEGREES = 90
    val INVERSE_ORIENTATIONS = SparseIntArray()
    val DEFAULT_ORIENTATIONS = SparseIntArray()

    /**
     * In this sample, we choose a video size with 3x4 for  aspect ratio. for more perfectness 720
     * as well Also, we don't use sizes
     * larger than 1080p, since MediaRecorder cannot handle such a high-resolution video.
     *
     * @param choices The list of available sizes
     * @return The video size 1080p,720px
     */
    fun chooseVideoSize(choices: Array<Size>): Size {
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
    fun chooseOptimalSize(
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
            Collections.min(bigEnough, CameraApi2Usecase.CompareSizesByArea())
        } else {
            Log.e(javaClass.simpleName, "Couldn't find any suitable preview size")
            choices[0]
        }
    }

    val ORIENTATIONS = SparseIntArray(4)

    init {
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_270, 0)
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_180, 90)
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_90, 180)
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_0, 270)
    }

    init {
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_90, 0)
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_0, 90)
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_270, 180)
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_180, 270)
    }

    init {
        ORIENTATIONS.append(Surface.ROTATION_0, 90)
        ORIENTATIONS.append(Surface.ROTATION_90, 0)
        ORIENTATIONS.append(Surface.ROTATION_180, 270)
        ORIENTATIONS.append(Surface.ROTATION_270, 180)
    }

}