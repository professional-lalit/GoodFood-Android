package com.goodfood.app.ui.home.fragments.recipe_detail

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.goodfood.app.R
import com.goodfood.app.databinding.FragmentRecipeDetailBinding
import com.goodfood.app.models.domain.Recipe
import com.goodfood.app.ui.common.BaseFragment
import com.goodfood.app.ui.home.fragments.explore.ExploreListController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val PAGER_SCROLL_DELAY = 5000L

@AndroidEntryPoint
class RecipeDetailFragment : BaseFragment() {

    private lateinit var binding: FragmentRecipeDetailBinding
    private lateinit var recipeId: String

    private val viewModel: RecipeDetailsViewModel by viewModels()

    override fun addObservers() {
        viewModel.recipeDetails.observe(viewLifecycleOwner, { details ->
            val recipe = details.recipe
            binding.model = recipe
            if (details.recipe.imgUrls?.isNotEmpty() == true) {
                setPagerScroll()
            }
            if(details.recipe.videos?.isNotEmpty() == true){
                val controller = RecipeVideoListController()
                controller.setData(details.recipe.videos)
                binding.recyclerRecipeVideos.setController(controller)
                binding.linRecipeVideos.visibility = View.VISIBLE
            }else{
                binding.linRecipeVideos.visibility = View.GONE
            }
        })
    }

    private fun setPagerScroll() {
        lifecycleScope.launch {
            while (true) {
                delay(PAGER_SCROLL_DELAY)
                binding.pager.nextPage()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecipeDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        recipeId = arguments?.getSerializable(ARG_RECIPE_ID) as String
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadRecipeDetails(recipeId)
    }

    companion object {
        private const val ARG_RECIPE_ID = "arg_recipe_id"

        @JvmStatic
        fun newInstance(recipeId: String) =
            RecipeDetailFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_RECIPE_ID, recipeId)
                }
            }
    }
}