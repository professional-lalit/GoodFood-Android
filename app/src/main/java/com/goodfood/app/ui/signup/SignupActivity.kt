package com.goodfood.app.ui.signup

import android.Manifest
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.goodfood.app.R
import com.goodfood.app.common.Constants
import com.goodfood.app.common.DirectoryManager
import com.goodfood.app.common.ImageManager
import com.goodfood.app.databinding.ActivitySignupBinding
import com.goodfood.app.interfaces.Navigable
import com.goodfood.app.ui.common.BaseActivity
import com.goodfood.app.ui.common.BaseViewModel
import com.goodfood.app.ui.common.dialogs.DialogManager
import com.goodfood.app.utils.Extensions.showToast
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import javax.inject.Inject


@AndroidEntryPoint
class SignupActivity : BaseActivity<ActivitySignupBinding, SignupViewModel>() {

    companion object {
        fun <B : ViewDataBinding, VM : BaseViewModel, T : BaseActivity<B, VM>> openScreen(
            activity: T,
            bundle: Bundle? = null
        ) {
            val intent = Intent(activity, SignupActivity::class.java)
            bundle?.let { intent.putExtras(bundle) }
            activity.startActivity(intent)
        }
    }

    @Inject
    lateinit var dialogManager: DialogManager

    @Inject
    lateinit var directoryManager: DirectoryManager

    @Inject
    lateinit var imageManager: ImageManager

    private var imageFileToBeUploaded: File? = null

    private val signupViewModel by lazy {
        ViewModelProvider(this)
            .get(SignupViewModel::class.java)
    }

    override fun setUp() {
        binding = ActivitySignupBinding.inflate(layoutInflater)
        viewModel = signupViewModel
        imageManager.setImageLoadedCallback { file ->
            imageFileToBeUploaded = file
            val bmp = BitmapFactory.decodeFile(imageFileToBeUploaded!!.absolutePath)
            Glide.with(this)
                .load(bmp)
                .circleCrop()
                .into(binding.imgProfile)
            dialogManager.closeDialog()
        }
    }

    override fun navigateTo(navigable: Navigable) {
        when (navigable) {
            BaseViewModel.SignupNav.PROFILE_PIC -> {
                checkPermissionsAndShowDialog()
            }
            BaseViewModel.SignupNav.LOGIN -> {
                finish()
            }
        }
    }

    override fun setObservers() {
        viewModel.validationMessage.observe(this, { code ->
            when (code) {
                SignupData.ValidationCode.FIRST_NAME_EMPTY -> {
                    showToast(getString(R.string.please_enter_first_name))
                }
                SignupData.ValidationCode.LAST_NAME_EMPTY -> {
                    showToast(getString(R.string.please_enter_last_name))
                }
                SignupData.ValidationCode.MOBILE_NUMBER_EMPTY -> {
                    showToast(getString(R.string.please_enter_mobile_number))
                }
                SignupData.ValidationCode.MOBILE_NUMBER_INVALID -> {
                    showToast(getString(R.string.please_enter_valid_mobile_number))
                }
                SignupData.ValidationCode.EMAIL_EMPTY -> {
                    showToast(getString(R.string.please_enter_email))
                }
                SignupData.ValidationCode.EMAIL_INVALID -> {
                    showToast(getString(R.string.please_enter_valid_email))
                }
                SignupData.ValidationCode.PASSWORD_EMPTY -> {
                    showToast(getString(R.string.please_enter_password))
                }
                SignupData.ValidationCode.PASSWORD_INVALID -> {
                    showToast(getString(R.string.please_enter_valid_password))
                }
                SignupData.ValidationCode.PASSWORD_LENGTH_INVALID -> {
                    showToast(getString(R.string.password_atleast_6_chars))
                }
                SignupData.ValidationCode.CONFIRM_PASSWORD_EMPTY -> {
                    showToast(getString(R.string.please_confirm_password))
                }
                SignupData.ValidationCode.PASSWORDS_NOT_MATCHING -> {
                    showToast(getString(R.string.passwords_dont_match))
                }
                else -> {
                    showToast("Validated")
                }
            }
        })
        viewModel.errorData.observe(this, { })
        viewModel.serverMessage.observe(this, { })
    }

    private fun checkPermissionsAndShowDialog() {
        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        showProfileImageDialog()
                    } else {
                        showToast(getString(R.string.profile_pic_perm_note))
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            }).check()
    }

    private fun showProfileImageDialog() {
        dialogManager.showProfilePicDialog(supportFragmentManager) { selection ->
            if (selection == Constants.CAMERA_SELECTED) {
                imageManager.initCameraFlow()
            } else {
                imageManager.initGalleryFlow()
            }
        }
    }

}