package com.goodfood.app.ui.home.fragments.recipe_detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.goodfood.app.R
import com.goodfood.app.databinding.FragmentRecipeDetailBinding
import com.goodfood.app.models.domain.Recipe
import com.goodfood.app.ui.common.BaseFragment


class RecipeDetailFragment : BaseFragment() {

    private lateinit var binding: FragmentRecipeDetailBinding
    private var recipe: Recipe? = null

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
        recipe = arguments?.getSerializable(ARG_RECIPE_ID) as Recipe?
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recipe?.let {
            binding.model = it
        }
    }

    companion object {
        private const val ARG_RECIPE_ID = "arg_recipe_id"

        @JvmStatic
        fun newInstance(recipeId: Recipe) =
            RecipeDetailFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_RECIPE_ID, recipeId)
                }
            }
    }
}