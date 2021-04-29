@file:Suppress("DEPRECATION")

package com.goodfood.app.ui.video

import android.os.Bundle
import android.util.Log
import android.view.View
import com.goodfood.app.R
import com.goodfood.app.databinding.ActivityCameraApi1Binding
import com.goodfood.app.ui.video.utils.CameraApi1Usecase
import com.goodfood.app.ui.video.utils.ICameraUsecase
import com.goodfood.app.utils.Extensions.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CameraApi1Activity : BaseCameraActivity(),
    ICameraUsecase.CameraCallback {

    private lateinit var binding: ActivityCameraApi1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityCameraApi1Binding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        showToast("using cam api 1")
    }

    override fun onPreview() {}

    override fun hideSensorDataViews() {
        binding.txtInclination.visibility = View.GONE
        binding.txtDirection.visibility = View.GONE
    }

    override fun showBatteryStatus(level: Int) {
        binding.txtBatteryStatus.text =
            getString(R.string.battery_status) + ": " + level + "%"
    }


    override fun onError(exception: java.lang.Exception?) {
        Log.e(javaClass.simpleName, exception?.localizedMessage ?: "")
    }

    private fun setViews() {
        binding.btnAction.setOnClickListener { v: View? ->
            if (isRecordingVideo) {
                stopRecUI()
            } else {
                prepareVideoRecording()
            }
        }
    }

    override fun enableScreen() {
        super.enableScreen()
        cameraUsecase = CameraApi1Usecase(this, binding.preview, this, videoFile!!)
        setViews()
        cameraUsecase?.init(binding.preview)
        cameraUsecase?.startPreview()
    }

    override fun updateTime() {
        runOnUiThread {
            binding.txtTimer.text = "$count secs"
        }
    }

    override fun onVideoRecordingStarted() {
        binding.btnAction.text = getString(R.string.stop)
    }

    override fun updateSnapshotValuesUI(strRollAngleForText: String, strAzimuth: String) {
//        binding.txtInclination.text = getString(R.string.inc_angle) + " " + strRollAngleForText
//        binding.txtDirection.text = getString(R.string.dir_angle) + " " + strAzimuth
    }


}