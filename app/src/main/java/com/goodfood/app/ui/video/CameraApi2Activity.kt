package com.goodfood.app.ui.video

import android.graphics.SurfaceTexture
import android.os.Bundle
import android.util.Log
import android.view.TextureView
import android.view.View
import com.goodfood.app.R
import com.goodfood.app.databinding.ActivityCameraApi2Binding
import com.goodfood.app.utils.Extensions.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CameraApi2Activity : BaseCameraActivity(),
    ICameraUsecase.CameraCallback {

    private lateinit var binding: ActivityCameraApi2Binding


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityCameraApi2Binding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        showToast("using cam api 2")
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

    override fun onPreview() {
        cameraUsecase?.startPreview()
    }

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
        if (exception is ICameraUsecase.CameraApi2NotSupportedException) {
            showToast(getString(R.string.plz_try_again_sorry_abt_incv))
            finish()
        }
    }

    override fun enableScreen() {
        super.enableScreen()
        cameraUsecase = CameraApi2Usecase(this, this, videoFile!!)
        setViews()
        cameraUsecase?.init(binding.preview)
        binding.preview.surfaceTextureListener = mSurfaceTextureListener
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

    private var mSurfaceTextureListener: TextureView.SurfaceTextureListener = object :
        TextureView.SurfaceTextureListener {
        override fun onSurfaceTextureAvailable(
            surfaceTexture: SurfaceTexture,
            width: Int, height: Int
        ) {
            (cameraUsecase as CameraApi2Usecase).openCamera(width, height)
        }

        override fun onSurfaceTextureSizeChanged(
            surfaceTexture: SurfaceTexture,
            width: Int, height: Int
        ) {
            (cameraUsecase as CameraApi2Usecase).configureTransform(width, height)
        }

        override fun onSurfaceTextureDestroyed(surfaceTexture: SurfaceTexture): Boolean {
            return true
        }

        override fun onSurfaceTextureUpdated(surfaceTexture: SurfaceTexture) {}
    }


}