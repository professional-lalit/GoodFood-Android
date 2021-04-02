package com.goodfood.app.ui.home.fragments.create_recipe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.goodfood.app.R
import com.goodfood.app.databinding.FragmentCreateRecipeBinding
import com.goodfood.app.databinding.FragmentExploreBinding
import com.goodfood.app.models.domain.MediaUploadState
import com.goodfood.app.models.domain.RecipePhoto
import com.goodfood.app.models.domain.RecipeVideo
import com.goodfood.app.ui.common.BaseFragment
import com.goodfood.app.ui.home.fragments.explore.ExploreFragment


class CreateRecipeFragment : BaseFragment() {

    private lateinit var binding: FragmentCreateRecipeBinding

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
        recipePhotoListController.setData(
            listOf(
                RecipePhoto(
                    null,
                    MediaUploadState.NOT_UPLOADING,
                    true
                )
            )
        )
        val recipeVideoListController = RecipeMultimediaListController()
        recipeVideoListController.setData(
            listOf(
                RecipeVideo(
                    null,
                    MediaUploadState.NOT_UPLOADING,
                    true
                )
            )
        )
        binding.recyclerRecipePhotos.setController(recipePhotoListController)
        binding.recyclerRecipeVideos.setController(recipeVideoListController)
    }


}