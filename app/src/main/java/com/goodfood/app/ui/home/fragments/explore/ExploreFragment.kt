package com.goodfood.app.ui.home.fragments.explore

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.goodfood.app.databinding.FragmentExploreBinding
import com.goodfood.app.events.ClickEventMessage
import com.goodfood.app.events.Message
import com.goodfood.app.models.domain.Recipe
import com.goodfood.app.ui.common.BaseFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ExploreFragment : BaseFragment() {

    private lateinit var binding: FragmentExploreBinding

    companion object {
        fun newInstance(bundle: Bundle? = null): ExploreFragment {
            val fragment = ExploreFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onClickEvent(event: ClickEventMessage) {
        val model = event.payload as Recipe
        Toast.makeText(
            requireContext(),
            "Event received, model: ${model.recipeTitle}",
            Toast.LENGTH_SHORT
        ).show()
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

        val controller = RecipeController()
        controller.setData(recipes)
        binding.recyclerRecipes.setController(controller)

    }

}