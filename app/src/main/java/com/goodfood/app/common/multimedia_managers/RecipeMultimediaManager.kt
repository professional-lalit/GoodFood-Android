package com.goodfood.app.common.multimedia_managers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.goodfood.app.common.DirectoryManager
import com.goodfood.app.ui.home.fragments.create_recipe.CreateRecipeFragment
import com.goodfood.app.ui.video.CameraApi1Activity
import com.goodfood.app.ui.video.CameraApi2Activity
import com.goodfood.app.ui.video.VideoViewActivity
import com.goodfood.app.utils.GenericFileProvider
import com.goodfood.app.utils.Utils
import java.io.File


/**
 * Created by Lalit N. Hajare (Android Developer) on 02/04/21.
 *
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */
class RecipeMultimediaManager constructor(
    private val context: AppCompatActivity,
    directoryManager: DirectoryManager
) : BaseMultimediaManager(directoryManager) {

    private var imageLoadedCallback: ((File) -> Unit)? = null
    private var videoLoadedCallback: ((File) -> Unit)? = null
    private var desiredFileName: String? = null

    override val cameraResult =
        context.registerForActivityResult(CameraActivityContract()) { result ->
            val file = directoryManager.getRecipeImageFile(desiredFileName!!)
            Log.d(javaClass.simpleName, "FILE SIZE: ${file.length()}")
            Log.d(javaClass.simpleName, "FILE PATH: ${file.absolutePath}")
            imageLoadedCallback?.invoke(file)
        }

    override val galleryResult =
        context.registerForActivityResult(GalleryActivityContract()) { resultUri ->
            if (resultUri != null) {
                val file = directoryManager.createRecipeImageFile(desiredFileName!!)
                copyFile(resultUri, file)
                imageLoadedCallback?.invoke(file)
            }
        }

    class VideoRecordContract : ActivityResultContract<String, File?>() {
        override fun createIntent(context: Context, input: String): Intent {
            return if (Utils.isCameraApi2Supported(context as AppCompatActivity)) {
                Intent(context, CameraApi2Activity::class.java).apply {
                    putExtra("filename", input)
                }
            } else {
                Intent(context, CameraApi1Activity::class.java).apply {
                    putExtra("filename", input)
                }
            }
        }

        override fun parseResult(resultCode: Int, intent: Intent?): File? {
            val data = intent?.getSerializableExtra("file") as File?
            return if (resultCode == Activity.RESULT_OK) {
                data
            } else null
        }
    }

    private val recordVideoLauncherResult =
        context.registerForActivityResult(VideoRecordContract()) { file ->
            file?.let {
                videoLoadedCallback?.invoke(it)
            }
        }

    fun setImageLoadedCallback(imageLoadedCallback: (File) -> Unit) {
        this.imageLoadedCallback = imageLoadedCallback
    }

    fun setVideoLoadedCallback(videoLoadedCallback: (File) -> Unit) {
        this.videoLoadedCallback = videoLoadedCallback
    }


    override fun copyFile(resultUri: Uri, file: File?) {
        val inputStream = context.contentResolver.openInputStream(resultUri)!!
        val outputStream = context.contentResolver.openOutputStream(file?.toUri()!!)!!
        try {
            copy(inputStream, outputStream)
            inputStream.close()
            outputStream.close()
        } catch (ex: Exception) {
            Log.e(javaClass.simpleName, ex.localizedMessage ?: "")
        }
    }

    fun initCameraFlow(desiredFileName: String) {
        this.desiredFileName = desiredFileName
        val imgFile = directoryManager.createRecipeImageFile(desiredFileName)
        val photoURI: Uri = FileProvider.getUriForFile(
            context,
            GenericFileProvider::class.java.canonicalName ?: "",
            imgFile
        )
        cameraResult.launch(photoURI)
    }

    fun initGalleryFlow(desiredFileName: String) {
        this.desiredFileName = desiredFileName
        galleryResult.launch(null)
    }

    fun initVideoRecordFlow(desiredFileName: String) {
        this.desiredFileName = desiredFileName
        recordVideoLauncherResult.launch(this.desiredFileName)
    }

    fun deleteCreateRecipeImageFiles() {
        directoryManager.deleteAllSavedCreateRecipeImages()
    }


}