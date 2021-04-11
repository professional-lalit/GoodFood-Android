package com.goodfood.app.ui.home.fragments.create_recipe

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.goodfood.app.R
import com.goodfood.app.common.Constants
import com.goodfood.app.common.multimedia_managers.RecipeMultimediaManager
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

    lateinit var recipeMultimediaManager: RecipeMultimediaManager

    private lateinit var binding: FragmentCreateRecipeBinding

    private val photos = mutableListOf<RecipePhoto>()
    private val videos = mutableListOf<RecipeVideo>()
    private val recipePhotoListController = RecipeMultimediaListController()
    private val recipeVideoListController = RecipeMultimediaListController()


    companion object {
        fun newInstance(bundle: Bundle? = null): CreateRecipeFragment {
            val fragment = CreateRecipeFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViews()

        setUpListControllers()

        setUpImageFileCallbackAndRegisterResults()
    }

    override fun onActivityCreated() {

    }

    private fun setViews() {
        binding.btnPickPhoto.setOnClickListener {
            checkPermissionsAndShowDialog("")
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
        dialogManager.showProfilePicDialog(childFragmentManager) { selection ->
            if (selection == Constants.CAMERA_SELECTED) {
                recipeMultimediaManager.initCameraFlow(desiredFileName)
            } else {
                recipeMultimediaManager.initGalleryFlow(desiredFileName)
            }
        }
    }

    private fun setUpImageFileCallbackAndRegisterResults() {
        recipeMultimediaManager.setImageLoadedCallback { file ->
            val photoItemToBeSet = photos.find { recipePhoto ->
                recipePhoto.state == MediaState.MEDIA_TO_BE_SET
            }
            if (photoItemToBeSet != null) {
                //val index = recipePhotoListController.currentData?.indexOf(photoItemToBeSet)
                photoItemToBeSet.imgUri = Uri.fromFile(file)
                photoItemToBeSet.state = MediaState.NOT_UPLOADING
                recipePhotoListController.setData(photos)
            } else {
                val photoItemToAdd =
                    RecipePhoto(
                        Uri.fromFile(file),
                        MediaState.NOT_UPLOADING
                    )
                photos.add(0, photoItemToAdd)
                recipePhotoListController.setData(photos)
            }
            dialogManager.closeDialog()
        }
    }

    override fun onClickEvent(event: ClickEventMessage) {
        super.onClickEvent(event)
        when (event.viewId) {
            R.id.img_recipe_photo -> {
                val recipePhoto = event.payload as RecipePhoto
                checkPermissionsAndShowDialog("CREATE_RECIPE_IMG_${photos.indexOf(recipePhoto)}")
            }
            R.id.img_recipe_video -> {

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        recipeMultimediaManager.deleteCreateRecipeImageFiles()
    }

}