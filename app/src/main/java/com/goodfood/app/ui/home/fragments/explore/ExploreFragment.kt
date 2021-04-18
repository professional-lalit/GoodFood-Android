package com.goodfood.app.ui.home.fragments.explore

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.goodfood.app.R
import com.goodfood.app.common.Constants
import com.goodfood.app.databinding.FragmentExploreBinding
import com.goodfood.app.events.ClickEventMessage
import com.goodfood.app.events.EventConstants
import com.goodfood.app.events.Message
import com.goodfood.app.events.sendEvent
import com.goodfood.app.models.domain.BooleanQuestion
import com.goodfood.app.models.domain.Inspiration
import com.goodfood.app.models.domain.Recipe
import com.goodfood.app.ui.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExploreFragment : BaseFragment() {

    companion object {
        fun newInstance(bundle: Bundle? = null): ExploreFragment {
            val fragment = ExploreFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var binding: FragmentExploreBinding

    private val viewModel: ExploreViewModel by viewModels()

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

    override fun onActivityCreated() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addObservers()
        viewModel.loadRecipes()
    }

    private fun addObservers() {
        viewModel.recipeList.observe(viewLifecycleOwner, {
            val controller = ExploreListController()
            controller.setData(it)
            binding.recyclerRecipes.setController(controller)
        })
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

    override fun onClickEvent(event: ClickEventMessage) {
        val model = event.payload as Recipe
        Toast.makeText(
            requireContext(),
            "Event received, model: ${model.recipeTitle}",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onEvent(event: Message) {
        super.onEvent(event)
        if (event.eventId == EventConstants.Event.RECIPE_UPLOADED.id) {
            viewModel.loadRecipes()
        }
    }


}