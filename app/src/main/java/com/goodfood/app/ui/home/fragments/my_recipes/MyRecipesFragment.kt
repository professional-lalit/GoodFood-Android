package com.goodfood.app.ui.home.fragments.my_recipes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.goodfood.app.databinding.FragmentMyRecipesBinding
import com.goodfood.app.ui.common.BaseFragment
import com.goodfood.app.ui.home.fragments.explore.ExploreListController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyRecipesFragment : BaseFragment() {

    companion object {
        fun newInstance(bundle: Bundle? = null): MyRecipesFragment {
            val fragment = MyRecipesFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onActivityCreated() {

    }

    private val viewModel: MyRecipesViewModel by viewModels()
    private lateinit var binding: FragmentMyRecipesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyRecipesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addObservers()
        viewModel.loadRecipes()
    }

    private fun addObservers() {
        viewModel.recipeList.observe(viewLifecycleOwner, { list ->
            val controller = ExploreListController()
            controller.setData(list)
            binding.recyclerRecipes.setController(controller)
        })
    }
}