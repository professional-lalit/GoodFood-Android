package com.goodfood.app.ui.home.fragments.explore

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.goodfood.app.R
import com.goodfood.app.databinding.FragmentExploreBinding
import com.goodfood.app.events.ClickEventMessage
import com.goodfood.app.events.EventConstants
import com.goodfood.app.events.Message
import com.goodfood.app.events.sendEvent
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
            recipes.add(Recipe("Recipe $it", getString(R.string.lorem_brief), "", 15))
        }

        val controller = RecipeController()
        controller.setData(recipes)
        binding.recyclerRecipes.setController(controller)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_create_recipe, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_add_recipe) {
            sendEvent(EventConstants.Event.OPEN_CREATE_RECIPE_SCREEN.id)
        }
        return super.onOptionsItemSelected(item)
    }

}