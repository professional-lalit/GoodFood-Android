package com.goodfood.app.common

import android.content.Context
import android.os.Environment
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
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

/**
 * Manages all the low level file handling, typically for an activity
 */
class DirectoryManager constructor(private val context: Context) {

    companion object {
        private var PROFILE_IMAGE_DIRECTORY_PATH = "MULTIMEDIA/PROFILE/IMAGES"
        private var PROFILE_PIC_FILE_NAME = "profile_pic.jpg"
    }

    private fun getStoragePath(): File {
        return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            File(context.getExternalFilesDir(null), PROFILE_IMAGE_DIRECTORY_PATH)
        } else {
            File(context.filesDir, PROFILE_IMAGE_DIRECTORY_PATH)
        }
    }

    fun createImageFile() {
        val file = getStoragePath()
        if (!file.exists()) {
            file.mkdirs()
        }
        val imgFile = File(file, "profile_pic.jpg")
        if(imgFile.exists()){
            imgFile.delete()
        }
        imgFile.createNewFile()
        Log.d(javaClass.simpleName, "file created at: ${imgFile.absolutePath}")
    }

    fun getProfileImageFile(): File {
        return File("${getStoragePath()}/${PROFILE_PIC_FILE_NAME}")
    }


}