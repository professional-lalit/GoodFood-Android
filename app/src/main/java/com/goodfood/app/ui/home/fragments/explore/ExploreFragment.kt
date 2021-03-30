package com.goodfood.app.ui.home.fragments.explore

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.goodfood.app.databinding.FragmentExploreBinding
import com.goodfood.app.models.domain.Recipe

class ExploreFragment : Fragment() {

    private lateinit var binding: FragmentExploreBinding

    companion object {
        fun newInstance(bundle: Bundle? = null): ExploreFragment {
            val fragment = ExploreFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExploreBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recipes = mutableListOf<Recipe>()
        repeat(5) {
            recipes.add(Recipe("Recipe $it", "", "", ""))
        }

        val adapter = RecipeAdapter()
        adapter.setData(recipes, false)
        binding.recyclerRecipes.adapter = adapter
        binding.recyclerRecipes.layoutManager = LinearLayoutManager(requireContext())
    }

}