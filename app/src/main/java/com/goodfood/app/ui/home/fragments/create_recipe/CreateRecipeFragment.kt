package com.goodfood.app.ui.home.fragments.create_recipe

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import com.goodfood.app.R
import com.goodfood.app.common.Constants
import com.goodfood.app.common.multimedia_managers.RecipeMultimediaManager
import com.goodfood.app.databinding.FragmentCreateRecipeBinding
import com.goodfood.app.events.ClickEventMessage
import com.goodfood.app.events.EventConstants
import com.goodfood.app.events.Message
import com.goodfood.app.events.sendEvent
import com.goodfood.app.models.domain.MediaState
import com.goodfood.app.models.domain.RecipePhoto
import com.goodfood.app.models.domain.RecipeVideo
import com.goodfood.app.ui.common.BaseFragment
import com.goodfood.app.ui.common.dialogs.DialogManager
import com.goodfood.app.ui.common.dialogs.NotificationBottomDialog
import com.goodfood.app.ui.home.HomeActivity
import com.goodfood.app.utils.Extensions.showToast
import com.goodfood.app.utils.Utils
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class CreateRecipeFragment : BaseFragment() {


    @Inject
    lateinit var dialogManager: DialogManager

    lateinit var recipeMultimediaManager: RecipeMultimediaManager

    private lateinit var binding: FragmentCreateRecipeBinding

    private val viewModel: CreateRecipeViewModel by viewModels()

    private val recipePhotoListController = RecipeMultimediaListController()
    private val recipeVideoListController = RecipeMultimediaListController()

    private lateinit var photos: MutableList<RecipePhoto>
    private lateinit var videos: MutableList<RecipeVideo>

    companion object {
        fun newInstance(bundle: Bundle? = null): CreateRecipeFragment {
            val fragment = CreateRecipeFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        photos = viewModel.photos
        videos = viewModel.videos
        recipeMultimediaManager = (requireActivity() as HomeActivity).recipeMultimediaManager
        (requireActivity() as AppCompatActivity).supportActionBar?.title =
            getString(R.string.create_recipe)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateRecipeBinding.inflate(inflater)
        binding.model = viewModel.createRecipeUI
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViews()

        setUpListControllers()

        setUpFileCallbacks()
    }

    override fun addObservers() {
        viewModel.recipeUploadState.observe(viewLifecycleOwner, {
            when (it) {
                CreateRecipeViewModel.RecipeUploadState.INIT -> showBottomDialog(
                    getString(R.string.upload_started),
                    false
                )
                CreateRecipeViewModel.RecipeUploadState.PHOTOS_UPLOAD_INIT -> showBottomDialog(
                    getString(R.string.recipes_photos_are_uploading),
                    false
                )
                CreateRecipeViewModel.RecipeUploadState.PHOTOS_UPLOADED -> showBottomDialog(
                    getString(R.string.recipes_photos_uploaded),
                    false
                )
                CreateRecipeViewModel.RecipeUploadState.VIDEOS_UPLOAD_INIT -> showBottomDialog(
                    getString(R.string.recipes_videos_are_uploading),
                    false
                )
                CreateRecipeViewModel.RecipeUploadState.VIDEOS_UPLOADED -> showBottomDialog(
                    getString(R.string.recipe_videos_uploaded),
                    false
                )
                CreateRecipeViewModel.RecipeUploadState.SUCCESS -> {
                    showBottomDialog(
                        getString(R.string.recipe_uploaded),
                        false
                    )
                    sendEvent(EventConstants.Event.RECIPE_UPLOADED.id)
                }
                CreateRecipeViewModel.RecipeUploadState.FAILED -> showBottomDialog(
                    getString(R.string.recipe_upload_failed),
                    false
                )
            }
        })
        viewModel.currentUploadingPhoto.observe(viewLifecycleOwner, { photo ->
            val photoInListToUpdate = photos.find { it.imgUri == photo.imgUri }
            photoInListToUpdate?.let {
                it.uploadProgress = photo.uploadProgress
                it.state = MediaState.UPLOADING
                recipePhotoListController.notifyModelChanged(photos.indexOf(it))
            }
        })
        viewModel.currentUploadingVideo.observe(viewLifecycleOwner, { video ->
            val videoInListToUpdate = videos.find { it.videoThumbUri == video.videoThumbUri }
            videoInListToUpdate?.let {
                it.uploadProgress = video.uploadProgress
                it.state = MediaState.UPLOADING
                recipeVideoListController.notifyModelChanged(videos.indexOf(it))
            }
        })
    }

    private fun setViews() {
        binding.btnPickPhoto.setOnClickListener {
            checkPermissionsAndShowDialog("RECIPE_IMG_${photos.size}")
        }
        binding.btnPickVideo.setOnClickListener {
            showVideoDialog("RECIPE_VID_${videos.size}")
        }
        binding.btnSubmit.setOnClickListener {
            viewModel.uploadRecipe()
        }
    }

    private fun setUpListControllers() {
        recipePhotoListController.setData(photos)
        recipeVideoListController.setData(videos)
        binding.recyclerRecipePhotos.setController(recipePhotoListController)
        binding.recyclerRecipeVideos.setController(recipeVideoListController)
    }

    private fun checkPermissionsAndShowDialog(desiredFileName: String) {
        Dexter.withContext(requireContext())
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        showImageDialog(desiredFileName)
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

    private fun showImageDialog(desiredFileName: String) {
        dialogManager.showMultimediaSelectionDialog(childFragmentManager) { selection ->
            if (selection == Constants.CAMERA_SELECTED) {
                recipeMultimediaManager.initCameraFlow(desiredFileName)
            } else {
                recipeMultimediaManager.initGalleryFlow(desiredFileName)
            }
        }
    }

    private fun showVideoDialog(desiredFileName: String) {
        dialogManager.showMultimediaSelectionDialog(childFragmentManager) { selection ->
            if (selection == Constants.CAMERA_SELECTED) {
                recipeMultimediaManager.initVideoRecordFlow(desiredFileName)
            } else {
                recipeMultimediaManager.initVideoGalleryPickFlow(desiredFileName)
            }
        }
    }

    private fun setUpFileCallbacks() {
        recipeMultimediaManager.setImageLoadedCallback { file ->
            val photoItemToBeSet = photos.find { recipePhoto ->
                recipePhoto.state == MediaState.MEDIA_TO_BE_SET
            }
            if (photoItemToBeSet != null) {
                photoItemToBeSet.imgUri = Uri.fromFile(file)
                photoItemToBeSet.state = MediaState.NOT_UPLOADING
                recipePhotoListController.setData(photos)
            } else {
                val photoItemToAdd =
                    RecipePhoto(
                        Uri.fromFile(file),
                        MediaState.NOT_UPLOADING
                    )
                photoItemToAdd.uploadProgress = 0
                photos.add(0, photoItemToAdd)
                recipePhotoListController.setData(photos)
            }
            dialogManager.closeDialog()
        }
        recipeMultimediaManager.setVideoLoadedCallback { file, thumbFile ->
            dismissFileCopyNotification()
            val videoItemToBeSet = videos.find { recipeVideo ->
                recipeVideo.state == MediaState.MEDIA_TO_BE_SET
            }
            if (videoItemToBeSet != null) {
                videoItemToBeSet.videoUri = file.toUri()
                videoItemToBeSet.videoThumbUri = thumbFile.toUri()
                videoItemToBeSet.state = MediaState.NOT_UPLOADING
                recipeVideoListController.setData(videos)
            } else {
                val videoItemToAdd =
                    RecipeVideo(
                        thumbFile.toUri(),
                        file.toUri(),
                        MediaState.NOT_UPLOADING
                    )
                videos.add(0, videoItemToAdd)
                recipeVideoListController.setData(videos)
            }
        }
    }

    private fun dismissFileCopyNotification() {
        Handler(Looper.getMainLooper()).postDelayed({
            val fragment = childFragmentManager.findFragmentByTag(NotificationBottomDialog.TAG)
            if (fragment is NotificationBottomDialog? && fragment?.isVisible == true) {
                fragment?.dismiss()
            }
        }, 1000L)
    }

    override fun onEvent(event: Message) {
        super.onEvent(event)
        when (event.eventId) {
            EventConstants.Event.COPYING_VIDEO_FILE.id -> {
                NotificationBottomDialog
                    .getInstance(getString(R.string.copying_file_plz_wt))
                    .show(childFragmentManager, NotificationBottomDialog.TAG)
            }
        }
    }

    override fun onClickEvent(event: ClickEventMessage) {
        super.onClickEvent(event)

        if (viewModel.recipeUploadState.value != CreateRecipeViewModel.RecipeUploadState.SUCCESS
            || viewModel.recipeUploadState.value != CreateRecipeViewModel.RecipeUploadState.FAILED
        ) {
            showBottomDialog(getString(R.string.plz_wt_until_files_upload), true)
            return
        }

        when (event.viewId) {
            R.id.img_recipe_photo -> {
                val recipePhoto = event.payload as RecipePhoto
                checkPermissionsAndShowDialog("RECIPE_IMG_${photos.indexOf(recipePhoto)}")
            }
            R.id.img_recipe_video -> {
                val recipeVideo = event.payload as RecipeVideo
                showVideoDialog("RECIPE_VID_${videos.indexOf(recipeVideo)}")
            }
            R.id.img_delete -> {
                if (event.payload is RecipeVideo) {
                    val recipeVideo = event.payload
                    videos.remove(recipeVideo)
                    recipeVideoListController.setData(videos)
                } else if (event.payload is RecipePhoto) {
                    val recipePhoto = event.payload
                    photos.remove(recipePhoto)
                    recipePhotoListController.setData(photos)
                }
            }
        }
    }

    private fun showBottomDialog(msg: String, isTimedDismissal: Boolean) {
        if (NotificationBottomDialog.isShown) {
            sendEvent(EventConstants.Event.UPDATE_BOTTOM_NOTIFICATION_TITLE.id, msg)
        } else {
            NotificationBottomDialog
                .getInstance(msg, isTimedDismissal)
                .show(childFragmentManager, NotificationBottomDialog.TAG)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        recipeMultimediaManager.deleteCreateRecipeMultimedia()
    }

}