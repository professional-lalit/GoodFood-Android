package com.goodfood.app.ui.home.fragments.explore

import android.os.Bundle
import android.view.*
import android.widget.Toast
import com.goodfood.app.R
import com.goodfood.app.common.Constants
import com.goodfood.app.databinding.FragmentExploreBinding
import com.goodfood.app.events.ClickEventMessage
import com.goodfood.app.events.EventConstants
import com.goodfood.app.events.sendEvent
import com.goodfood.app.models.domain.BooleanQuestion
import com.goodfood.app.models.domain.Inspiration
import com.goodfood.app.models.domain.Recipe
import com.goodfood.app.ui.common.BaseFragment

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

        val list = mutableListOf<Any>()

        list.add(
            Recipe(
                "Easy Breakfast Recipe -Nutri Vegetable Roastie",
                getString(R.string.lorem_brief),
                "${Constants.BASE_URL}recipe_images/veg-roastie.jpg",
                15
            )
        )

        list.add(
            Recipe(
                "New snack recipe | No Oven, No Maida, No baking powder & Soda | Easy&Simple Puff pastry | Snacks",
                getString(R.string.lorem_brief),
                "${Constants.BASE_URL}recipe_images/cheeze-puff.jpg",
                0
            )
        )

        list.add(
            Recipe(
                "Crispy Balls - Instant Snack to be made in 5 mins - Sooji/Semolina Snacks",
                getString(R.string.lorem_brief),
                "${Constants.BASE_URL}recipe_images/spicey-balls.jpg",
                20
            )
        )

        list.add(
            Inspiration(
                "When you eat food with your family and friends, it always tastes better!",
                "${Constants.BASE_URL}recipe_images/inspiration.jpg"
            )
        )

        list.add(
            BooleanQuestion(
                "Do you like fast food? Do want to learn the skills for Burgers, Pizzas, Fried Ribs, Chicken Breast?"
            )
        )

        val controller = ExploreListController()
        controller.setData(list)
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