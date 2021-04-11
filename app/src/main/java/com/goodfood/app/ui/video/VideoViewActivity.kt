package com.goodfood.app.ui.video

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.goodfood.app.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import java.io.File
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class VideoViewActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var videoView: VideoView
    private lateinit var txtFileDetails: TextView
    private lateinit var mVideoPath: Uri

    companion object {
        fun startActivity(activity: AppCompatActivity, file: File) {
            activity.startActivity(Intent(activity, VideoViewActivity::class.java).apply {
                putExtra("video_file", file)
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_view)

        val file = intent.getSerializableExtra("video_file") as File?
        mVideoPath = Uri.fromFile(file)


        videoView = findViewById(R.id.video_view)
        txtFileDetails = findViewById(R.id.txt_file_details)

        setUpVideo(mVideoPath)

        txtFileDetails.text = "File Details:\n\n File Size: ${file?.length()?.div(1024)} KB"

    }

    private fun setUpVideo(videoPath: Uri) {
        val videoMediaController = MediaController(this)
        videoView.setVideoURI(videoPath)
        videoMediaController.setMediaPlayer(videoView)
        videoView.setMediaController(videoMediaController)
        videoView.requestFocus()
        videoView.start()
    }


    private fun setFileSize(filePath: String) {
        txtFileDetails.text =
            txtFileDetails.text.toString() + "\n\n" + "Compressed File Size:\n" + File(
                filePath
            ).length() / 1024 + "KB"
    }

    override val coroutineContext: CoroutineContext
        get() = Job()
}
