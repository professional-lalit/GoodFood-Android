package com.goodfood.app.common.multimedia_managers

import android.net.Uri
import android.util.Log
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
    private val context: AppCompatActivity,
    directoryManager: DirectoryManager
) : BaseMultimediaManager(directoryManager) {

    private var imageLoadedCallback: ((File) -> Unit)? = null
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

    fun setImageLoadedCallback(imageLoadedCallback: (File) -> Unit) {
        this.imageLoadedCallback = imageLoadedCallback
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

    fun deleteCreateRecipeImageFiles() {
        directoryManager.deleteAllSavedCreateRecipeImages()
    }


}