package com.goodfood.app.ui.home.fragments.recipe_detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.goodfood.app.R
import com.goodfood.app.databinding.FragmentRecipeDetailBinding
import com.goodfood.app.models.domain.Recipe
import com.goodfood.app.ui.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeDetailFragment : BaseFragment() {

    private lateinit var binding: FragmentRecipeDetailBinding
    private lateinit var recipeId: String

    private val viewModel: RecipeDetailsViewModel by viewModels()

    override fun onActivityCreated() {

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
        addDetailsObserver()
        viewModel.loadRecipeDetails(recipeId)
    }

    private fun addDetailsObserver() {
        viewModel.recipeDetails.observe(viewLifecycleOwner, { details ->
            val recipe = details.recipe
            binding.model = recipe
        })
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