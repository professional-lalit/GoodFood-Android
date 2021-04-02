package com.goodfood.app.common.multimedia_managers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.goodfood.app.common.DirectoryManager
import com.goodfood.app.utils.GenericFileProvider
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


/**
 * Created by Lalit N. Hajare (Android Developer) on 02/04/21.
 *
 * This project is for demonstration purposes. It is highly likely that you must
 * be seeing this code by clicking the link in my resume.
 * This code is free and can be reused by anyone,
 * also if any suggestions they are welcomed at: `lalit.appsmail@gmail.com`
 * (please keep the subject as 'GoodFood Android Code Suggestion')
 */
abstract class BaseMultimediaManager(open val context: Context, open val directoryManager: DirectoryManager) {

    protected abstract val cameraResult: ActivityResultLauncher<Uri>
    protected abstract val galleryResult: ActivityResultLauncher<Any?>

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

    protected abstract fun copyFile(resultUri: Uri)

    @Throws(IOException::class)
    protected fun copy(input: InputStream, output: OutputStream): Long {
        var count: Long = 0
        var n: Int
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        while (-1 !== input.read(buffer).also { n = it }) {
            output.write(buffer, 0, n)
            count += n.toLong()
        }
        return count
    }

}