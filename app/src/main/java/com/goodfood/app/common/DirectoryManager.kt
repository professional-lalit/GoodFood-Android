package com.goodfood.app.common

import android.content.Context
import android.os.Environment
import android.util.Log
import java.io.File
import java.util.*
import kotlin.Comparator


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
        private var CREATE_RECIPE_IMAGE_DIRECTORY_PATH = "MULTIMEDIA/RECIPE/CREATE/IMAGES"
        private var CREATE_RECIPE_VIDEO_DIRECTORY_PATH = "MULTIMEDIA/RECIPE/CREATE/VIDEOS"
        private var PROFILE_PIC_FILE_NAME = "profile_pic.jpg"
    }

    private fun getProfileImagesStoragePath(): File {
        return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            File(context.getExternalFilesDir(null), PROFILE_IMAGE_DIRECTORY_PATH)
        } else {
            File(context.filesDir, PROFILE_IMAGE_DIRECTORY_PATH)
        }
    }

    private fun getRecipeImagesStoragePath(): File {
        return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            File(context.getExternalFilesDir(null), CREATE_RECIPE_IMAGE_DIRECTORY_PATH)
        } else {
            File(context.filesDir, CREATE_RECIPE_IMAGE_DIRECTORY_PATH)
        }
    }

    private fun getRecipeVideosStoragePath(): File {
        return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            File(context.getExternalFilesDir(null), CREATE_RECIPE_VIDEO_DIRECTORY_PATH)
        } else {
            File(context.filesDir, CREATE_RECIPE_VIDEO_DIRECTORY_PATH)
        }
    }

    fun createProfileImageFile(): File {
        val file = getProfileImagesStoragePath()
        if (!file.exists()) {
            file.mkdirs()
        }
        val imgFile = File(file, "profile_pic.jpg")
        if (imgFile.exists()) {
            imgFile.delete()
        }
        imgFile.createNewFile()
        Log.d(javaClass.simpleName, "file created at: ${imgFile.absolutePath}")
        return imgFile
    }

    fun getProfileImageFile(): File {
        return File("${getProfileImagesStoragePath()}/${PROFILE_PIC_FILE_NAME}")
    }

    fun createRecipeImageFile(desiredFileName: String): File {
        val file = getRecipeImagesStoragePath()
        if (!file.exists()) {
            file.mkdirs()
        }
        val imgFile = File(file, "${desiredFileName}_${System.currentTimeMillis()}.jpg")

        val previouslySelectedFilesForItem = getRecipeImagesStoragePath()
            .listFiles()?.filter { it.name.contains(desiredFileName) }

        previouslySelectedFilesForItem?.forEach { it.delete() }

        imgFile.createNewFile()
        Log.d(javaClass.simpleName, "file created at: ${imgFile.absolutePath}")
        return imgFile
    }

    fun getRecipeImageFile(desiredFileName: String): File {
        val files = getRecipeImagesStoragePath().listFiles()
        val filesToSort = files?.filter { it.name.contains(desiredFileName) }
        return filesToSort?.last()!!
    }

    fun createRecipeVideoFile(desiredFileName: String): File {
        val file = getRecipeImagesStoragePath()
        if (!file.exists()) {
            file.mkdirs()
        }
        val imgFile = File(file, "${desiredFileName}.mp4")

        val previouslySelectedFilesForItem = getRecipeVideosStoragePath()
            .listFiles()?.filter { it.name.contains(desiredFileName) }

        previouslySelectedFilesForItem?.forEach { it.delete() }

        imgFile.createNewFile()
        Log.d(javaClass.simpleName, "file created at: ${imgFile.absolutePath}")
        return imgFile
    }

    fun deleteAllSavedCreateRecipeImages() {
        deleteRecursive(getRecipeImagesStoragePath())
    }

    private fun deleteRecursive(file: File) {
        val dir = getRecipeImagesStoragePath()
        if (file.isDirectory) {
            for (childFile in file.listFiles()) {
                deleteRecursive(childFile)
            }
        }
        file.delete()
    }


}