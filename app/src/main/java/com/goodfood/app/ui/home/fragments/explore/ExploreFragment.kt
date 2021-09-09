package com.goodfood.app.ui.home.fragments.explore

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.isEmpty
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.goodfood.app.R
import com.goodfood.app.common.Constants
import com.goodfood.app.databinding.FragmentExploreBinding
import com.goodfood.app.events.*
import com.goodfood.app.interfaces.IClickListener
import com.goodfood.app.models.domain.BooleanQuestion
import com.goodfood.app.models.domain.Inspiration
import com.goodfood.app.models.domain.Recipe
import com.goodfood.app.ui.common.BaseFragment
import com.goodfood.app.ui.common.dialogs.CommentsBottomDialog
import com.goodfood.app.ui.home.fragments.recipe_detail.RecipeDetailFragment
import com.ncapdevi.fragnav.FragNavTransactionOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExploreFragment : BaseFragment(), IClickListener {

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
    ): View {
        binding = FragmentExploreBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewModel.recipeList.value == null) {
            viewModel.loadRecipes()
        }
    }

    override fun addObservers() {
        viewModel.recipeList.observe(viewLifecycleOwner, {
            val controller = ExploreListController()
            controller.clickListener = this
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

    override fun onEvent(event: Message) {
        super.onEvent(event)
        if (event.eventId == EventConstants.Event.RECIPE_UPLOADED.id) {
            viewModel.loadRecipes()
        }
    }

    override fun onClick(view: View, model: Any?) {
        when (view.id) {
            R.id.txt_comments_count -> {
                val recipe = model as Recipe
                CommentsBottomDialog.newInstance(recipe)
                    .show(childFragmentManager, CommentsBottomDialog.TAG)
            }
            R.id.cl_root -> {
                sendClickEvent(viewId = view.id, model)
            }
        }
    }


}