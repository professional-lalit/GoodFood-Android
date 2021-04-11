package com.goodfood.app.ui.video

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.media.AudioManager
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import com.goodfood.app.common.DirectoryManager
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
open class BaseCameraActivity : AppCompatActivity() {
    companion object {
        private val TAG = BaseCameraActivity::class.java.simpleName
        var isActive = false
        private const val REQ_RECORD_PERM = 200
        private const val REQ_CAMERA_PERM = 201
        private const val REQ_STORAGE_PERM = 203
        const val REQ_LOCATION_SETTINGS = 204
        const val REQ_COARSE_LOCATION_PERM = 205
        const val REQ_FINE_LOCATION_PERM = 206
    }

    protected var cameraUsecase: ICameraUsecase? = null
    protected var videoFile: File? = null
    private var videoFileName: String? = null

    var isRecordingVideo = false

    @Inject
    lateinit var directoryManager: DirectoryManager


    private var mLifecycleRegistry: LifecycleRegistry? = null
    protected var count = 0

    private var mTimer: Timer? = null

    private fun stopTimer() {
        mTimer!!.cancel()
        mTimer = null
    }

    protected fun startTimer() {
        count = 1
        mTimer = Timer()
        mTimer!!.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                count++
                updateTime()
            }
        }, 1000, 1000)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        videoFileName = intent.getStringExtra("filename")
        isActive = true
        mLifecycleRegistry = LifecycleRegistry(this)
        mLifecycleRegistry!!.currentState = Lifecycle.State.CREATED
        this.registerReceiver(mBatInfoReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        setUpScreen()
        checkPerms()
    }

    private fun initializeFiles() {
        videoFile = if (videoFileName?.isNotEmpty() == true) {
            directoryManager.createRecipeVideoFile(videoFileName!!)
        } else {
            directoryManager.createRecipeVideoFile(
                "RECIPE_VID_${System.currentTimeMillis()}"
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isActive = false
        unregisterReceiver(mBatInfoReceiver)
    }

    override fun onResume() {
        super.onResume()
        mLifecycleRegistry!!.currentState = Lifecycle.State.RESUMED
        mLifecycleRegistry!!.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        cameraUsecase?.onScreenResumed()
    }

    override fun onPause() {
        super.onPause()
        mLifecycleRegistry!!.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    }

    override fun onStart() {
        super.onStart()
        mLifecycleRegistry!!.currentState = Lifecycle.State.STARTED
    }

    private fun checkPerms() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ), REQ_RECORD_PERM
                )
            } else {
                enableScreen()
            }
        } else {
            enableScreen()
        }
    }

    private fun setUpScreen() {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    protected fun prepareVideoRecording() {
        cameraUsecase?.startVideoRecording()
        isRecordingVideo = true
        startTimer()
        onVideoRecordingStarted()
        val currentOrientation = resources.configuration.orientation
        requestedOrientation = if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE
        } else {
            ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT
        }
    }

    protected fun stopRecUI() {
        cameraUsecase?.stopVideoRecording()
        isRecordingVideo = false
        stopTimer()
        setResult(RESULT_OK, intent.apply { putExtra("file", videoFile!!) })
        finish()
    }

    private val mBatInfoReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(ctxt: Context, intent: Intent) {
            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
            showBatteryStatus(level)
        }
    }


    protected open fun updateSnapshotValuesUI(strRollAngleForText: String, strAzimuth: String) {}
    protected open fun showBatteryStatus(level: Int) {}
    protected open fun hideSensorDataViews() {}
    protected open fun onVideoRecordingStarted() {}
    protected open fun updateTime() {}
    protected open fun enableScreen() {
        initializeFiles()
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.size == 4 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED && grantResults[3] == PackageManager.PERMISSION_GRANTED
        ) {
            recreate()
        } else {
            when {
                checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED -> {
                    requestPermissions(
                        arrayOf(Manifest.permission.CAMERA),
                        REQ_CAMERA_PERM
                    )
                }
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED -> {
                    requestPermissions(
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        REQ_STORAGE_PERM
                    )
                }
                checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED -> {
                    requestPermissions(
                        arrayOf(Manifest.permission.RECORD_AUDIO),
                        REQ_STORAGE_PERM
                    )
                }
                else -> {
                    recreate()
                }
            }
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_LOCATION_SETTINGS) {
            checkPerms()
        }
    }

}