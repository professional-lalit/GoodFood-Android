package com.goodfood.app.common.multimedia_managers

import android.net.Uri
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.goodfood.app.common.DirectoryManager
import com.goodfood.app.utils.GenericFileProvider
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
    context: AppCompatActivity,
    directoryManager: DirectoryManager
) : BaseMultimediaManager(context, directoryManager) {

    private var imageLoadedCallback: ((File) -> Unit)? = null
    fun setImageLoadedCallback(imageLoadedCallback: (File) -> Unit) {
        this.imageLoadedCallback = imageLoadedCallback
    }

    var desiredRecipeImageFileName = ""

    init {
        directoryManager.clearSavedData()
    }

    override val cameraResult =
        context.registerForActivityResult(CameraActivityContract()) { result ->
            val file = directoryManager.getRecipeImageFile(desiredRecipeImageFileName)
            Log.d(javaClass.simpleName, "FILE SIZE: ${file.length()}")
            Log.d(javaClass.simpleName, "FILE PATH: ${file.absolutePath}")
            imageLoadedCallback?.invoke(file)
        }

    override val galleryResult =
        context.registerForActivityResult(GalleryActivityContract()) { resultUri ->
            if (resultUri != null) {
                directoryManager.createRecipeImageFile(desiredRecipeImageFileName)
                copyFile(resultUri)
                val file = directoryManager.getRecipeImageFile(desiredRecipeImageFileName)
                imageLoadedCallback?.invoke(file)
            }
        }

    override fun copyFile(resultUri: Uri) {
        val inputStream = context.contentResolver.openInputStream(resultUri)!!
        val outputStream = context.contentResolver.openOutputStream(
            directoryManager.getRecipeImageFile(desiredRecipeImageFileName).toUri()
        )!!
        try {
            copy(inputStream, outputStream)
            inputStream.close()
            outputStream.close()
        } catch (ex: Exception) {
            Log.e(javaClass.simpleName, ex.localizedMessage ?: "")
        }
    }

    fun initCameraFlow(desiredFileName: String) {
        desiredRecipeImageFileName = desiredFileName
        directoryManager.createRecipeImageFile(desiredFileName)
        val imgFile = directoryManager.getRecipeImageFile(desiredFileName)
        val photoURI: Uri = FileProvider.getUriForFile(
            context,
            GenericFileProvider::class.java.canonicalName ?: "",
            imgFile
        )
        cameraResult.launch(photoURI)
    }

    fun initGalleryFlow(desiredFileName: String) {
        desiredRecipeImageFileName = desiredFileName
        galleryResult.launch(null)
    }


}