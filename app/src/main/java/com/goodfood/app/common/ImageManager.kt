package com.goodfood.app.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.goodfood.app.utils.GenericFileProvider
import dagger.hilt.android.qualifiers.ActivityContext
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject


/**
 * Created by Lalit N. Hajare (Android Developer) on 20/03/21.
 *
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */
class ImageManager constructor(
    private val context: AppCompatActivity,
    private val directoryManager: DirectoryManager
) {

    private var imageLoadedCallback: ((File) -> Unit)? = null
    fun setImageLoadedCallback(imageLoadedCallback: (File) -> Unit) {
        this.imageLoadedCallback = imageLoadedCallback
    }

    class CameraActivityContract : ActivityResultContract<Uri, Any?>() {

        override fun createIntent(context: Context, input: Uri): Intent {
            val camIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            camIntent.putExtra(MediaStore.EXTRA_OUTPUT, input)
            return camIntent
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Any? {
            val data = intent?.data
            return if (resultCode == Activity.RESULT_OK && data != null) {
                data
            } else null
        }
    }

    class GalleryActivityContract : ActivityResultContract<Any?, Uri?>() {

        override fun createIntent(context: Context, input: Any?): Intent {
            val galleryIntent = Intent()
            galleryIntent.type = "image/*"
            galleryIntent.action = Intent.ACTION_GET_CONTENT
            return galleryIntent
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            val data = intent?.data
            return if (resultCode == Activity.RESULT_OK && data != null) {
                data
            } else null
        }
    }


    private val cameraResult =
        (context as AppCompatActivity).registerForActivityResult(CameraActivityContract()) { result ->
            val file = directoryManager.getProfileImageFile()
            Log.d(javaClass.simpleName, "FILE SIZE: ${file.length()}")
            Log.d(javaClass.simpleName, "FILE PATH: ${file.absolutePath}")
            imageLoadedCallback?.invoke(file)
        }

    private val galleryResult =
        (context as AppCompatActivity).registerForActivityResult(GalleryActivityContract()) { resultUri ->
            if (resultUri != null) {
                directoryManager.createImageFile()
                copyFile(resultUri)
                val file = directoryManager.getProfileImageFile()
                imageLoadedCallback?.invoke(file)
            }
        }

    private fun copyFile(resultUri: Uri) {
        val inputStream = context.contentResolver.openInputStream(resultUri)!!
        val outputStream = context.contentResolver.openOutputStream(
            directoryManager.getProfileImageFile().toUri()
        )!!
        try {
            copy(inputStream, outputStream)
            inputStream.close()
            outputStream.close()
        } catch (ex: Exception) {
            Log.e(javaClass.simpleName, ex.localizedMessage ?: "")
        }
    }

    @Throws(IOException::class)
    private fun copy(input: InputStream, output: OutputStream): Long {
        var count: Long = 0
        var n: Int
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        while (-1 !== input.read(buffer).also { n = it }) {
            output.write(buffer, 0, n)
            count += n.toLong()
        }
        return count
    }

    fun initCameraFlow() {
        directoryManager.createImageFile()
        val imgFile = directoryManager.getProfileImageFile()
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

}