package com.goodfood.app.common.multimedia_managers

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.FragmentActivity
import com.goodfood.app.common.DirectoryManager
import com.goodfood.app.utils.GenericFileProvider
import dagger.hilt.android.qualifiers.ActivityContext
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

    override val cameraResult =
        context.registerForActivityResult(CameraActivityContract()) { result ->
            val file = File("ABCD")
            Log.d(javaClass.simpleName, "FILE SIZE: ${file.length()}")
            Log.d(javaClass.simpleName, "FILE PATH: ${file.absolutePath}")
            imageLoadedCallback?.invoke(file)
        }

    override val galleryResult =
        context.registerForActivityResult(GalleryActivityContract()) { resultUri ->
            if (resultUri != null) {
                val file = directoryManager.createRecipeImageFile()
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

    fun initCameraFlow() {
        val imgFile = directoryManager.createRecipeImageFile()
        val photoURI: Uri = FileProvider.getUriForFile(
            context,
            GenericFileProvider::class.java.canonicalName ?: "",
            imgFile
        )
        cameraResult.launch(photoURI)
    }

    fun initGalleryFlow() {
        galleryResult.launch(null)
    }

    fun deleteCreateRecipeImageFiles() {
        directoryManager.deleteAllSavedCreateRecipeImages()
    }


}