package com.goodfood.app.ui.home.fragments.create_recipe

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.goodfood.app.R
import com.goodfood.app.common.Constants
import com.goodfood.app.databinding.FragmentCreateRecipeBinding
import com.goodfood.app.events.ClickEventMessage
import com.goodfood.app.models.domain.MediaState
import com.goodfood.app.models.domain.RecipePhoto
import com.goodfood.app.models.domain.RecipeVideo
import com.goodfood.app.ui.common.BaseFragment
import com.goodfood.app.ui.common.dialogs.DialogManager
import com.goodfood.app.ui.home.HomeActivity
import com.goodfood.app.utils.Extensions.showToast
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

    private lateinit var binding: FragmentCreateRecipeBinding

    private val photos = mutableListOf<RecipePhoto>()
    private val videos = mutableListOf<RecipeVideo>()

    companion object {
        fun newInstance(bundle: Bundle? = null): CreateRecipeFragment {
            val fragment = CreateRecipeFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.title =
            getString(R.string.create_recipe)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateRecipeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recipePhotoListController = RecipeMultimediaListController()
        photos.add(RecipePhoto(null, MediaState.NOT_UPLOADING, true))
        recipePhotoListController.setData(photos)

        val recipeVideoListController = RecipeMultimediaListController()
        videos.add(RecipeVideo(null, MediaState.NOT_UPLOADING, true))
        recipeVideoListController.setData(videos)

        binding.recyclerRecipePhotos.setController(recipePhotoListController)
        binding.recyclerRecipeVideos.setController(recipeVideoListController)

        (requireActivity() as HomeActivity).recipeMultimediaManager.setImageLoadedCallback { file ->

            Log.d(javaClass.simpleName, "FILE PATH: ${file.absolutePath}")

            val photoItemToBeSet = photos.find { recipePhoto ->
                recipePhoto.state == MediaState.MEDIA_TO_BE_SET
            }
            if (photoItemToBeSet != null) {
                photoItemToBeSet.imgUri = Uri.fromFile(file)
                photoItemToBeSet.state = MediaState.NOT_UPLOADING
                recipePhotoListController.addInterceptor {models ->
                    recipePhotoListController.requestModelBuild()
                }
            } else {
                val photoItemToAdd =
                    RecipePhoto(
                        Uri.fromFile(file), MediaState.NOT_UPLOADING,
                        false
                    )
                photos.add(0, photoItemToAdd)
                recipePhotoListController.setData(photos)
            }
            dialogManager.closeDialog()
        }
    }

    private fun checkPermissionsAndShowDialog(fileName: String) {
        Dexter.withContext(requireContext())
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        showImageDialog(fileName)
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

    private fun showImageDialog(fileName: String) {
        dialogManager.showProfilePicDialog(childFragmentManager) { selection ->
            if (selection == Constants.CAMERA_SELECTED) {
                (requireActivity() as HomeActivity).recipeMultimediaManager.initCameraFlow(fileName)
            } else {
                (requireActivity() as HomeActivity).recipeMultimediaManager.initGalleryFlow(fileName)
            }
        }
    }

    override fun onClickEvent(event: ClickEventMessage) {
        super.onClickEvent(event)
        val itemData = event.payload
        when (event.viewId) {
            R.id.img_recipe_photo -> {
                val fileName = "RECIPE_IMG_${photos.indexOf(itemData)}.jpg"
                checkPermissionsAndShowDialog(fileName)
            }
            R.id.img_recipe_video -> {

            }
        }
    }


}